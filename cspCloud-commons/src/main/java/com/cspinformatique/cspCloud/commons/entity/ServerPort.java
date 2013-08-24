package com.cspinformatique.cspCloud.commons.entity;

public class ServerPort {
	public static final String TYPE_AJP = "ajp";
	public static final String TYPE_HTTP = "http";
	public static final String TYPE_HTTPS = "https";
	public static final String TYPE_SHUTDOWN = "shutdown";
	
	private int port;
	private String type;
	
	public ServerPort(){
		
	}
	
	public ServerPort(int port, String type){
		this.port = port;
		this.type = type;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString(){
		return this.getPort() + "-" + this.getType();
	}
}
