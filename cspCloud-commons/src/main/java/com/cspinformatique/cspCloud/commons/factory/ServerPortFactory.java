package com.cspinformatique.cspCloud.commons.factory;

import com.cspinformatique.cspCloud.commons.entity.ServerPort;

public abstract class ServerPortFactory {
	public static ServerPort getServerPor(int port, String type){
		return new ServerPort(port, type);
	}
}
