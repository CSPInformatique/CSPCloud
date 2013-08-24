package com.cspinformatique.cspCloud.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;
import com.cspinformatique.cspCloud.commons.factory.ServerPortFactory;
import com.cspinformatique.cspCloud.server.command.CommandExecutor;
import com.cspinformatique.cspCloud.server.repository.ServerPortRepository;
import com.cspinformatique.cspCloud.server.service.ServerPortService;

@Component
@Transactional
public class ServerPortServiceImpl implements ServerPortService {
	static final Logger logger = Logger.getLogger(ServerPortServiceImpl.class);
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	@Autowired
	private ServerPortRepository serverPortRepository;
	
	@Override
	public void deleteInstancePorts(Instance instance){
		for(ServerPort serverPort : instance.getServerPorts().values()){
			this.serverPortRepository.deletePort(instance.getServer().getId(), serverPort.getPort());
		}
	}
	
	@Override
	public int generateNewPort(Instance instance, String portType){
		int port = 20000;
		List<ServerPort> ports =	this.loadServerPorts(
											instance.getServer().getId()
										);
		
		boolean portFound = false;
		while(!portFound){
			if(!this.containsPort(ports, port)){
				if(this.isPortAvailable(instance.getServer(), port)){
					portFound = true;
				}
			}
			
			if(!portFound){
				++port;
			}
		}
		
		
		ServerPort serverPort = ServerPortFactory.getServerPor(port, portType);
		
		logger.debug("Creating serverport " + serverPort + ".");
		
		this.serverPortRepository.save(serverPort, instance);
		instance.getServerPorts().put(port, serverPort);
		
		logger.debug("Serverport " + serverPort + " created.");
		
		return port;
	}
	
	@Override
	public boolean isPortAvailable(Server server, int port){
		return this.commandExecutor.checkPortAvailability(server.getHostname(), port);
	}
	
	@Override
	public List<ServerPort> loadServerPorts(int serverId){
		return this.serverPortRepository.loadServerPorts(serverId);
	}
	
	private boolean containsPort(List<ServerPort> ports, int port){
		for(ServerPort serverPort : ports){
			if(serverPort.getPort() == port){
				return true;
			}
		}
		
		return false;
	}
}
