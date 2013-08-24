package com.cspinformatique.cspCloud.server.executor.application;

import com.cspinformatique.cspCloud.commons.entity.Application;

public class CreateApplicationExecutor extends ApplicationExecutor{
	public CreateApplicationExecutor(Application application) {
		super(application);
	}
	
	@Override
	public void execute(){
		this.applicationService.createApplication(application);
	}
}
