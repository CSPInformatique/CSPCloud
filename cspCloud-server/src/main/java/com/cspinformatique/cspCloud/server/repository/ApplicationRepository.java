package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Application;

public interface ApplicationRepository {
	public void activateClustering(int applicationId);
	
	public void activateLoadBalancing(int applicationId);
	
	public void createApplication(Application application);
	
	public void deleteApplication(int id);
	
	public int findApplicationId(String name, String email);
	
	public Application getApplicationById(int id);
	
	public List<Application> getApplications();
	
	public List<Integer> getUserApplications(int accountId);
	
	public void updateApplicationStatus(int applicationId, String status);
}
