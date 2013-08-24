package com.cspinformatique.cspCloud.commons.factory;

import com.cspinformatique.cspCloud.commons.entity.Account;
import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;

public abstract class ApplicationFactory {
	public static Application getApplication(
		int id, 
		String name, 
		Account account, 
		ApplicationKit applicationKit,
		int numberOfInstances,
		boolean clustered,
		String status
	){
		return new Application(id, name, account, applicationKit, numberOfInstances, clustered, status);
	}
	
	public static Application getNewApplication(Account account){
		return new Application(
			0, 
			"", 
			account, 
			ApplicationKitFactory.getNewApplicationKit(), 
			1,
			false,
			Application.STATUS_UNDEPLOYED
		);
	}
}
