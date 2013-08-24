package com.cspinformatique.cspCloud.server.service;

import com.cspinformatique.cspCloud.commons.entity.ServerStats;

public interface ServerService {
	public void activateServer();
	
	public ServerStats getServerStats();
	
	public void registerServer();
}
