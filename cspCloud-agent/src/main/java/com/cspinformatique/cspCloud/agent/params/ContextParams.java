package com.cspinformatique.cspCloud.agent.params;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContextParams implements Params {
	@Autowired
	private ServletContext context;
	
	private String applicationConfFolder;
	private String applicationDeploymentFolder;
	private String applicationFolder;
	private String daCloudWebUrl;
	private String username;
	private String password;
	private int serverPort;
	
	@PostConstruct
	public void init(){
		this.applicationConfFolder = this.context.getInitParameter("applicationsConfFolder");
		this.applicationDeploymentFolder = this.context.getInitParameter("applicationsDeploymentFolder");
		this.applicationFolder = this.context.getInitParameter("applicationsFolder");
		this.daCloudWebUrl = this.context.getInitParameter("DACloudWebUrl");
		this.username = this.context.getInitParameter("username");
		this.password = this.context.getInitParameter("password");
		this.serverPort = Integer.valueOf(this.context.getInitParameter("serverPort"));
	}
	
	@Override
	public String getApplicationConfFolder() {
		return applicationConfFolder;
	}
	
	@Override
	public String getApplicationsDeploymentFolder(){
		return applicationDeploymentFolder;
	}

	@Override
	public String getApplicationFolder() {
		return this.applicationFolder;
	}
	@Override
	public String getDaCloudWebUrl() {
		return this.daCloudWebUrl;
	}
	@Override
	public String getUsername() {
		return this.username;
	}
	@Override
	public String getPassword() {
		return this.password;
	}
	@Override
	public int getServerPort() {
		return this.serverPort;
	}
}
