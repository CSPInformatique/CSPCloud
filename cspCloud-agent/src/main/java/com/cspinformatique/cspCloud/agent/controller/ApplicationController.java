package com.cspinformatique.cspCloud.agent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.agent.service.ApplicationService;
import com.cspinformatique.cspCloud.commons.entity.Application;

@Controller
@RequestMapping("/applications")
public class ApplicationController {
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(method=RequestMethod.POST, consumes="application/json", params="activateClustering")
	public void activateClustering(@RequestBody Application application){
		
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(method=RequestMethod.PUT, consumes="application/json")
	public void createApplication(@RequestBody Application application){
		this.applicationService.createApplication(application);
	}
}