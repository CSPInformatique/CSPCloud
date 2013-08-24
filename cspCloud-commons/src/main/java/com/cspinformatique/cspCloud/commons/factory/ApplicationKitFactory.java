package com.cspinformatique.cspCloud.commons.factory;

import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;

public abstract class ApplicationKitFactory {
	public static ApplicationKit getApplicationKit(String name, String path, String type){
		return new ApplicationKit(name, path, type);
	}
	
	public static ApplicationKit getNewApplicationKit(){
		return new ApplicationKit("","","");
	}
}
