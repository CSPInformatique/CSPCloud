package com.cspinformatique.cspCloud.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.entity.ServerStats;
import com.cspinformatique.cspCloud.server.entity.PlatformStats;
import com.cspinformatique.cspCloud.server.service.PlatformService;
import com.cspinformatique.cspCloud.server.task.ServersMonitor;

@Component
public class PlatformServiceImpl implements PlatformService {
	@Autowired
	private ServersMonitor serverMonitoring;
	
	@Override
	public PlatformStats getPlatformStats() {
		int totalAvailableDiskSpace = 0;
		int totalAverageCpuUsage = 0;
		int totalAverageFreeMemory = 0;
		int totalAverageFreePercentMemory = 0;
		int totalTotalMemory = 0;
		
		List<Server> servers = this.serverMonitoring.getServers();
		
		for(Server server : servers){
			ServerStats stats = server.getStats();
			totalAvailableDiskSpace += stats.getAvailableDiskSpace();
			totalAverageCpuUsage += stats.getAverageCpuUsage();
			totalAverageFreeMemory += stats.getAverageFreeMemory();
			totalAverageFreePercentMemory += stats.getAverageFreePercentMemory();
			totalTotalMemory = stats.getTotalMemory();
		}
		
		int numberOfServers = servers.size();
		
		// Prevents the devide by 0. If numberOfServers == 0, all the stats will remain at 0 anyway.
		if(numberOfServers == 0) numberOfServers = 1;
		
		return new PlatformStats(
			totalAvailableDiskSpace / numberOfServers, 
			totalAverageCpuUsage / numberOfServers, 
			totalAverageFreeMemory / numberOfServers, 
			totalAverageFreePercentMemory / numberOfServers, 
			totalTotalMemory / numberOfServers
		);
	}

}
