package com.cspinformatique.cspCloud.server.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cspinformatique.cspCloud.commons.entity.Account;
import com.cspinformatique.cspCloud.server.service.AccountService;
import com.cspinformatique.cspCloud.server.service.ApplicationService;

@Controller
public class HomeController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired 
	private ApplicationController applicationController;
	
	@Autowired
	private MonitoringController monitoringController;
	
	@RequestMapping(value="")
	public String showIndexPage(){
		return "index";
	}
	
	@RequestMapping(value="/home")
	public String showHomePage(Model model, Principal principal){
		Account account = this.accountService.getAccount(principal.getName());
		
		if(account.getType().equals(Account.TYPE_USER)){
			return this.applicationController.showUserApplications(model, principal);
		}else if(account.getType().equals(Account.TYPE_ADMIN)){
			return this.monitoringController.getMonitoringOverview(model);
		}else{
			throw new RuntimeException("Invalid account type.");
		}
	}
	
	@RequestMapping(value={"/admin/dashboard","/admin"})
	public String showAdminDashboard(){
		return "admin/dashboard";
	}
	
	@RequestMapping(value="/login")
	public String showLoginPage(Principal principal){
		if(principal == null){
			return "login";
		}else{
			return this.showIndexPage();
		}
	}
	
	@RequestMapping(value="/menu")
	public String showMenu(Model model, Principal principal){
		model.addAttribute(
			"applications", 
			this.applicationService.getUserApplications(
				this.accountService.getAccount(principal.getName()).getId()
			).values().toArray()
		);
		
		return "menu";
	}
}
