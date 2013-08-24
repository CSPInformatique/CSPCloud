package com.cspinformatique.cspCloud.server.task.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;
import com.cspinformatique.cspCloud.server.task.LoadBalancersMonitor;

@Component
public class LoadBalancersMonitorImpl implements LoadBalancersMonitor {
	@Autowired
	private LoadBalancerServerService loadBalancerServerService;
	
	@Override
	@Scheduled(fixedDelay=6000)
	public void monitorMissingLoadBalancers(){
		try{
			this.loadBalancerServerService.controlMissingLoadBalancerServers();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	@Scheduled(fixedDelay=6000)
	public void monitorUnsuedLoadBalancers() {
		try{
			this.loadBalancerServerService.controlUnusedLoadBalancerServers();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
