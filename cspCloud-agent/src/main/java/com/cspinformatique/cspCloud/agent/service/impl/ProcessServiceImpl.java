package com.cspinformatique.cspCloud.agent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.cspinformatique.commons.util.RestUtil;
import com.cspinformatique.cspCloud.agent.command.CommandExecutor;
import com.cspinformatique.cspCloud.agent.params.Params;
import com.cspinformatique.cspCloud.agent.service.ProcessService;
import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.commons.rest.CSPCRestTemplate;

@Component
public class ProcessServiceImpl implements ProcessService {
	@Autowired
	private CommandExecutor commandExecutor;
	
	@Autowired
	private Params params;
	
	@Autowired
	protected CSPCRestTemplate restTemplate;
	
	public void createProcess(Process process){
		this.restTemplate.exchange( 
			this.params.getDaCloudWebUrl() + "/process", 
			HttpMethod.PUT, 
			new HttpEntity<Process>( 
				process, 
				RestUtil.createBasicAuthHeader(this.params.getUsername(), this.params.getPassword()) 
			), 
			Process.class 
		);
	}
	
	public void deleteProcess(Process process){
		this.restTemplate.exchange( 
			this.params.getDaCloudWebUrl() + "/process?delete", 
			HttpMethod.POST, 
			new HttpEntity<Process>( 
				process, 
				RestUtil.createBasicAuthHeader(this.params.getUsername(), this.params.getPassword()) 
			), 
			Process.class 
		);
		
		this.commandExecutor.killProcess(process.getId());
	}
}
