package com.cspinformatique.cspCloud.server.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.DeploymentService;

@Controller
@RequestMapping("/deployment") 
public class DeploymentController {
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private DeploymentService deploymentService;
	
	@Autowired
	private Params params;
	
	@Autowired
	private ServletContext servletContext;
	
	@RequestMapping(value="/{applicationId}", method=RequestMethod.POST)
	public void deployPackage(
		@PathVariable int applicationId, 
		@RequestParam(value="contextToDeploy", required=false) MultipartFile contextToDeploy, 
		@RequestParam(value="packageToDeploy", required=true) MultipartFile packageToDeply,
		HttpServletRequest request
	){
		try{
			// Defining the deploymentDate.
			Date deploymentDate = new Date();
			
			Application application = this.applicationService.getApplicationById(applicationId);
			
			String formatedDeploymentDate = new SimpleDateFormat("yyyy-MM-dd_HHmm").format(deploymentDate);
			File deploymentFolder =	new File(
										this.servletContext.getRealPath(
											this.params.getDeploymentFolder() + "/" + 
												application.getId() + "-" + application.getName() + "/" +
												formatedDeploymentDate
										)
									);
			
			deploymentFolder.mkdirs();
			
			String url =	"http://" + 
								request.getServerName() + ":" + 
								request.getServerPort() + 
								request.getContextPath() + 
								this.params.getDeploymentFolder() + "/" +
								application.getId() + "-" + application.getName() + "/" +
								formatedDeploymentDate;
			
			// Controller needs to save files found in request.
			String packageUrl = url + "/" + application.getName() + ".war";
			String contextUrl = null;
			
			// Archiving package file.
			FileUtils.writeByteArrayToFile(
				new File(deploymentFolder + "/" + application.getName() + ".war"), 
				packageToDeply.getBytes()
			);
			
			// Archiving context file if necessairy.
			if(contextToDeploy.getSize() > 0){
				FileUtils.writeByteArrayToFile(
					new File(deploymentFolder + "/" + application.getName() + ".xml"), 
					packageToDeply.getBytes()
				);
				contextUrl = url + "/" + application.getName() + ".xml";
			}

			// Initiating the deployment.
			deploymentService.deploy(
				application, 
				packageUrl, 
				contextUrl, 
				deploymentDate
			);
		}catch(IOException ioEx){
			throw new RuntimeException(ioEx);
		}
	}
	
	@RequestMapping(value="/{applicationId}", method=RequestMethod.GET)
	public String newDeployment(@PathVariable int applicationId, Model model){
		model.addAttribute("application", this.applicationService.getApplicationById(applicationId));
		
		return "application/deployPackage";
	}
}
