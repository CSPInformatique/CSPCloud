package com.cspinformatique.cspCloud.commons.entity;

public class ServerStats {
	private int availableDiskSpace;
	private int averageCpuUsage;
	private int averageFreeMemory;
	private int averageFreePercentMemory;
	private int totalMemory;
	
	public ServerStats(){
		
	}
	
	public ServerStats(
		int availableDiskSpace, 
		int averageCpuUsage, 
		int averageFreeMemory, 
		int averageFreePercentMemory,
		int totalMemory
	){
		this.availableDiskSpace = availableDiskSpace;
		this.averageCpuUsage = averageCpuUsage;
		this.averageFreeMemory = averageFreeMemory;
		this.averageFreePercentMemory = averageFreePercentMemory;
		this.totalMemory = totalMemory;
	}
	
	public int getAvailableDiskSpace() {
		return availableDiskSpace;
	}
	public int getAverageCpuUsage() {
		return averageCpuUsage;
	}
	public int getAverageFreeMemory() {
		return averageFreeMemory;
	}
	public int getAverageFreePercentMemory() {
		return averageFreePercentMemory;
	}
	public int getTotalMemory() {
		return totalMemory;
	}
}
