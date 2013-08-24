package com.cspinformatique.cspCloud.agent.controller;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.cspCloud.agent.service.ServerService;
import com.cspinformatique.cspCloud.commons.entity.ServerStats;

@Controller
@RequestMapping("/server")
public class ServerController {
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private ServerService serverService;
	
	private HashMap<String, Process> runningApplications;
	
	public ServerController(){
		
	}
	
	@PostConstruct
	public void init(){
		try{
			if(runningApplications == null){
				this.servletContext.setAttribute(
					"runningApplications", 
					this.runningApplications = new HashMap<String, Process>()
				);
			}
			
			this.serverService.registerServer();
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, headers="Content-Type=application/json")
	public @ResponseBody ServerStats getServerStats(){
		return this.serverService.getServerStats();
	}
}
