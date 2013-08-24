package com.cspinformatique.cspCloud.server.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.server.service.ServerService;

@Controller
@RequestMapping("/servers")
public class ServerController {
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	private ServerService serverService;
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json", params="activate")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void activateServer(@RequestBody Server server){
		this.serverService.activateServer(server);
	}
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json")
	public @ResponseBody Server registerServer(@RequestBody Server server){		
		this.serverService.registerServer(server);
		return server;
	}
}
