package com.cspinformatique.cspCloud.server.repository;

import com.cspinformatique.cspCloud.commons.entity.Deployment;

public interface DeploymentRepository {
	public void createDeployment(Deployment deployment);
	
	public void deleteDeployments(int applicationId);
	
	public Deployment getDeployment(final int id);
	
	public Deployment getLastDeployment(final int applicationId);
}
