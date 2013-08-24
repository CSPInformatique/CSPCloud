package com.cspinformatique.cspCloud.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerStats;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.repository.ServerRepository;
import com.cspinformatique.cspCloud.server.service.InstanceService;
import com.cspinformatique.cspCloud.server.service.ProcessService;
import com.cspinformatique.cspCloud.server.service.ServerService;

@Component
@Transactional
public class ServerServiceImpl implements ServerService{
	static final Logger logger = Logger.getLogger(ServerServiceImpl.class);
	
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private CSPCRestTemplate restTemplate;
	
	@Autowired
	private ServerRepository serverRepository;
	
	@Autowired
	private Params params;
	
	@Override
	public void activateServer(Server server){
		this.serverRepository.activateServer(server);
	}
	
	@Override
	public void deleteServer(Server server){
		logger.info("Deleting server " + server);
		
		// Delete every instances found on the server.
		for(Instance instance : this.instanceService.getServerInstances(server)){
			this.instanceService.deleteInstance(instance);
		}
		
		// Delete the server's reference.
		this.serverRepository.deleteServer(server);
		
		logger.info("Server " + server + " deleted.");
	}
	
	@Override
	public Server findBestServerAvailable(){
		List<Server> servers = this.loadServers(); 
		
		// Chargement des infos de chaque server.
		Server bestServer = null;
		int lowestCpu = 0;
		boolean firstServerFound = false;
		for(Server server : servers){
			if(server.getStatus().equals(Server.STATUS_ACTIVE)){
				if(!firstServerFound || lowestCpu > server.getStats().getAverageCpuUsage()){
					bestServer = server;
				}
			}
		}
		
		return bestServer;
	}
	
	@Override
	public Server loadServer(int serverId){
		Server server = this.serverRepository.loadServer(serverId);		
		return server;
	}
	
	@Override
	public List<Server> loadServers(){
		List<Server> servers = new ArrayList<Server>();;
		
		for(Integer id : this.serverRepository.loadServers()){
			servers.add(this.loadServer(id));
		}
		
		return servers;
	}
	
	@Override
	public void loadServerStats(Server server){
		ServerStats serverStats =	(ServerStats) this.restTemplate.exchange( 
										server.getServerUrl() + "/server", 
										HttpMethod.GET, 
										new HttpEntity<ServerStats>( 
											new ServerStats(),
											RestUtil.createBasicAuthHeader(
												this.params.getSystemUser(), 
												this.params.getSystemPassword()
											) 
										), 
										ServerStats.class 
									).getBody();
		
		server.setStats(serverStats);
	}
	
	@Override
	public void registerServer(Server server){
		boolean alreadyRegistered = false;
		for(Server srv : this.loadServers()){
			if(srv.getServerUrl().equals(server.getServerUrl())){
				alreadyRegistered = true;
				server = srv;
			}
		}
		
		if(!alreadyRegistered){
			// Searching for old processes.
			server.setProcesses(this.processService.getProcesses(server));
			
			this.serverRepository.createServer(server);
			
			for(Server srv : this.loadServers()){
				if(srv.getServerUrl().equals(server.getServerUrl())){
					server.setId(srv.getId());
				}
			}
		}
	}
}
