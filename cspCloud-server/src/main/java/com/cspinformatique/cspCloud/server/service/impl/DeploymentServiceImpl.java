package com.cspinformatique.cspCloud.server.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Deployment;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.repository.DeploymentRepository;
import com.cspinformatique.cspCloud.server.service.DeploymentService;
import com.cspinformatique.cspCloud.server.service.InstanceService;
@Component
@Transactional
public class DeploymentServiceImpl implements DeploymentService {	
	static final Logger logger = Logger.getLogger(DeploymentServiceImpl.class);
	
	@Autowired
	private CSPCRestTemplate restTemplate;
	
	@Autowired
	private DeploymentRepository deploymentRepository;
	
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
	private Params params;
	
	@Override
	public void deleteDeployments(Application application){
		this.deploymentRepository.deleteDeployments(application.getId());
	}
	
	@Override
	public void deploy(Application application, String packageUrl, String contextUrl, Date deploymentDate){
		// Persisting the deployment.
		this.deploymentRepository.createDeployment(
			new Deployment(0, application, packageUrl, contextUrl, deploymentDate)
		);
	}
	
	@Override
	public void executeDeployer(Application application){
		Deployment deployment = this.deploymentRepository.getLastDeployment(application.getId());
		
		// Identifing all the instance for this application.
		for(Instance instance : instanceService.getApplicationInstances(application)){
			if(	deployment != null && (
					instance.getLastDeployment() == null ||
					instance.getLastDeployment().getId() != deployment.getId()
				)
			){
				logger.info(
					"Deploying the newest package of  " + 
						application.getId() + "-" + application.getName() + 
						" to server " + instance.getServer().getServerUrl()
				);
				
				// The instance is not up to date, we need to deploy the newest version.
				boolean restartInstance = false;
				if(instance.getStatus().equals(Instance.STATUS_RUNNING)){
					restartInstance = true;
					instanceService.stopInstance(instance);
				}
				
				// Update the instance's status.
				instanceService.updateLastDeployment(instance, deployment.getId());
				
				// Sending the request to the server for a new deployment.
				this.restTemplate.exchange( 
					instance.getServer().getServerUrl() + "/deployment", 
					HttpMethod.PUT, 
					new HttpEntity<Deployment>( 
						deployment,
						RestUtil.createBasicAuthHeader(this.params.getSystemUser(), this.params.getSystemPassword()) 
					), 
					Deployment.class 
				);
				
				if(restartInstance){
					this.instanceService.startInstance(instance);
				}
				
				logger.info(
					"Package for  " + 
					application.getId() + "-" + application.getName() + 
					" deployed on server " + instance.getServer().getServerUrl() +
					"."
				);
			}
		}
	}
}
