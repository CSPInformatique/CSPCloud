package com.cspinformatique.cspCloud.server.task.impl;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.server.exception.application.NoServerAvailableException;
import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.DeploymentService;
import com.cspinformatique.cspCloud.server.service.InstanceService;
import com.cspinformatique.cspCloud.server.task.InstancesMonitor;
import com.cspinformatique.cspCloud.server.task.ServersMonitor;

@Component
public class InstancesMonitorImpl implements InstancesMonitor {
	static final Logger logger = Logger.getLogger(InstancesMonitorImpl.class);
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private DeploymentService deploymentService;
	
	@Autowired
	private InstanceService instanceService;
	
	private boolean errorDisplayed;
	
	@Autowired
	private ServersMonitor serverMonitor;
	
	@PostConstruct
	public void init(){
		this.errorDisplayed = false;
	}
	
	@Override
	@Scheduled(fixedDelay=6000)
	public void monitorInstances() {
		if(this.serverMonitor.isStarted()){
			// Loading applications that has no instance.
			boolean error = false;
			for(Application application : this.instanceService.getApplicationsMissingInstances()){
				try{
					this.applicationService.createInstance(application, errorDisplayed);
					
					error = false;
					errorDisplayed = false;
				}catch(NoServerAvailableException noServerAvailableEx){
					error = true;
					if(!errorDisplayed){
						logger.warn(
							"Application " + application + " could not be deployed." +
							" No servers are available"
						);
						this.applicationService.stopApplication(application);
					}
				}
			}
			if(error){
				this.errorDisplayed = true;
			}
			
			// Identifying instances not running with latest versions of deployment.
			for(Application application : this.applicationService.getApplications()){
				this.deploymentService.executeDeployer(application);
			}
		}
	}

}
