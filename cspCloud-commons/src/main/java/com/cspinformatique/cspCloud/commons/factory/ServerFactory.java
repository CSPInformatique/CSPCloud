package com.cspinformatique.cspCloud.commons.factory;

import java.util.ArrayList;

import com.cspinformatique.cspCloud.commons.entity.Server;

public abstract class ServerFactory {
	public static Server getNewServer(String adminContext, String hostname, String ipAddress, int serverPort){
		return new Server(
			adminContext, 
			hostname, 
			0, 
			ipAddress, 
			new ArrayList<Long>(),
			serverPort, 
			null, 
			Server.STATUS_INACTIVE
		);
	}
}
