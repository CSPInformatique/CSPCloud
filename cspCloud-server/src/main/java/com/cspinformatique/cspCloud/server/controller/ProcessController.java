package com.cspinformatique.cspCloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.server.service.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessController {
	@Autowired
	private ProcessService processService;
	
	@RequestMapping(method=RequestMethod.PUT, headers="Content-Type=application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void createProcess(@RequestBody Process process){
		this.processService.createProcess(process);
	}
	
	@RequestMapping(method=RequestMethod.POST, headers="Content-Type=application/json")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProcess(@RequestBody Process process){
		this.processService.deleteProcess(process);
	}
}
