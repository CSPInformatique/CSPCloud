package com.cspinformatique.cspCloud.server.executor.application;

import org.springframework.beans.factory.annotation.Autowired;

import com.cspinformatique.commons.thread.Executor;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.server.service.ApplicationService;

public abstract class ApplicationExecutor extends Executor {
	protected Application application;
	
	@Autowired
	protected ApplicationService applicationService;
	
	public ApplicationExecutor(Application application){
		this.application = application;
	}
	
	@Override
	protected abstract void execute();
}
