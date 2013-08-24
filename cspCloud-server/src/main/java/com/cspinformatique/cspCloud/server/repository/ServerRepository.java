package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Server;

public interface ServerRepository {
	public void activateServer(Server server);
	
	public void createServer(Server server);
	
	public void deleteServer(Server server);
	
	public Server loadServer(int serverId);
	
	public List<Integer> loadServers();
}