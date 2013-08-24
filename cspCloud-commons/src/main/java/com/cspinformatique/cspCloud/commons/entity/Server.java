package com.cspinformatique.cspCloud.commons.entity;

import java.io.Serializable;
import java.util.List;

public class Server implements Serializable {
	private static final long serialVersionUID = -5970048717777930186L;
	
	public static final String STATUS_ACTIVE = "active";
	public static final String STATUS_INACTIVE = "inactive";
	
	private String adminContext;
	private String hostname;
	private int id;
	private String ipAddress;	
	private List<Long> processes;
	private int serverPort;
	private String serverUrl;
	private String status;
	private ServerStats stats;
	
	public Server(){
		
	}
	
	public Server(
		String adminContext, 
		String hostname, 
		int id, 
		String ipAddress, 
		List<Long> processes, 
		int serverPort,
		String serverUrl, 
		String status
	){
		this.adminContext = adminContext;
		this.hostname = hostname;
		this.id = id;
		this.ipAddress = ipAddress;
		this.processes = processes;
		this.serverPort = serverPort;
		this.serverUrl = serverUrl;
		this.status = status;
	}
	
	public String getAdminContext() {
		return adminContext;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public int getId() {
		return id;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public List<Long> getProcesses() {
		return processes;
	}
	
	public ServerStats getStats() {
		return stats;
	}
	
	public String getServerUrl(){
		if(serverUrl == null){
			serverUrl =	"http://" + 
							this.getHostname() + 
							":" + this.getServerPort() + 
							this.getAdminContext();
		}
		
		return serverUrl;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setAdminContext(String adminContext) {
		this.adminContext = adminContext;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setProcesses(List<Long> processes) {
		this.processes = processes;
	}

	public void setStats(ServerStats stats) {
		this.stats = stats;
	}	

	public void setServerUrl(String serverUrl){
		this.serverUrl = serverUrl;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString(){
		return this.getHostname() + ":" + this.getServerPort();
	}
}
