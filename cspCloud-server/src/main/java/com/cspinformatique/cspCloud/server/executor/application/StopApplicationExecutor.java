package com.cspinformatique.cspCloud.server.executor.application;

import com.cspinformatique.cspCloud.commons.entity.Application;

public class StopApplicationExecutor extends ApplicationExecutor {

	public StopApplicationExecutor(Application application) {
		super(application);
	}

	@Override
	protected void execute() {
		this.applicationService.stopApplication(application);
	}
}
