package com.cspinformatique.cspCloud.server.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.commons.entity.Account;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;
import com.cspinformatique.cspCloud.server.binder.AccountPropertyEditor;
import com.cspinformatique.cspCloud.server.binder.ApplicationKitPropertyEditor;
import com.cspinformatique.cspCloud.server.service.AccountService;
import com.cspinformatique.cspCloud.server.service.ApplicationKitService;
import com.cspinformatique.cspCloud.server.service.ApplicationService;

@Controller
@RequestMapping("/applications")
public class ApplicationController {
	@Autowired
	private AccountService accountService;
	
	private AccountPropertyEditor accountPropertyEditor;
	
	@Autowired
	private ApplicationKitService applicationKitService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ApplicationKitPropertyEditor applicationKitPropertyEditor;
	
	private Map<String, CreateApplicationExecutor> createApplicationExecutors;
	
	public ApplicationController(){
		this.createApplicationExecutors = new HashMap<String, CreateApplicationExecutor>();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST, params="activateClustering")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void activateClustering(@PathVariable int id){
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST, params="delete")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteApplication(@PathVariable int id){
		this.applicationService.deleteApplication(this.applicationService.getApplicationById(id));
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(Account.class, this.accountPropertyEditor);
		binder.registerCustomEditor(ApplicationKit.class, this.applicationKitPropertyEditor);
	}
	
	@RequestMapping(
		params="creationStatus"
	)
	public void isApplicationCreated(Principal principal, HttpServletResponse response){
		try{
			CreateApplicationExecutor executor = this.createApplicationExecutors.get(principal.getName());
			
			if(executor != null){
				if(executor.isCompleted()){
					this.createApplicationExecutors.remove(principal.getName());
					
					response.getWriter().write("true");
				}else{
					response.getWriter().write("false");
				}
			}else{
				response.getWriter().write("false");
			}
		}catch(IOException ioEx){
			throw new RuntimeException(ioEx);
		}
	}
	
	@RequestMapping(params="new", method=RequestMethod.GET)
	public String newApplication(Model model, Principal principal){
		model.addAttribute("application", applicationService.newApplication(principal.getName()));
		model.addAttribute("applicationKits", this.applicationKitService.getApplicationKits());
		
		return "application/addApplication";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String saveApplication(
		@Valid Application application, 
		BindingResult bindingResult, 
		Principal principal
	){
		application.setAccount(this.accountService.getAccount(principal.getName()));
		
		CreateApplicationExecutor executor = new CreateApplicationExecutor(application);
		
		this.createApplicationExecutors.put(
			principal.getName(),
			executor
		);
		
		new Thread(executor).start();
		
		return "application/creatingApplication";
	}
	
	
	@RequestMapping(value="/{id}", params="configure", method=RequestMethod.GET)
	public String showConfigurationPage(@PathVariable int id, Model model){
		model.addAttribute("application", this.applicationService.getApplicationById(id));
		
		return "application/configure";
	}
	
	@RequestMapping(value="/{id}", params="showDeleteWindow", method=RequestMethod.GET)
	public String showDeleteWindow(@PathVariable int id, Model model){
		model.addAttribute("application", this.applicationService.getApplicationById(id));
		
		return "application/deleteApplication";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String showUserApplications(Model model, Principal principal){
		model.addAttribute(
			"applications", 
			this.applicationService.getUserApplications(
				this.accountService.getAccount(principal.getName()).getId()
			).values().toArray()
		);
		
		return "applications";
	}
	
	@RequestMapping(value="/{id}",params="start", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void startApplication(@PathVariable int id, Principal principal){
		Application application = this.applicationService.getApplicationById(id);
		
		if(application.getAccount().getEmail().equals(principal.getName())){
			this.applicationService.startApplication(application);
		}
	}
	
	@RequestMapping(value="/{id}",params="stop", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void stopApplication(@PathVariable int id, Principal principal){
		Application application = this.applicationService.getApplicationById(id);
		
		if(application.getAccount().getEmail().equals(principal.getName())){
			this.applicationService.stopApplication(application);
		}
	}
	
	protected class CreateApplicationExecutor implements Runnable{
		private Application application;
		private boolean completed;
		
		public CreateApplicationExecutor(Application application){
			this.application = application;
		}
		
		@Override
		public void run() {
			this.completed = false;
			applicationService.createApplication(application);
			this.completed = true;
		}
		
		public boolean isCompleted(){
			return this.completed;
		}
	}
}
