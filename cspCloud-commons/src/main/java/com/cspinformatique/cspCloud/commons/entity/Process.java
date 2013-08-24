package com.cspinformatique.cspCloud.commons.entity;

public class Process {
	private long id;
	private Server server;
	
	public Process(){
		
	}
	
	public Process(long id, Server server){
		this.id = id;
		this.server = server;
	}
	
	public long getId() {
		return id;
	}
	public Server getServer() {
		return server;
	}
}
