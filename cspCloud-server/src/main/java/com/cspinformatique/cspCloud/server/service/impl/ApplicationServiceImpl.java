package com.cspinformatique.cspCloud.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancer;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.factory.ApplicationFactory;
import com.cspinformatique.cspCloud.commons.factory.InstanceFactory;
import com.cspinformatique.cspCloud.commons.factory.LoadBalancerFactory;
import com.cspinformatique.cspCloud.server.exception.application.NoServerAvailableException;
import com.cspinformatique.cspCloud.server.repository.ApplicationRepository;
import com.cspinformatique.cspCloud.server.service.AccountService;
import com.cspinformatique.cspCloud.server.service.ApplicationKitService;
import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.DeploymentService;
import com.cspinformatique.cspCloud.server.service.InstanceService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerService;
import com.cspinformatique.cspCloud.server.service.ServerService;

@Component
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private ApplicationKitService applicationKitService;
	
//	@Autowired
//	private DACloudServerService daCloudServerService;
	
	@Autowired
	private DeploymentService deploymentService;
	
	@Autowired
	private LoadBalancerService loadBalancerService;
	@Autowired
	private ServerService serverService;
	
	@Autowired
	private InstanceService instanceService;
	
	@Override
	public void activateClustering(Application application){
		
	}
	
	@Override
	public void createApplication(Application application){
		// Persisting the application.
		applicationRepository.createApplication(application);
		application.setId(
				applicationRepository.findApplicationId(application.getName(), application.getAccount().getEmail())
		);
		
		// Creation of the load Balancers.
		this.createLoadBalancer(application);
	}
	
	@Override
	@Transactional(noRollbackFor=NoServerAvailableException.class)
	public Instance createInstance(Application application, boolean errorDisplayed){
		// Find the best available server.
		Server bestServer = this.serverService.findBestServerAvailable();
		
		if(bestServer != null){
			Instance instance =	InstanceFactory.getInstance(
									application, 
									bestServer, 
									null, 
									null, 
									Instance.STATUS_STOPPED
								);
			
			// Creation command on server found.
			this.instanceService.createInstance(instance);			
			
			// Configuration done. Launching the server.
			this.instanceService.startInstance(instance);
			
			if(application.getStatus().equals(Application.STATUS_STOPPED)){
				this.updateApplicationStatus(application, Application.STATUS_RUNNING);
			}
			return instance;
		}else{
			throw new NoServerAvailableException();
		}
	}
	
	private LoadBalancer createLoadBalancer(Application application){
		// Create a new load balancer in the database.
		LoadBalancer loadBalancer =	LoadBalancerFactory.getNewLoadBalancer(
										application, 
										LoadBalancer.TYPE_INSTANCES
									);
		
		// Creating the load balancer in the database and on every DACloud Servers.
		this.loadBalancerService.createLoadBalancer(loadBalancer);
		
		return loadBalancer;
	}
	
	@Override
	public void deleteApplication(Application application){
		// Delete every serverPorts and instances.
		this.deploymentService.deleteDeployments(application);
		
		for(Instance instance : this.instanceService.getApplicationInstances(application)){
			this.instanceService.deleteInstance(instance);
		}
		
		// Delete the application.
		this.applicationRepository.deleteApplication(application.getId());
	}
	
	@Override
	public Application getApplicationById(int id){
		Application application = this.applicationRepository.getApplicationById(id);
		
		this.loadApplicationKit(application);
		
		return application;
	}
	
	@Override
	public List<Application> getApplications(){
		return this.applicationRepository.getApplications();
	}
	
	@Override
	public Map<Integer, Application> getUserApplications(int accountId){
		Map<Integer, Application> applications = new HashMap<Integer, Application>();
		
		for(Integer integer : this.applicationRepository.getUserApplications(accountId)){
			applications.put(integer, this.getApplicationById(integer));
		}
		
		return applications;
	}
	
	private void loadApplicationKit(Application application){
		application.setApplicationKit(
			this.applicationKitService.getApplicationKitByName(application.getApplicationKit().getName())
		);
	}
	
	@Override
	public Application newApplication(String email){
		Application application =	ApplicationFactory.getNewApplication(
										this.accountService.getAccount(email)
									);
		
		return application;
	}
	
	@Override
	public void startApplication(Application application){
		for(Instance instance : this.instanceService.getApplicationInstances(application)){
			this.instanceService.startInstance(instance);
		}
		this.updateApplicationStatus(application, Application.STATUS_RUNNING);
	}
	
	@Override
	public void stopApplication(Application application){
		for(Instance instance : this.instanceService.getApplicationInstances(application)){
			this.instanceService.stopInstance(instance);
		}
		this.updateApplicationStatus(application, Application.STATUS_STOPPED);
	}
	
	@Override
	public void updateApplicationStatus(Application application, String status){
		this.applicationRepository.updateApplicationStatus(application.getId(), status);
	}
}
