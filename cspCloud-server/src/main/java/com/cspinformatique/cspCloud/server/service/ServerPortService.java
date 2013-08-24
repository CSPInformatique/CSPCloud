package com.cspinformatique.cspCloud.server.service;

import java.io.IOException;
import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;

public interface ServerPortService {
	public void deleteInstancePorts(Instance instance);
	
	public int generateNewPort(Instance instance, String portType);
	
	public boolean isPortAvailable(Server server, int port) throws IOException;
	
	public List<ServerPort> loadServerPorts(int serverId);
}
