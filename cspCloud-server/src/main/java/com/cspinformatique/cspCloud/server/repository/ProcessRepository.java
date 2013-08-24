package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Process;

public interface ProcessRepository {
	public void createProcess(Process process);
	
	public void deleteProcess(Process process);
	
	public List<Long> getProcesses(String serverUrl);
}
