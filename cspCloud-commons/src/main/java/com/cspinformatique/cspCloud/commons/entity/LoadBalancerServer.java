package com.cspinformatique.cspCloud.commons.entity;

public class LoadBalancerServer {
	private CSPCloudServer cspCloudServer;
	private LoadBalancer loadBalancer;
	private int port;
	
	public LoadBalancerServer(){
		
	}
	
	public LoadBalancerServer(
		CSPCloudServer cspCloudServer, 
		LoadBalancer loadBalancer, 
		int port
	){
		this.cspCloudServer = cspCloudServer;
		this.loadBalancer = loadBalancer;
		this.port = port;
	}

	public CSPCloudServer getCSPCloudServer() {
		return cspCloudServer;
	}

	public LoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public int getPort() {
		return port;
	}
	
	public String toString(){
		return this.getLoadBalancer().getApplication() + "-" + 
			this.getCSPCloudServer().getHostname() + ":" + 
			this.getPort();
	}
}
