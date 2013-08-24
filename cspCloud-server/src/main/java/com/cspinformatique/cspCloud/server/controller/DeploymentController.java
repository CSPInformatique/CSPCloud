package com.cspinformatique.cspCloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.commons.entity.Deployment;
import com.cspinformatique.cspCloud.server.service.DeploymentService;

@Controller
@RequestMapping("/deployment")
public class DeploymentController {
	@Autowired
	private DeploymentService deploymentService;
	
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deploy(@RequestBody Deployment deployment){
		this.deploymentService.deploy(deployment);
	}
}
