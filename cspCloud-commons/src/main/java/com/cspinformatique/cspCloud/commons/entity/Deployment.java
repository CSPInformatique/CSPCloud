package com.cspinformatique.cspCloud.commons.entity;

import java.util.Date;

public class Deployment {
	private Application application;
	private Date date;
	private int id;
	
	private String packageUrl;
	private String contextUrl;
	
	public Deployment(){
		
	}
	
	public Deployment(int id, Application application, String packageUrl, String contextUrl, Date date){
		this.application = application;
		this.date = date;
		this.id = id;
		this.packageUrl = packageUrl;
		this.contextUrl = contextUrl;
	}
	
	public Application getApplication(){
		return this.application;
	}
	
	public Date getDate(){
		return this.date;
	}
	
	public int getId(){
		return id;
	}
	
	public String getPackageUrl() {
		return packageUrl;
	}
	
	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
	}
	
	public String getContextUrl() {
		return contextUrl;
	}
	
	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}
}
