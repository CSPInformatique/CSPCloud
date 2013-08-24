package com.cspinformatique.cspCloud.agent.service;

import com.cspinformatique.cspCloud.commons.entity.ServerStats;

public interface ServerService {
	public void activateServer();
	
	public ServerStats getServerStats();
	
	public void registerServer();
}
