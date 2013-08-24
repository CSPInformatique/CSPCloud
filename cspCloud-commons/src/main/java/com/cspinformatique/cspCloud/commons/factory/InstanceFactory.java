package com.cspinformatique.cspCloud.commons.factory;

import java.util.HashMap;
import java.util.Map;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Deployment;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;

public abstract class InstanceFactory {
	public static Instance getInstance(
		Application application, 
		Server server,
		Deployment lastDeployment, 
		Map<Integer, ServerPort> serverPorts, 
		String status
	){
		if(serverPorts == null){
			serverPorts = new HashMap<Integer, ServerPort>();
		}
		return new Instance(application, server, lastDeployment, serverPorts, status);
	}
}
