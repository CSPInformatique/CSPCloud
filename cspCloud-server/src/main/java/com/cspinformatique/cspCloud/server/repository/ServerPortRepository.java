package com.cspinformatique.cspCloud.server.repository;

import java.util.List;
import java.util.Map;

import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;

public interface ServerPortRepository {
	public void deletePort(int serverId, int port);
	
	public Map<Integer, ServerPort> loadInstancePorts(int serverId, int appId);
	
	public List<ServerPort> loadServerPorts(int serverId);
	
	public void save(ServerPort serverPort, Instance instance);
}
