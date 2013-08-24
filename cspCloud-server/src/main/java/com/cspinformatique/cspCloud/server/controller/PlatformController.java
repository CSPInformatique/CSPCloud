package com.cspinformatique.cspCloud.server.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cspinformatique.cspCloud.commons.entity.Account;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancer;
import com.cspinformatique.cspCloud.server.service.AccountService;
import com.cspinformatique.cspCloud.server.service.ApplicationService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;
import com.cspinformatique.cspCloud.server.service.LoadBalancerService;

@Controller
@RequestMapping("/platform")
public class PlatformController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private LoadBalancerServerService loadBalancerServerService;
	
	@Autowired
	private LoadBalancerService loadBalancerService;
	
	@RequestMapping(method=RequestMethod.GET, value="")
	public String  getPlatformOverview(Principal principal){
		return "platform/overview";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/accountApplications/{accountId}")
	public @ResponseBody Collection<Application> getAccountApplications(@PathVariable int accountId){
		return this.applicationService.getUserApplications(accountId).values();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/applicationLoadBalancer/{applicationId}")
	public @ResponseBody LoadBalancer getApplicationLoadBalancer(@PathVariable int applicationId){
		return this.loadBalancerService.getApplicationLoadBalancer(
			this.applicationService.getApplicationById(applicationId)
		);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/accounts")
	public @ResponseBody List<Account> getApplicationsAccounts(){
		return this.accountService.getApplicationsAccounts();
	}
}
