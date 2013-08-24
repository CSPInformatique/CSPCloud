package com.cspinformatique.cspCloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;
import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;

@Controller
@RequestMapping("/LoadBalancerServer")
public class LoadBalancerServerController {
	@Autowired
	private LoadBalancerServerService loadBalancerServerService;
	
	@RequestMapping(headers="Content-Type=application/json", method=RequestMethod.POST, params="configure")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void configureLoadBalancerServer(@RequestBody LoadBalancerServer loadBalancerServer){
		this.loadBalancerServerService.configureLoadBalancerServer(loadBalancerServer);
	}
	
	@RequestMapping(headers="Content-Type=application/json", method=RequestMethod.POST, params="create")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void createLoadBalancerServer(@RequestBody LoadBalancerServer loadBalancerServer){
		this.loadBalancerServerService.deployLoadBalancerServer(loadBalancerServer);
	}
	
	@RequestMapping(headers="Content-Type=application/json", method=RequestMethod.POST, params="restart")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void restartLoadBalancerServer(@RequestBody LoadBalancerServer loadBalancerServer){
		this.loadBalancerServerService.restartLoadBalancerServer(loadBalancerServer);
	}
}
