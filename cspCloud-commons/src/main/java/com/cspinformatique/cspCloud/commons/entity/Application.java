package com.cspinformatique.cspCloud.commons.entity;

import java.io.Serializable;

public class Application implements Serializable{
	private static final long serialVersionUID = -3608438771559031111L;
	
	public static final String STATUS_RUNNING = "Running";
	public static final String STATUS_SLEEPING = "Sleeping";
	public static final String STATUS_STOPPED = "Stopped";
	public static final String STATUS_UNDEPLOYED = "Undeployed";
	
	private Account account;
	private ApplicationKit applicationKit;
	private boolean clustered;
	private int id;
	private String name;
	private int numberOfInstances;
	private String status;
	
	
	public Application(){
		
	}
	
	public Application(
		int id, 
		String name, 
		Account account, 
		ApplicationKit applicationKit, 
		int numberOfInstances, 
		boolean clustered,
		String status
	){
		this.id = id;
		this.name = name;
		this.account = account;
		this.applicationKit = applicationKit;
		this.numberOfInstances = numberOfInstances;
		this.clustered = clustered;
		this.status = status;
	}

	public Account getAccount() {
		return account;
	}
	
	public ApplicationKit getApplicationKit(){
		return this.applicationKit;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumberOfInstances() {
		return numberOfInstances;
	}
	
	public String getStatus() {
		return status;
	}
	
	public boolean isClustered() {
		return clustered;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public void setApplicationKit(ApplicationKit applicationKit){
		this.applicationKit = applicationKit;
	}

	public void setClustered(boolean clustered) {
		this.clustered = clustered;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String toString(){
		return this.getId() + "-" + this.getName();
	}
}