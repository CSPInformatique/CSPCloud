package com.cspinformatique.cspCloud.server.task;

public interface LoadBalancersMonitor {
	public void monitorMissingLoadBalancers();
	
	public void monitorUnsuedLoadBalancers();
}
