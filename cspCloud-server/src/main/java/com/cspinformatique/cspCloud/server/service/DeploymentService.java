package com.cspinformatique.cspCloud.server.service;

import java.util.Date;

import com.cspinformatique.cspCloud.commons.entity.Application;

public interface DeploymentService {
	public void deleteDeployments(Application application);
	
	public void deploy(Application application, String packageUrl, String contextUrl, Date deploymentDate);
	
	public void executeDeployer(Application application);
}
