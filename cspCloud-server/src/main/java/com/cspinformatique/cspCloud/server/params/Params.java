package com.cspinformatique.cspCloud.server.params;

public interface Params {	
	public String getApacheBin();
	
	public String getApacheConfFile();
	
	public String getApacheRoot();
	
	public String getApacheTemplateFile();
	
	public String getDeploymentFolder();
	
	public String getEmailAlertsReceiver();
	
	public String getEmailPassword();
	
	public String getEmailSender();
	
	public String getEmailSmtpServer();
	
	public String getEmailUsername();
	
	public int getPort();
	
	public String getLoadBalancerInstallFolder();
	
	public String getLoadBalancerInstallKit();
	
	public String getSystemUser();
	
	public String getSystemPassword();
	
	public String getServiceDomain();
	
	public String getVirtualHostTemplateFile();
	
	public boolean isEmailActive();
	
	public boolean isEmailSsl();
}
