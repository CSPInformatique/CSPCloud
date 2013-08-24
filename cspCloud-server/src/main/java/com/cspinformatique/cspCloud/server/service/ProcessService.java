package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.commons.entity.Server;

public interface ProcessService {
	public void createProcess(Process process);
	
	public void deleteProcess(Process process);
	
	public List<Long> getProcesses(Server server);
}
