package com.cspinformatique.cspCloud.server.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.commons.util.FileUtil;
import com.cspinformatique.commons.util.ZipUtil;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.service.ApplicationService;

@Component
public class ApplicationServiceImpl implements ApplicationService {	
	@Autowired
	private Params params;
	
	public void createApplication(Application application){
		String fileToDownload =	this.params.getDaCloudWebUrl() + 
									application.getApplicationKit().getPath();
		
		String destinationFilename = fileToDownload.substring(fileToDownload.lastIndexOf("/") + 1);
		
		String destinationFolder =	params.getApplicationFolder() + "/" +
										application.getAccount().getId() + "/" + 
										application.getName();
		
		FileUtil.downloadFile(fileToDownload, destinationFolder, destinationFilename);
		
		ZipUtil.extractFolder(
			destinationFolder + "/" + destinationFilename, 
			destinationFolder
		);
		
		// Delete the install kit
		new File(destinationFolder + "/" + destinationFilename).delete();
	}
}
