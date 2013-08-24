package com.cspinformatique.cspCloud.agent.service;

public interface ServerStatusService {
	public Integer getAverageCpuUsage();
	
	public Integer getAverageFreeMemory();
	
	public Integer getAverageFreePercentMemory();
	
	public Integer getTotalMemory();
}
