package com.cspinformatique.cspCloud.server.service;

import java.util.Map;

import com.cspinformatique.cspCloud.commons.entity.Instance;

public interface InstanceService {
	public void configureTomcatInstance(Instance instance);
	
	public void deleteInstance(Instance instance);
	
	public Map<String, Instance> getRunningInstances();
	
	public void startInstance(Instance instance);
	
	public void stopInstance(Instance instance);
}
