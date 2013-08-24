package com.cspinformatique.cspCloud.server.aop.audit.impl;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.server.aop.audit.ApplicationAuditCreator;
import com.cspinformatique.cspCloud.server.aop.audit.AuditCreator;
import com.cspinformatique.cspCloud.server.entity.Audit;
import com.cspinformatique.cspCloud.server.exception.application.NoServerAvailableException;

@Aspect
@Component
public class ApplicationAuditCreatorImpl extends AuditCreator implements ApplicationAuditCreator {
	
	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.ApplicationService.createApplication(..)" +
		") && " +
		"args(application)"
	)
	public void createApplication(Application application) {}

	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.ApplicationService.createInstance(..)" +
		") && " +
		"args(application, errorDisplayed)"
	)
	public void createInstance(Application application, boolean errorDisplayed) {}
	
	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.ApplicationService.deleteApplication(..)" +
		") && " +
		"args(application)"
	)
	public void deleteApplication(Application application) {}

	@Override
	@Pointcut(	
		"execution(" +
		"	* com.cspinformatique.dacloudWeb.services.ApplicationService.startApplication(..)" +
		") && " +
		"args(application)"
	)
	public void startApplication(Application application) {}

	@Override
	@Pointcut(	
		"execution(" +
		"	* com.cspinformatique.dacloudWeb.services.ApplicationService.stopApplication(..)" +
		") && " +
		"args(application)"
	)
	public void stopApplication(Application application) {}

	@Override
	@Pointcut(	
		"execution(" +
		"	* com.cspinformatique.dacloudWeb.services.ApplicationService.updateApplicationStatus(..)" +
		") && " +
		"args(application, status)"
	)
	public void updateApplicationStatus(Application application, String status) {}
	
	@AfterReturning("createApplication(application)")
	public void logCreateApplication(Application application){
		this.createAudit("Application " + application + " created.",  Audit.TYPE_INFO);
	}

	@AfterThrowing(
		pointcut="createInstance(application, errorDisplayed)", 
		throwing="noServerAvailableException"
	)
	public void logCreateInstanceError(
		Application application, 
		boolean errorDisplayed,
		NoServerAvailableException noServerAvailableException
	){
		if(!errorDisplayed){
			this.createAudit(
				"Application " + application + " could not be deployed. No servers are available", 
				Audit.TYPE_ALERT
			);
		}
	}
	
	@AfterReturning("deleteApplication(application)")
	public void logDeleteApplication(Application application){
		this.createAudit("Application " + application + " deleted.", Audit.TYPE_INFO);
	}
	
	@AfterReturning("startApplication(application)")
	public void logStartApplication(Application application){
		this.createAudit("Application " + application + " started.", Audit.TYPE_INFO);
	}
	
	@AfterReturning("stopApplication(application)")
	public void logStopApplication(Application application){
		this.createAudit("Application " + application + " stopped.", Audit.TYPE_INFO);
	}
	
	@AfterReturning("updateApplicationStatus(application, status)")
	public void logUpdateApplicationStatus(Application application, String status){
		this.createAudit("Application " + application + " status now at : " + status , Audit.TYPE_INFO);
	}
}
