package com.cspinformatique.cspCloud.server.binder;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.server.service.AccountService;

@Component
public class AccountPropertyEditor extends PropertyEditorSupport {
	
	@Autowired
	private AccountService accountService;
	
	public void setAsText(String email){
		this.setValue(this.accountService.getAccount(email));
	}
	
	public String getAsText(){
		return this.getValue().toString();
	}
}
