package com.cspinformatique.cspCloud.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;
import com.cspinformatique.cspCloud.server.repository.ApplicationKitRepository;
import com.cspinformatique.cspCloud.server.service.ApplicationKitService;

@Component
@Transactional
public class ApplicationKitServiceImpl implements ApplicationKitService{
	@Autowired
	private ApplicationKitRepository applicationKitRepository;
	
	public ApplicationKit getApplicationKitByName(String name){
		return this.applicationKitRepository.getApplicationKitByName(name);
	}
	
	public List<ApplicationKit> getApplicationKits(){
		return this.applicationKitRepository.getApplicationKits();
	}
}
