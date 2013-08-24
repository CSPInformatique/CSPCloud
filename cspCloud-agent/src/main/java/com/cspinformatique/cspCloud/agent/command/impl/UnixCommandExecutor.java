package com.cspinformatique.cspCloud.agent.command.impl;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.agent.command.CommandExecutor;

@Component
public class UnixCommandExecutor implements CommandExecutor {

	@Override
	public void killProcess(long processId) {
		try{
			new ProcessBuilder("kill", processId + "").start();
		}catch(IOException ioEx){
			throw new RuntimeException(ioEx);
		}
	}
	
	@Override
	public void startInstance(String instancePath){
		try{
			new ProcessBuilder(instancePath + "/" + "bin/startup.sh").start();
		}catch(IOException ioEx){
			throw new RuntimeException(ioEx);
		}
	}

	@Override
	public void stopInstance(String instancePath){
		try{
			new ProcessBuilder(instancePath + "/" + "bin/shutdown.sh").start();
		}catch(IOException ioEx){
			throw new RuntimeException(ioEx);
		}
	}
}
