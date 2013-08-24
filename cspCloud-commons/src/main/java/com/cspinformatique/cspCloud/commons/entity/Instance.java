package com.cspinformatique.cspCloud.commons.entity;

import java.util.Map;

public class Instance {
	public static final String STATUS_RUNNING = "running";
	public static final String STATUS_SLEEPING = "sleeping";
	public static final String STATUS_STOPPED = "stopped";
	
	private Application application;

	private Deployment lastDeployment;
	
	private Server server;
	
	private Map<Integer, ServerPort> serverPorts;
	
	private String status;
	
	public Instance(){
		
	}
	
	public Instance(
		Application application, 
		Server server, 
		Deployment lastDeployment, 
		Map<Integer, ServerPort> serverPorts, 
		String status
	){
		this.application = application;
		this.server = server;
		this.lastDeployment = lastDeployment;
		this.serverPorts = serverPorts;
		this.status = status;
	}
	
	public Application getApplication(){
		return this.application;
	}
	
	public Deployment getLastDeployment() {
		return lastDeployment;
	}
	
	public Server getServer(){
		return this.server;
	}
	
	public ServerPort getServerPort(String type) {
		for(ServerPort serverPort : serverPorts.values()){
			if(serverPort.getType().equals(type)){
				return serverPort;
			}
		}
		return null;
	}

	public Map<Integer, ServerPort> getServerPorts() {
		return serverPorts;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setApplication(Application application) {
		this.application = application;
	}

	public void setLastDeployment(Deployment lastDeployment) {
		this.lastDeployment = lastDeployment;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setServerPorts(Map<Integer, ServerPort> serverPorts) {
		this.serverPorts = serverPorts;
	}

	public String getStatus() {
		return status;
	}
	
	public String toString(){
		return this.getApplication() + "-" + this.getServer();
	}
}
