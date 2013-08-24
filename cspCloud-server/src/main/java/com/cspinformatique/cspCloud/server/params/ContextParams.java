package com.cspinformatique.cspCloud.server.params;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContextParams implements Params {
	@Autowired
	private ServletContext context;
	
	private String apacheBin;
	private String apacheConfFile;
	private String apacheRoot;
	private String apacheTemplateFile;
	private String deploymentFolder;
	private boolean emailActive;
	private String emailAlertsReceiver;
	private String emailPassword;
	private String emailSender;
	private String emailSmtpServer;
	private boolean emailSsl;
	private String emailUsername;
	private String loadBalancerInstallFolder;
	private String loadBalancerInstallKit;
	private int port;
	private String serviceDomain;
	private String systemPassword;
	private String systemUser;
	private String virtualHostTemplateFile;
	
	@PostConstruct
	public void init(){
		this.apacheBin = this.context.getInitParameter("apacheBin");
		this.apacheConfFile = this.context.getInitParameter("apacheConfFile");
		this.apacheRoot = this.context.getInitParameter("apacheRoot");
		this.apacheTemplateFile = this.context.getInitParameter("apacheTemplateFile");
		this.deploymentFolder = this.context.getInitParameter("deploymentsFolder");
		this.emailActive = Boolean.parseBoolean(this.context.getInitParameter("email.active"));
		this.emailAlertsReceiver = this.context.getInitParameter("email.alerts.receiver");
		this.emailPassword = this.context.getInitParameter("email.password");
		this.emailSender = this.context.getInitParameter("email.sender");
		this.emailSmtpServer= this.context.getInitParameter("email.smtp.server");
		this.emailSsl = Boolean.parseBoolean(this.context.getInitParameter("email.ssl"));
		this.emailUsername = this.context.getInitParameter("email.username");
		this.loadBalancerInstallFolder = this.context.getInitParameter("loadBalancerInstallFolder");
		this.loadBalancerInstallKit = this.context.getInitParameter("loadBalancerInstallKit");
		this.port = Integer.parseInt(this.context.getInitParameter("port"));
		this.serviceDomain = this.context.getInitParameter("serviceDomain");
		this.systemPassword = this.context.getInitParameter("systemPassword");
		this.systemUser = this.context.getInitParameter("systemUser");
		this.virtualHostTemplateFile = this.context.getInitParameter("virtualHostTemplateFile");
	}
	
	@Override
	public String getApacheBin(){
		return this.apacheBin;
	}
	
	@Override
	public String getApacheConfFile(){
		return this.apacheConfFile;
	}
	
	@Override
	public String getApacheRoot(){
		return this.apacheRoot;
	}
	
	@Override
	public String getApacheTemplateFile(){
		return this.apacheTemplateFile;
	}
	
	@Override
	public String getDeploymentFolder(){
		return this.deploymentFolder;
	}
	
	@Override
	public String getEmailAlertsReceiver(){
		return this.emailAlertsReceiver;
	}
	
	@Override
	public String getEmailPassword(){
		return this.emailPassword;
	}
	
	@Override
	public String getEmailSender(){
		return this.emailSender;
	}
	
	@Override
	public String getEmailSmtpServer(){
		return this.emailSmtpServer;
	}
	
	@Override
	public String getEmailUsername(){
		return this.emailUsername;
	}
	
	@Override
	public int getPort(){
		return this.port;
	}
	
	public String getLoadBalancerInstallFolder() {
		return loadBalancerInstallFolder;
	}

	public String getLoadBalancerInstallKit() {
		return loadBalancerInstallKit;
	}

	@Override
	public String getServiceDomain(){
		return this.serviceDomain;
	}

	@Override
	public String getSystemPassword() {
		return this.systemPassword;
	}
	
	@Override
	public String getSystemUser() {
		return this.systemUser;
	}
	
	@Override 
	public String getVirtualHostTemplateFile(){
		return this.virtualHostTemplateFile;
	}
	
	@Override
	public boolean isEmailActive(){
		return this.emailActive;
	}
	
	@Override
	public boolean isEmailSsl(){
		return this.emailSsl;
	}
}
