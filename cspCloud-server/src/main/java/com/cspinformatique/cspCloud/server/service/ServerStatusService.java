package com.cspinformatique.cspCloud.server.service;

public interface ServerStatusService {
	public Integer getAverageCpuUsage();
	
	public Integer getAverageFreeMemory();
	
	public Integer getAverageFreePercentMemory();
	
	public Integer getTotalMemory();
}
