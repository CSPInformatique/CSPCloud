package com.cspinformatique.cspCloud.server.entity;

import com.cspinformatique.cspCloud.commons.entity.ServerStats;

public class PlatformStats extends ServerStats{
	public PlatformStats(){
		super();
	}
	
	public PlatformStats(
		int availableDiskSpace, 
		int averageCpuUsage, 
		int averageFreeMemory, 
		int averageFreePercentMemory,
		int totalMemory
	){
		super(
			availableDiskSpace, 
			averageCpuUsage, 
			averageFreeMemory, 
			averageFreePercentMemory,
			totalMemory
		);
	}
}
