package com.cspinformatique.cspCloud.server.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.commons.util.FileUtil;
import com.cspinformatique.commons.util.ZipUtil;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Deployment;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.service.DeploymentService;
import com.cspinformatique.cspCloud.server.service.InstanceService;

@Component
@Transactional
public class DeploymentServiceImpl implements DeploymentService {
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
	private Params params;
	
	public void deploy(Deployment deployment){
		Application application = deployment.getApplication();
		if(	!this.instanceService.getRunningInstances().containsKey(
				application.getAccount().getId() + application.getName()
		)){
			String applicationFolder =	params.getApplicationFolder() + "/" +
											application.getAccount().getId() + "/" + 
											application.getName();

			String warFilename = application.getName() + ".war";
			
			// Deleting old deployment folder.
			String deploymentFolder = applicationFolder + params.getApplicationsDeploymentFolder();
			
			new File(deploymentFolder + "/" + application.getName()).delete();
			
			// Deleting old war.
			String warFile = deploymentFolder + "/" + warFilename;
			new File(warFile).delete();
			
			// Download new package.
			FileUtil.downloadFile(deployment.getPackageUrl(), deploymentFolder, warFilename);
			
			// Checking if context needs to be deployed.
			if(deployment.getContextUrl() != null && !deployment.getContextUrl().equals("")){
				// Deleting old context file in conf folder.
				String confFolder = applicationFolder + params.getApplicationConfFolder();
				String confFile = confFolder + "/" + application.getName() + ".xml";
				
				new File(confFile).delete();
				
				// Download new context file.
				FileUtil.downloadFile(deployment.getContextUrl(), deploymentFolder, warFilename);
			}
			
			// Unzip the package.
			ZipUtil.extractFolder(
				deploymentFolder + "/" + warFilename, 
				deploymentFolder + "/" + application.getName()
			);
		}else{
			throw new RuntimeException("Instance is running.");
		}
	}
}
