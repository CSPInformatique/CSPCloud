package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.commons.apache.entity.ApacheServer;
import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;

public interface CSPCloudServerService {
	public int getNewPort(CSPCloudServer daCloudServer);
	
	public void configureApache(ApacheServer apacheServer);
	
	public void configureApacheForLoadBalancers();
	
	public List<CSPCloudServer> getServers();
	
	public void registerServer();
	
	public void restartApache(CSPCloudServer daCloudServer);
	
	public void unregisterServer(CSPCloudServer daCloudServer);
}
