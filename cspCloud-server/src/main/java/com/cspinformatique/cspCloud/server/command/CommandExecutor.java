package com.cspinformatique.cspCloud.server.command;

public interface CommandExecutor {
	public void killProcess(long processId);
	
	public void startInstance(String instancePath);
	
	public void stopInstance(String instancePath);
}
