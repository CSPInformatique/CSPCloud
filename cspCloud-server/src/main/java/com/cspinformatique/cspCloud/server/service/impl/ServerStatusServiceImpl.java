package com.cspinformatique.cspCloud.server.service.impl;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cspinformatique.commons.util.SigarUtil;

@Component
public class ServerStatusServiceImpl {
	@Autowired
	private SigarUtil sigarUtil;
	
	private ArrayList<Integer> cpuReads;
	private ArrayList<Integer> averageFreeMemReads;
	private ArrayList<Integer> averagePercentMemReads;
	private long totalMemory;
	
	public ServerStatusServiceImpl() {
		this.cpuReads = new ArrayList<Integer>();
		this.averageFreeMemReads = new ArrayList<Integer>();
		this.averagePercentMemReads = new ArrayList<Integer>();
	}
	
	@PostConstruct
	private void init(){
		this.totalMemory = this.sigarUtil.getTotalMemory();
	}

	@Scheduled(fixedRate=100)
	private void execute() throws Exception {
		// Retreiving CPU usage;
		if(this.cpuReads.size() > 1800){
			this.cpuReads.remove(0);
		}
		this.cpuReads.add(this.sigarUtil.getAverageCpu());
		
		if(this.averageFreeMemReads.size() > 1800){
			this.averageFreeMemReads.remove(0);
		}
		this.averageFreeMemReads.add(this.sigarUtil.getFreeMemory());
		
		if(this.averagePercentMemReads.size() > 1800){
			this.averagePercentMemReads.remove(0);
		}
		this.averagePercentMemReads.add(this.sigarUtil.getUsedMemoryPercent());
	}
	
	public Integer getAverageCpuUsage(){
		double sum = 0d;
		for(Integer value : this.cpuReads){
			sum += value;
		}
		
		return (int)(sum / this.cpuReads.size());
	}

	public Integer getAverageFreeMemory(){
		double sum = 0d;
		for(Integer value : this.averageFreeMemReads){
			sum += value;
		}
		
		return (int)(sum / this.averageFreeMemReads.size());
	}
	
	public Integer getAverageFreePercentMemory(){
		double sum = 0d;
		for(Integer value : this.averagePercentMemReads){
			sum += value;
		}
		
		return (int)(sum / this.averagePercentMemReads.size());
	}
	
	public Integer getTotalMemory(){
		return (int)this.totalMemory;
	}
}
