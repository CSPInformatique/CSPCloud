package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Server;

public interface ServerService {
	public void activateServer(Server server);
	
	public void deleteServer(Server server);
	
	public Server findBestServerAvailable();
	
	public Server loadServer(int serverId);
	
	public List<Server> loadServers();
	
	public void loadServerStats(Server server);
	
	public void registerServer(Server server);
}
