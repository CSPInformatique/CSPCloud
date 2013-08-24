package com.cspinformatique.cspCloud.server.task;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Server;

public interface ServersMonitor {
	public Server getServer(int serverId);
	
	public List<Server> getServers();
	
	public boolean isStarted();
	
	public void monitorServers();
}
