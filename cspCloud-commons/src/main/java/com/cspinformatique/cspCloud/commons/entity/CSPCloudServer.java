package com.cspinformatique.cspCloud.commons.entity;

public class CSPCloudServer {
	private String adminContext;
	private int id;
	private String ip;
	private String hostname;
	private int port;
	
	public CSPCloudServer(){
		
	}
	
	public CSPCloudServer(String adminContext, int id, String ip, String hostname, int port){
		this.adminContext = adminContext;
		this.id = id;
		this.ip = ip;
		this.hostname = hostname;
		this.port = port;
	}

	public String getAdminContext() {
		return adminContext;
	}
	
	public int getId() {
		return id;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public int getPort() {
		return port;
	}

	public void setAdminContext(String adminContext) {
		this.adminContext = adminContext;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public String toString(){
		return this.getHostname() + ":" + this.getPort();
	}
}