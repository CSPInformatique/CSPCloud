package com.cspinformatique.cspCloud.server.service.impl;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.commons.apache.entity.ApacheServer;
import com.cspinformatique.commons.apache.entity.VirtualHost;
import com.cspinformatique.commons.apache.service.ApacheServerService;
import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;
import com.cspinformatique.cspCloud.server.command.CommandExecutor;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.repository.CSPCloudServerRepository;
import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.CSPCloudServerService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;

@Component
@Transactional
public class CSPCloudServerServiceImpl implements CSPCloudServerService {
	private static final int PORT = 2080;
	
	static final Logger logger = Logger.getLogger(CSPCloudServerServiceImpl.class);
	
	@Autowired
	private ApacheServerService apacheServerService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	@Autowired
	private ServletContext context;
	
	private CSPCloudServer cspCloudServer;
	
	@Autowired
	private CSPCloudServerRepository cspCloudServerRepository;
	
	@Autowired
	private LoadBalancerServerService loadBalancerServerService;
	
	@Autowired
	private Params params;
	
	@Autowired
	private CSPCRestTemplate restTemplate;
	
	private void callConfigureApacheForLoadBalancers(ApacheServer apacheServer){
		this.restTemplate.exchange(
			"http://" + cspCloudServer.getHostname() + ":" + 
				cspCloudServer.getPort() +
				cspCloudServer.getAdminContext() +
				"/DACloudServer?configure", 
			HttpMethod.POST, 
			new HttpEntity<ApacheServer>( 
				apacheServer,
				RestUtil.createBasicAuthHeader(
					this.params.getSystemUser(), 
					this.params.getSystemPassword()
				) 
			),
			ApacheServer.class
		);
	}
	
	private void callRestartApache(CSPCloudServer daCloudServer){
		this.restTemplate.exchange(
			"http://" + daCloudServer.getHostname() + ":" + 
				daCloudServer.getPort() +
				daCloudServer.getAdminContext() +
				"/DACloudServer?restart", 
			HttpMethod.POST, 
			new HttpEntity<CSPCloudServer>( 
					daCloudServer,
				RestUtil.createBasicAuthHeader(
					this.params.getSystemUser(), 
					this.params.getSystemPassword()
				) 
			),
			CSPCloudServer.class
		);
	}
	
	@Override
	public void configureApache(ApacheServer apacheServer){		
		this.apacheServerService.configureServer(apacheServer);
	}
	
	public void configureApacheForLoadBalancers(){
		// Appel de la methode de configuration du load balancer.
		for(CSPCloudServer daCloudServer : this.getServers()){
			this.callConfigureApacheForLoadBalancers(this.getApacheServer(daCloudServer));
			
			this.callRestartApache(daCloudServer);
		}
	}
	
	private ApacheServer getApacheServer(CSPCloudServer daCloudServer){
		return new ApacheServer(
			"dlavoie@cspinformatique.com",
			PORT,
			"apache.dacloud.com",
			this.params.getApacheRoot(),
			this.getVirtualHosts(daCloudServer)
		);
	}
	
	@Override
	public int getNewPort(CSPCloudServer cspCloudServer){
		int port = 10000;
		
		boolean portFound = false;
		while(!portFound){
			if(this.commandExecutor.checkPortAvailability(cspCloudServer.getHostname(), port)){
				portFound = true;
			}
			
			if(!portFound){
				++port;
			}
		}
		
		return port;
	}
	
	private CSPCloudServer getServer(){
		try{
			if(this.cspCloudServer == null){
				InetAddress address = InetAddress.getLocalHost();
				
				this.cspCloudServer =	new CSPCloudServer(
											this.context.getContextPath(), 
											0,
											address.getHostAddress(), 
											address.getHostName(), 
											this.params.getPort()
										);
			}
			return this.cspCloudServer;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	@Override
	public List<CSPCloudServer> getServers(){
		return this.cspCloudServerRepository.getServers();
	}
	
	private List<VirtualHost> getVirtualHosts(CSPCloudServer cspCloudServer){
		List<VirtualHost> virtualHosts = new ArrayList<VirtualHost>();

		for(Application application : this.applicationService.getApplications()){
			List<String> loadBalancers = new ArrayList<String>();
			for(LoadBalancerServer loadBalancerServer : 
					this.loadBalancerServerService.getApplicationLoadBalancerServers(application)
			){
				loadBalancers.add(
					"http://" + 
						loadBalancerServer.getCSPCloudServer().getHostname()+ ":" + 
						loadBalancerServer.getPort()
				);
			}
			
			virtualHosts.add(
				new VirtualHost(
					"",
					VirtualHost.TYPE_HTTP, 
					loadBalancers, 
					application.getName() + "." + this.params.getServiceDomain()
				)
			);
		}
		
		return virtualHosts;
	}
	
	@Override
	public void registerServer() {
		boolean alreadyRegistered = false;
		for(CSPCloudServer server : this.cspCloudServerRepository.getServers()){
			if(	server.getHostname().equals(this.getServer().getHostname()) &&
				server.getPort() == this.getServer().getPort()
			){
				alreadyRegistered = true;
			}
		}
		
		if(!alreadyRegistered){
			this.cspCloudServerRepository.registerServer(this.getServer());
		}
	}
	
	@Override
	public void restartApache(CSPCloudServer daCloudServer){
		this.apacheServerService.restartApache(this.getApacheServer(daCloudServer));
	}
	
	@Override
	/**
	 * Used to delete every reference o
	 */
	public void unregisterServer(CSPCloudServer daCloudServer){
		logger.info("Unregistering DACloudServer " + daCloudServer);
		
		// Deleting every Load Balancer Servers.
		for(	LoadBalancerServer loadBalancerServer : 
					this.loadBalancerServerService.getLoadBalancerServers(daCloudServer.getId())
		){
			this.loadBalancerServerService.unregisterLoadBalancerServer(loadBalancerServer);
		}
		
		this.cspCloudServerRepository.unregisterServer(daCloudServer.getId());
		
		logger.info("DACloudServer " + daCloudServer + " unregistered.");
	}
}
