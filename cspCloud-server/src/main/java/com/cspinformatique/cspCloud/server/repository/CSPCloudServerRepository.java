package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;

public interface CSPCloudServerRepository {
	
	public CSPCloudServer getServer(int id);
	
	public List<CSPCloudServer> getServers();
	
	public void registerServer(CSPCloudServer cspCloudServer);
	
	public void unregisterServer(int serverId);
}
