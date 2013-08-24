package com.cspinformatique.cspCloud.server.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.commons.apache.entity.ApacheServer;
import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;
import com.cspinformatique.cspCloud.server.service.CSPCloudServerService;

@Controller
@RequestMapping("/CSPCloudServer")
public class CSPCloudServerController {
	@Autowired
	private CSPCloudServerService CSPCloudServerService;
	
	@PostConstruct
	public void init(){
		this.CSPCloudServerService.registerServer();
	}
	
	@RequestMapping(headers="Content-Type=application/json", method=RequestMethod.POST, params="configure")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void configureApache(@RequestBody ApacheServer apacheServer){
		this.CSPCloudServerService.configureApache(apacheServer);
	}
	
	@RequestMapping(headers="Content-Type=application/json", method=RequestMethod.POST, params="ping")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void pingServer(@RequestBody CSPCloudServer cspCloudServer){}
	
	@RequestMapping(headers="Content-Type=application/json", method=RequestMethod.POST, params="restart")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void restartApache(@RequestBody CSPCloudServer cspCloudServer){
		this.CSPCloudServerService.restartApache(cspCloudServer);
	}
}
