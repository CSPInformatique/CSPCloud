package com.cspinformatique.cspCloud.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancer;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;
import com.cspinformatique.cspCloud.server.repository.LoadBalancerRepository;
import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerService;

@Component
@Transactional
public class LoadBalancerServiceImpl implements LoadBalancerService {	
	
	@Autowired
	private LoadBalancerServerService loadBalancerServerService;
	
	@Autowired
	private LoadBalancerRepository loadBalancerRepository;
	
	@Override
	public void addInstanceToLoadBalancer(LoadBalancer loadBalancer, Instance instance){
		// Adding the instance to the load balancer.
		this.loadBalancerRepository.addInstanceToLoadBalancer(loadBalancer, instance);
		
		// Updating the configuration of every load balancers.
		for(LoadBalancerServer loadBalancerServer : 
				this.loadBalancerServerService.getApplicationLoadBalancerServers(
					loadBalancer.getApplication()
				)
		){
			// Calling the server to add the instance to the web server.
			this.loadBalancerServerService.callAddInstanceToLoadBalancerServer(
				loadBalancerServer, 
				instance
			);
		}
	}
	
	@Override
	public void createLoadBalancer(LoadBalancer loadBalancer) {
		// Creating the load balancer.
		this.loadBalancerRepository.createLoadBalancer(loadBalancer);
	}
	
	@Override
	public void deleteLoadBalancedInstance(Instance instance){
		// Delete all the load balancer servers instances.
		for(LoadBalancerServer loadBalancerServer : 
				this.loadBalancerServerService.getApplicationLoadBalancerServers(
					instance.getApplication()
				)
		){
			this.loadBalancerServerService.removeInstanceFromLoadBalancerServer(
				loadBalancerServer, 
				instance
			);
		}
		
		this.loadBalancerRepository.deleteLoadBalancedInstance(instance);
	}
	
	@Override
	public LoadBalancer getApplicationLoadBalancer(Application application){
		return this.loadBalancerRepository.getApplicationLoadBalancer(application);
	}
	
	@Override
	public List<LoadBalancer> getLoadBalancers(){
		return this.loadBalancerRepository.getLoadBalancers();
	}
}
