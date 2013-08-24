package com.cspinformatique.cspCloud.server.binder;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.server.service.ApplicationKitService;

@Component
public class ApplicationKitPropertyEditor extends PropertyEditorSupport {
	@Autowired
	private ApplicationKitService applicationKitService;
	
	public void setAsText(String name){
		this.setValue(this.applicationKitService.getApplicationKitByName(name));
	}
	
	public String getAsText(){
		return this.getValue().toString();
	}
}
