package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;

public interface LoadBalancerServerRepository {
	public void addInstanceToLoadBalancerServer(LoadBalancerServer loadBalancerServer, Instance instance);
	
	public void createLoadBalancerServer(LoadBalancerServer loadBalancerServer);
	
	public void deleteLoadBalancerServer(int daCloudServerId, int loadBalancerId);
	
	public List<LoadBalancerServer> getApplicationLoadBalancerServers(Application application);
	
	public LoadBalancerServer getLoadBalancerServer(int loadBalancerId, int daCloudServerId);
	
	public List<LoadBalancerServer> getLoadBalancerServers();
	
	public List<LoadBalancerServer> getLoadBalancerServers(final int daCloudServerId);
	
	public List<LoadBalancerServer> getMissingLoadBalancerServer();
	
	public List<Instance> getMissingLoadBalancerServerInstances(LoadBalancerServer loadBalancerServer);
	
	public void removeInstanceFromLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	);
}
