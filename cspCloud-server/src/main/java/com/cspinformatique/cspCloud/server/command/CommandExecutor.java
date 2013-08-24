package com.cspinformatique.cspCloud.server.command;

public interface CommandExecutor {
	public boolean checkPortAvailability(String hostname, int port);
	
	public void killProcess(long processId);
}
