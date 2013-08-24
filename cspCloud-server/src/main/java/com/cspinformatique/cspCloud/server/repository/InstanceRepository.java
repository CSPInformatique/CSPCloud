package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;

public interface InstanceRepository {
	public void createInstance(Instance instance);
	
	public void deleteInstance(int applicationId, int serverId);
	
	public List<Instance> getApplicationInstances(Application application);
	
	public List<Application> getApplicationsMissingInstances();
	
	public Instance getInstance(Application application, Server server);
	
	public List<Instance> getInstances();
	
	public List<Integer> getServerApplications(int serverId);
	
	public void updateLastDeployment(Instance instance, int deploymentId);
	
	public void updateRunningStatus(Instance instance);
}
