package com.cspinformatique.cspCloud.server.aop.audit.impl;

import org.aspectj.lang.annotation.Pointcut;

import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.server.aop.audit.AuditCreator;
import com.cspinformatique.cspCloud.server.aop.audit.InstanceAuditCreator;
import com.cspinformatique.cspCloud.server.entity.Audit;

public class InstanceAuditCreatorImpl extends AuditCreator implements InstanceAuditCreator {
	
	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.InstanceService.createInstance(..)" +
		") && " +
		"args(instance)"
	)
	public void createInstance(Instance instance){}
	
	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.InstanceService.deleteInstance(..)" +
		") && " +
		"args(instance)"
	)
	public void deleteInstance(Instance instance){}
	
	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.InstanceService.startInstance(..)" +
		") && " +
		"args(instance)"
	)
	public void startInstance(Instance instance){}
	
	@Override
	@Pointcut(
		"execution(" +
		"	* " +
		"	com.cspinformatique.dacloudWeb.services.InstanceService.stopInstance(..)" +
		") && " +
		"args(instance)"
	)
	public void stopInstance(Instance instance){}
	
	public void logCreateInstance(Instance instance){
		this.createAudit("Instance " + instance + " created.", Audit.TYPE_INFO);
	}
	
	public void logDeleteInstance(Instance instance){
		this.createAudit("Instance " + instance + " deleted.", Audit.TYPE_INFO);
	}
	
	public void logStartInstance(Instance instance){
		this.createAudit("Instance " + instance + " started.", Audit.TYPE_INFO);
	}
	
	public void logStopInstance(Instance instance){
		this.createAudit("Instance " + instance + " stopped.", Audit.TYPE_INFO);
	}
}