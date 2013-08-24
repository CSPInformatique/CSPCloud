package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;

public interface LoadBalancerServerService {
	public void callAddInstanceToLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	);
	
	public void callRestartLoadBalancerServer(LoadBalancerServer loadBalancerServer);
	
	public void createLoadBalancerServer(LoadBalancerServer loadBalancerServer);
	
	public void configureLoadBalancerServer(LoadBalancerServer loadBalancerServer);
	
	public void controlMissingLoadBalancerServers();
	
	public void controlUnusedLoadBalancerServers();
	
	public void deployLoadBalancerServer(LoadBalancerServer loadBalancerServer);
	
	public List<LoadBalancerServer> getApplicationLoadBalancerServers(Application application);
	
	public LoadBalancerServer getLoadBalancerServer(int loadBalancerId, int daCloudServerId);
	
	public List<LoadBalancerServer> getLoadBalancerServers(int daCloudServerId);
	
	public int getNewPortFromServer(CSPCloudServer daCloudServer);
	
	public void removeInstanceFromLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	);
	
	public void restartLoadBalancerServer(LoadBalancerServer loadBalancerServer);
	
	public void unregisterLoadBalancerServer(LoadBalancerServer loadBalancerServer);
}
