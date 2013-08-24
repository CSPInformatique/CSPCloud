package com.cspinformatique.cspCloud.agent.service.impl;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.commons.util.FileUtil;
import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.cspCloud.agent.params.Params;
import com.cspinformatique.cspCloud.agent.service.ProcessService;
import com.cspinformatique.cspCloud.agent.service.ServerService;
import com.cspinformatique.cspCloud.agent.service.ServerStatusService;
import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerStats;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;

@Component
@Transactional
public class ServerServiceImpl implements ServerService {
	@Autowired
	private ServletContext context;
	
	@Autowired
	private Params params;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	protected CSPCRestTemplate restTemplate;
	
	private Server server;
	
	@Autowired
	private ServerStatusService serverStatusService;
	
	public void activateServer(){
		this.restTemplate.exchange( 
			this.params.getDaCloudWebUrl() + "/servers?activate", 
			HttpMethod.POST, 
			new HttpEntity<Server>( 
				this.getServer(), 
				RestUtil.createBasicAuthHeader(this.params.getUsername(), this.params.getPassword()) 
			), 
			Server.class 
		);
	}
	
	private Server getServer(){
		try{
			if(this.server == null){
				InetAddress address = InetAddress.getLocalHost();
				
				this.server =	new Server(
									this.context.getContextPath(), 
									address.getHostName(), 
									0, 
									address.getHostAddress(), 
									new ArrayList<Long>(),
									this.params.getServerPort(), 
									null, 
									Server.STATUS_INACTIVE
								);
			}
			return this.server;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public ServerStats getServerStats(){
		ServerStats serverStats =	new ServerStats(
										0,
										this.serverStatusService.getAverageCpuUsage(),
										this.serverStatusService.getAverageFreeMemory(),
										this.serverStatusService.getAverageFreePercentMemory(),
										this.serverStatusService.getTotalMemory()
									);
		
		return serverStats;
	}
	
	public void registerServer(){
		// Registering the server.
		this.server =	(Server)this.restTemplate.exchange( 
							this.params.getDaCloudWebUrl() + "/servers", 
							HttpMethod.POST, 
							new HttpEntity<Server>( 
								this.getServer(), 
								RestUtil.createBasicAuthHeader(this.params.getUsername(), this.params.getPassword()) 
							), 
							Server.class 
						).getBody();
		
		// Killing every instances which is still running.
		for(long processId : this.getServer().getProcesses()){
			this.processService.deleteProcess(new Process(processId, this.server));
		}
		
		// Deleting every instances.
		File applicationFolder = new File(this.params.getApplicationFolder());  
		FileUtil.deleteDir(applicationFolder);
		applicationFolder.mkdirs();
		
		// Activating the server.
		this.activateServer();
	}
}
