package com.cspinformatique.cspCloud.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.repository.InstanceRepository;
import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.InstanceService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerService;
import com.cspinformatique.cspCloud.server.service.ServerPortService;

@Component
@Transactional
public class InstanceServiceImpl implements InstanceService{
	static final Logger logger = Logger.getLogger(InstanceServiceImpl.class);
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private InstanceRepository instanceRepository;
	
	@Autowired
	private LoadBalancerService loadBalancerService;
	
	@Autowired
	private Params params;
	
	@Autowired
	private CSPCRestTemplate restTemplate;
	
	@Autowired
	private ServerPortService serverPortService;
	
	@Override
	public void createInstance(Instance instance){
		logger.info(
			"Creating a new instance for application " + 
				instance.getApplication().getId() + "-" + instance.getApplication().getName() + 
				" on server " + instance.getServer().getServerUrl()
		);
		
		// Persisting the instance.
		this.instanceRepository.createInstance(instance);
		
		// Creating the ports for the instance.
		this.serverPortService.generateNewPort(instance, ServerPort.TYPE_SHUTDOWN);
		this.serverPortService.generateNewPort(instance, ServerPort.TYPE_AJP);
		this.serverPortService.generateNewPort(instance, ServerPort.TYPE_HTTP);
		this.serverPortService.generateNewPort(instance, ServerPort.TYPE_HTTPS);
		
		// Creating the instance physically.		
		this.restTemplate.exchange( 
			instance.getServer().getServerUrl() + "/applications", 
			HttpMethod.PUT, 
			new HttpEntity<Application>( 
				instance.getApplication(),
				RestUtil.createBasicAuthHeader(this.params.getSystemUser(), this.params.getSystemPassword()) 
			), 
			Application.class 
		);
		
		// Configuring the instance physically.
		try{
			this.restTemplate.exchange( 
				instance.getServer().getServerUrl() + "/instance?configure", 
				HttpMethod.POST, 
				new HttpEntity<Instance>( 
					instance,
					RestUtil.createBasicAuthHeader(this.params.getSystemUser(), this.params.getSystemPassword()) 
				), 
				Instance.class 
			);
		}catch(Exception ex){
			// An exception occured. Physical instance must be delete.
			throw new RuntimeException(ex);
		}
		
		// Configuring the load balancers for the new instance.
		loadBalancerService.addInstanceToLoadBalancer(
			this.loadBalancerService.getApplicationLoadBalancer(instance.getApplication()), 
			instance
		);
		
		logger.info(
			"Instance for application " + 
				instance.getApplication().getId() + "-" + instance.getApplication().getName() + 
				" created on server " + instance.getServer().getServerUrl() +
				"."
		);
	}
	
	@Override
	public void deleteInstance(Instance instance){
		logger.info("Deleting instance " + instance);
		
		this.serverPortService.deleteInstancePorts(instance);
		
		this.loadBalancerService.deleteLoadBalancedInstance(instance);
		
		this.instanceRepository.deleteInstance(instance.getApplication().getId(), instance.getServer().getId());
		
		try{
			this.restTemplate.exchange( 
				instance.getServer().getServerUrl() + "/instance?delete", 
				HttpMethod.POST, 
				new HttpEntity<Instance>( 
					instance,
					RestUtil.createBasicAuthHeader(this.params.getSystemUser(), this.params.getSystemPassword()) 
				), 
				Instance.class 
			);
			
			logger.info("Instance " + instance + " deleted.");
		}catch(ResourceAccessException ressourceAccessEx){
			logger.warn("Server " + instance.getServer().getServerUrl() + " not available.");
		}
	}
	
	@Override
	public List<Instance> getApplicationInstances(Application application){
		return this.instanceRepository.getApplicationInstances(application);
	}
	
	@Override
	public List<Application> getApplicationsMissingInstances(){
		return this.instanceRepository.getApplicationsMissingInstances();
	}
	
	@Override
	public List<Instance> getInstances(){
		return this.instanceRepository.getInstances();
	}
	
	@Override
	public List<Instance> getServerInstances(Server server){
		List<Instance> instances = new ArrayList<Instance>();
		
		for(Integer applicationId : this.instanceRepository.getServerApplications(server.getId())){
			instances.add(
				this.instanceRepository.getInstance(
					this.applicationService.getApplicationById(applicationId), 
					server
				)
			);
		}
		
		return instances;
	}
	
	@Override
	public void startInstance(Instance instance){
		if(!instance.getStatus().equals(Instance.STATUS_RUNNING)){
			this.restTemplate.exchange( 
				instance.getServer().getServerUrl() + "/instance?start", 
				HttpMethod.POST, 
				new HttpEntity<Instance>( 
					instance,
					RestUtil.createBasicAuthHeader(this.params.getSystemUser(), this.params.getSystemPassword()) 
				), 
				Instance.class 
			);
			
			// Persist status in database.
			instance.setStatus(Instance.STATUS_RUNNING);
			this.instanceRepository.updateRunningStatus(instance);
		}
	}
	
	@Override
	public void stopInstance(Instance instance){
		if(instance.getStatus().equals(Instance.STATUS_RUNNING)){
			this.restTemplate.exchange( 
				instance.getServer().getServerUrl() + "/instance?stop", 
				HttpMethod.POST, 
				new HttpEntity<Instance>( 
					instance,
					RestUtil.createBasicAuthHeader(this.params.getSystemUser(), this.params.getSystemPassword()) 
				), 
				Instance.class 
			);
			
			// Persist status in database.
			instance.setStatus(Instance.STATUS_STOPPED);
			this.instanceRepository.updateRunningStatus(instance);
		}
	}
	
	@Override
	public void updateLastDeployment(Instance instance, int deploymentId){
		this.instanceRepository.updateLastDeployment(instance, deploymentId);
	}
}
