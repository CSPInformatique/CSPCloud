package com.cspinformatique.cspCloud.server.service;

import java.util.List;
import java.util.Map;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;

public interface ApplicationService {
	public void activateClustering(Application application);
	
	public void createApplication(Application application);
	
	public Instance createInstance(Application application, boolean errorDisplayed);
	
	public void deleteApplication(Application application);
	
	public Application getApplicationById(int id);
	
	public List<Application> getApplications();
	
	public Map<Integer, Application> getUserApplications(int accountId);
	
	public Application newApplication(String email);
	
	public void startApplication(Application application);
	
	public void stopApplication(Application application);
	
	public void updateApplicationStatus(Application application, String status);
}
