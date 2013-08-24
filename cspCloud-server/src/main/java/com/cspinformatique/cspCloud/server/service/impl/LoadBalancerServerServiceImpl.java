package com.cspinformatique.cspCloud.server.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.cspinformatique.commons.apache.entity.ApacheServer;
import com.cspinformatique.commons.apache.entity.VirtualHost;
import com.cspinformatique.commons.apache.service.ApacheServerService;
import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.commons.util.SigarUtil;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;
import com.cspinformatique.cspCloud.server.command.CommandExecutor;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.repository.LoadBalancerServerRepository;
import com.cspinformatique.cspCloud.server.service.CSPCloudServerService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;

@Component
@Transactional
public class LoadBalancerServerServiceImpl implements LoadBalancerServerService {
	static final Logger logger = Logger.getLogger(LoadBalancerServerServiceImpl.class);
	
	@Autowired
	private ApacheServerService apacheServerService;
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	@Autowired
	private CSPCloudServerService CSPCloudServerService;
	
	@Autowired
	private LoadBalancerServerRepository loadBalancerServerRepository;
	
	@Autowired
	private Params params;
	
	@Autowired
	private CSPCRestTemplate restTemplate;
	
	@Autowired
	private SigarUtil sigarUtil;
	
	@PostConstruct
	public void init(){
		// Killing all the running load balancers.
		for(Long processId : this.sigarUtil.getPIds(this.params.getLoadBalancerInstallFolder())){
			this.commandExecutor.killProcess(processId);
			
			// Deleting all the instances.
			File[] loadBalancersInstanceFolder =	new File(
														this.params.getLoadBalancerInstallFolder()
													).listFiles();
			
			for(File file : loadBalancersInstanceFolder){
				file.delete();
			}
		}
	}
	

	
	@Override
	public void callAddInstanceToLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	){
		logger.info(
			"Adding instance " + instance + " to load balancer server" + loadBalancerServer + "."
		);
		
		// Persisting the new configuration to the database.
		this.loadBalancerServerRepository.addInstanceToLoadBalancerServer(loadBalancerServer, instance);
		
		// Reloading the loadBalancerServer new infos.
		loadBalancerServer =	this.loadBalancerServerRepository.getLoadBalancerServer(
									loadBalancerServer.getLoadBalancer().getId(), 
									loadBalancerServer.getCSPCloudServer().getId()
								);
		
		// Calling the LoadBalancerServer for a physical reconfiguration.
		this.callConfigureLoadBalancerServer(loadBalancerServer);
		
		logger.info("Instance " + instance + " added to load balancer server" + loadBalancerServer + ".");
	}
	
	private void callConfigureLoadBalancerServer(
		LoadBalancerServer loadBalancerServer
	){
		this.restTemplate.exchange(
			"http://" + loadBalancerServer.getCSPCloudServer().getHostname() + ":" + 
				loadBalancerServer.getCSPCloudServer().getPort() +
				loadBalancerServer.getCSPCloudServer().getAdminContext() +
				"/LoadBalancerServer?configure", 
			HttpMethod.POST, 
			new HttpEntity<LoadBalancerServer>( 
				loadBalancerServer,
				RestUtil.createBasicAuthHeader(
					this.params.getSystemUser(), 
					this.params.getSystemPassword()
				) 
			),
			LoadBalancerServer.class
		);
		
		this.callRestartLoadBalancerServer(loadBalancerServer);
	}
	
	@Override
	public void callRestartLoadBalancerServer(LoadBalancerServer loadBalancerServer){
		logger.info("Restarting load balancer server " + loadBalancerServer + ".");
		
		this.restTemplate.exchange(
			"http://" + loadBalancerServer.getCSPCloudServer().getHostname() + ":" + 
					loadBalancerServer.getCSPCloudServer().getPort() +
					loadBalancerServer.getCSPCloudServer().getAdminContext() +
				"/LoadBalancerServer?restart", 
			HttpMethod.POST, 
			new HttpEntity<LoadBalancerServer>( 
				loadBalancerServer,
				RestUtil.createBasicAuthHeader(
					this.params.getSystemUser(), 
					this.params.getSystemPassword()
				) 
			),
			LoadBalancerServer.class
		);
		
		logger.info("Load balancer server " + loadBalancerServer + " restarted.");
	}
	
	@Override
	/**
	 * Persist the load balancer server in the database and call the DACloudWeb server
	 * for physical deployment (LoadBalancerServer.deployLoadBalancerServer).
	 */
	public void createLoadBalancerServer(LoadBalancerServer loadBalancerServer) {
		logger.info("Deploying the new load balancer server " + loadBalancerServer);
		
		// Creating the load balancer on the specified server.
		this.loadBalancerServerRepository.createLoadBalancerServer(loadBalancerServer);
		
		try{
			// Create the physical load balancer.
			this.restTemplate.exchange(
				"http://" + loadBalancerServer.getCSPCloudServer().getHostname() + ":" + 
				loadBalancerServer.getCSPCloudServer().getPort() +
				loadBalancerServer.getCSPCloudServer().getAdminContext() +
				"/LoadBalancerServer?create", 
				HttpMethod.POST, 
				new HttpEntity<LoadBalancerServer>( 
					loadBalancerServer,
					RestUtil.createBasicAuthHeader(
						this.params.getSystemUser(), 
						this.params.getSystemPassword()
					) 
				),
				LoadBalancerServer.class
			);
		}catch(ResourceAccessException resourceAccessEx){
			throw new RuntimeException(resourceAccessEx);
		}
		
		// Configure every instances for this load balancer server.
		for(Instance instance : loadBalancerServer.getLoadBalancer().getInstances()){
			this.callAddInstanceToLoadBalancerServer(loadBalancerServer, instance);
		}
		
		// Every Apaches must be reconfigured for this new load balancer server.
		this.CSPCloudServerService.configureApacheForLoadBalancers();
		
		logger.info("Load balancer server " + loadBalancerServer + " deployed.");
	}
	
	@Override
	public void configureLoadBalancerServer(LoadBalancerServer loadBalancerServer){		
		this.apacheServerService.configureServer(this.getApacheServer(loadBalancerServer));
	}
	
	@Override
	public void controlMissingLoadBalancerServers(){
		// Deploy missing load balancer servers.
		for(LoadBalancerServer server : this.loadBalancerServerRepository.getMissingLoadBalancerServer()){
			this.createLoadBalancerServer(server);
		}
		
		// Deploying any missing instances into the load balancer servers.
		for(LoadBalancerServer server : this.loadBalancerServerRepository.getLoadBalancerServers()){
			for(	Instance instance : 
					this.loadBalancerServerRepository.getMissingLoadBalancerServerInstances(server)
			){
				this.callAddInstanceToLoadBalancerServer(server, instance);
			}
		}
	}
	
	@Override
	public void controlUnusedLoadBalancerServers(){
		// Looking for DACloud Web Servers that aren't running anymore.
		for(CSPCloudServer CSPCloudServer : this.CSPCloudServerService.getServers()){
			try{
				this.restTemplate.exchange( 
					"http://" + 
						CSPCloudServer.getHostname() + ":" + 
						CSPCloudServer.getPort() + 
						CSPCloudServer.getAdminContext() + 
						"/CSPCloudServer?ping", 
					HttpMethod.POST, 
					new HttpEntity<CSPCloudServer>( 
						CSPCloudServer,
						RestUtil.createBasicAuthHeader(
							this.params.getSystemUser(), 
							this.params.getSystemPassword()
						) 
					), 
					CSPCloudServer.class
				);
			}catch(ResourceAccessException ex){				
				this.CSPCloudServerService.unregisterServer(CSPCloudServer);
			}
		}
	}
	
	public void deployLoadBalancerServer(LoadBalancerServer loadBalancerServer){
		ApacheServer apacheServer = this.getApacheServer(loadBalancerServer);
		
		// Deploy the binairies.
		this.apacheServerService.deployApache(
			apacheServer, 
			params.getLoadBalancerInstallKit(), 
			params.getLoadBalancerInstallKit() + "/../pcre-8.31/"
		);
		
		// Configure the configuration file.
		this.configureLoadBalancerServer(loadBalancerServer);
	}
	
	private ApacheServer getApacheServer(LoadBalancerServer loadBalancerServer){
		return new ApacheServer(
			"dlavoie@cspinformatique.com",
			loadBalancerServer.getPort(),
			loadBalancerServer.getLoadBalancer().getApplication().getName() + "." + 
				this.params.getServiceDomain(),
			this.params.getLoadBalancerInstallFolder() + "/" + 
					loadBalancerServer.getLoadBalancer().getId() + "-" + 
					loadBalancerServer.getLoadBalancer().getApplication().getName(),
			this.getVirtualHosts(loadBalancerServer)
		);
	}
	
	@Override
	public List<LoadBalancerServer> getApplicationLoadBalancerServers(Application application){
		return this.loadBalancerServerRepository.getApplicationLoadBalancerServers(application);
	}
	
	@Override
	public LoadBalancerServer getLoadBalancerServer(int loadBalancerId, int CSPCloudServerId){
		return this.loadBalancerServerRepository.getLoadBalancerServer(loadBalancerId, CSPCloudServerId);
	}
	
	@Override
	public List<LoadBalancerServer> getLoadBalancerServers(int CSPCloudServerId){
		return this.loadBalancerServerRepository.getLoadBalancerServers(CSPCloudServerId);
	}
	
	@Override
	public int getNewPortFromServer(CSPCloudServer CSPCloudServer){
		return this.CSPCloudServerService.getNewPort(CSPCloudServer);
	}
	
	private List<VirtualHost> getVirtualHosts(LoadBalancerServer loadBalancerServer){
		List<VirtualHost> virtualHosts = new ArrayList<VirtualHost>();
		List<String> instances = new ArrayList<String>();
		
		for(Instance instance : loadBalancerServer.getLoadBalancer().getInstances()){
			instances.add(
				"ajp://" + 
					instance.getServer().getHostname() + ":" + 
					instance.getServerPort(ServerPort.TYPE_AJP).getPort() + 
					"/" + instance.getApplication().getName()
			);
		}
		
		virtualHosts.add(
			new VirtualHost(
				loadBalancerServer.getLoadBalancer().getApplication().getName(),
				VirtualHost.TYPE_AJP, 
				instances, 
				loadBalancerServer.getLoadBalancer().getApplication().getName() + "." + 
					this.params.getServiceDomain()
			)
		);
		
		return virtualHosts;
	}
	
	@Override
	public void removeInstanceFromLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	){
		logger.info("Deleting " + instance + " from " + loadBalancerServer);
		
		// Removing the physical instance.
		this.loadBalancerServerRepository.removeInstanceFromLoadBalancerServer(loadBalancerServer, instance);
		
		// Reloading the new informations of the load balancer server.
		loadBalancerServer =	this.loadBalancerServerRepository.getLoadBalancerServer(
									loadBalancerServer.getLoadBalancer().getId(), 
									loadBalancerServer.getCSPCloudServer().getId()
								);
		
		// Reconfiguring the load balancer servers.
		loadBalancerServer.getLoadBalancer().removeInstance(instance);
		this.callConfigureLoadBalancerServer(loadBalancerServer);
		
		logger.info("Instance " + instance + " deleted from " + loadBalancerServer);
	}
	
	@Override
	public void restartLoadBalancerServer(LoadBalancerServer loadBalancerServer){
		this.apacheServerService.restartApache(this.getApacheServer(loadBalancerServer));
	}
	
	@Override
	public void unregisterLoadBalancerServer(LoadBalancerServer loadBalancerServer){
		logger.info("Unregistering " + loadBalancerServer + ".");
		
		for(Instance instance : loadBalancerServer.getLoadBalancer().getInstances()){
			this.removeInstanceFromLoadBalancerServer(loadBalancerServer, instance);
		}
		
		this.loadBalancerServerRepository.deleteLoadBalancerServer(
			loadBalancerServer.getCSPCloudServer().getId(), 
			loadBalancerServer.getLoadBalancer().getId()
		);
		
		try{
			this.CSPCloudServerService.configureApacheForLoadBalancers();
		}catch(ResourceAccessException ressourceAccessEx){
			logger.info("LoadBalancerServer " + loadBalancerServer + " not available.");
		}
		logger.info("LoadBalancerServer " + loadBalancerServer + " unregistered.");
	}
}
