package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;

public interface ApplicationKitRepository {
	public ApplicationKit getApplicationKit(int applicationId);
	
	public ApplicationKit getApplicationKitByName(String name);
	
	public List<ApplicationKit> getApplicationKits();
}
