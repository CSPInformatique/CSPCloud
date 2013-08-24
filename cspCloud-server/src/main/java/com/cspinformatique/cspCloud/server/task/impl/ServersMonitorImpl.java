package com.cspinformatique.cspCloud.server.task.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.server.service.ServerService;
import com.cspinformatique.cspCloud.server.task.ServersMonitor;

@Component
public class ServersMonitorImpl implements ServersMonitor {
	static final Logger logger = Logger.getLogger(ServersMonitorImpl.class);
	
	@Autowired
	private ServerService serverService;
	
	private boolean started;
	
	private List<Server> servers;
	
	public Server getServer(int serverId){
		for(Server server : this.servers){
			if(server.getId() == serverId){
				return server;
			}
		}
		
		return null;
	}
	
	public List<Server> getServers(){
		return this.servers;
	}
	
	public boolean isStarted(){
		return started;
	}
	
	@Scheduled(fixedDelay=6000)
	public void monitorServers(){
		// Call the current server to retreive the server stats.
		// If an error occurs during this process, we consider that the server is unavailable. We delete
		// the server reference and every instances references.
		
		// Loading servers.
		this.servers = serverService.loadServers();
		for(Server server : servers){
			try{
				serverService.loadServerStats(server);
			}catch(ResourceAccessException connectEx){
				// The server is not available. We must delete all it's instances and remove it's 
				// reference from the database.
				this.serverService.deleteServer(server);
			}
		}
		
		this.started = true;
	}
}
