package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;

public interface ApplicationKitService {
	public ApplicationKit getApplicationKitByName(String name);
	
	public List<ApplicationKit> getApplicationKits();
}
