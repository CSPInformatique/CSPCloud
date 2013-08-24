package com.cspinformatique.cspCloud.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.server.repository.ProcessRepository;
import com.cspinformatique.cspCloud.server.service.ProcessService;

@Component
@Transactional
public class ProcessServiceImpl implements ProcessService {
	@Autowired
	private ProcessRepository processRepository;
	
	@Override
	public void createProcess(Process process){
		this.processRepository.createProcess(process);
	}
	
	@Override
	public void deleteProcess(Process process){
		this.processRepository.deleteProcess(process);
	}
	
	@Override
	public List<Long> getProcesses(Server server){
		return this.processRepository.getProcesses(server.getServerUrl());
	}
}
