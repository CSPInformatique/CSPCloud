package com.cspinformatique.cspCloud.server.aop.audit;

import com.cspinformatique.cspCloud.commons.entity.Application;

public interface ApplicationAuditCreator {	
	public void createApplication(Application application);
	
	public void deleteApplication(Application application);
	
	public void createInstance(Application application, boolean errorDisplayed);
	
	public void startApplication(Application application);
	
	public void stopApplication(Application application);
	
	public void updateApplicationStatus(Application application, String status);
}
