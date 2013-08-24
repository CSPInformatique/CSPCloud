package com.cspinformatique.cspCloud.agent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.agent.service.InstanceService;
import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;
import com.cspinformatique.cspCloud.commons.entity.Instance;


@Controller
@RequestMapping("/instance")
public class InstanceController {
	@Autowired
	private InstanceService instanceService;
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json", params="configure")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void configureInstance(@RequestBody Instance instance){
		if(instance.getApplication().getApplicationKit().getType().equals(ApplicationKit.TYPE_TOMCAT)){
			this.instanceService.configureTomcatInstance(instance);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json", params="delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteInstance(@RequestBody Instance instance){
		this.instanceService.deleteInstance(instance);
	}
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json", params="start")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void startInstance(@RequestBody Instance instance){		
		this.instanceService.startInstance(instance);
	}
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json", params="stop")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void stopInstance(@RequestBody Instance instance){		
		this.instanceService.stopInstance(instance);
	}
}
