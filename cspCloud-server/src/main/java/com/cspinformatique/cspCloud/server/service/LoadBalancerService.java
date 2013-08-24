package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancer;

public interface LoadBalancerService {
	public void addInstanceToLoadBalancer(LoadBalancer loadBalancer, Instance instance);
	
	public void createLoadBalancer(LoadBalancer loadBalancer);
	
	public void deleteLoadBalancedInstance(Instance instance);
	
	public LoadBalancer getApplicationLoadBalancer(Application application);
	
	public List<LoadBalancer> getLoadBalancers();
}
