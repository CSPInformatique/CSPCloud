package com.cspinformatique.cspCloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.AuditService;
import com.cspinformatique.cspCloud.server.service.InstanceService;
import com.cspinformatique.cspCloud.server.service.PlatformService;
import com.cspinformatique.cspCloud.server.service.ServerService;
import com.cspinformatique.cspCloud.server.task.ServersMonitor;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private InstanceService instanceService;
	
	@Autowired
	private PlatformService platformService;
	
	@Autowired
	private ServersMonitor serverMonitor;
	
	@Autowired
	private ServerService serverService;
	
	@RequestMapping({"", "/overview"})
	public String getMonitoringOverview(Model model){
		model.addAttribute("platformStats", this.platformService.getPlatformStats());
		model.addAttribute("servers", this.serverService.loadServers().size());
		model.addAttribute("applications", this.applicationService.getApplications().size());
		model.addAttribute("instances", this.instanceService.getInstances().size());
		model.addAttribute("auditsCount", this.auditService.getAuditsCount());
		return "monitoring/overview";
	}
	
	@RequestMapping("/applications")
	public String getApplications(Model model){
		model.addAttribute("applications", this.applicationService.getApplications());
		
		return "monitoring/applications";
	}
	
	@RequestMapping("/instances")
	public String getInstances(Model model){
		model.addAttribute("instances", this.instanceService.getInstances());
		
		return "monitoring/instances";
	}
	
	@RequestMapping(value="/servers/{serverId}", method=RequestMethod.GET)
	public String getServer(@PathVariable int serverId, Model model){
		model.addAttribute("server", this.serverMonitor.getServer(serverId));
		
		return "monitoring/server";
	}
	
	
	@RequestMapping("/servers")
	public String getServers(Model model){
		model.addAttribute("servers", this.serverService.loadServers());
		
		return "monitoring/servers";
	}
}
