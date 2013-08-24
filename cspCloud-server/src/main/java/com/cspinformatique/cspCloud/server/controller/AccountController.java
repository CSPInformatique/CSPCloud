package com.cspinformatique.cspCloud.server.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cspinformatique.cspCloud.commons.entity.Account;

@RequestMapping({"/account"})
public class AccountController {	
	@RequestMapping(method=RequestMethod.GET, params="new")
	public String createAccount(Model model){
		model.addAttribute(new Account());
		
		return "account/create";
	}
}
