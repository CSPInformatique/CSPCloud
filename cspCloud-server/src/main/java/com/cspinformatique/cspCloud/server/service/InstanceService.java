package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;

public interface InstanceService {
	public void createInstance(Instance instance);
	
	public void deleteInstance(Instance instance);
	
	public List<Instance> getApplicationInstances(Application application);
	
	public List<Application> getApplicationsMissingInstances();
	
	public List<Instance> getInstances();
	
	public List<Instance> getServerInstances(Server server);
	
	public void startInstance(Instance instance);
	
	public void stopInstance(Instance instance);
	
	public void updateLastDeployment(Instance instance, int deploymentId);
}
