package com.cspinformatique.cspCloud.server.aop.audit;

import com.cspinformatique.cspCloud.commons.entity.Instance;

public interface InstanceAuditCreator {
	public void createInstance(Instance instance);
	
	public void deleteInstance(Instance instance);
	
	public void startInstance(Instance instance);
	
	public void stopInstance(Instance instance);
}
