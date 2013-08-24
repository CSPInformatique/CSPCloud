package com.cspinformatique.cspCloud.server.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.commons.util.FileUtil;
import com.cspinformatique.commons.util.SigarUtil;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;
import com.cspinformatique.cspCloud.server.command.CommandExecutor;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.service.InstanceService;
import com.cspinformatique.cspCloud.server.service.ProcessService;

@Component
public class InstanceServiceImpl implements InstanceService {
	private static final String TOMCAT_CONFIG_TEMPLATE = "/files/server.xml";
	
	@Autowired
	private CommandExecutor commandExecutor;
	
	@Autowired
	private ServletContext context;
	
	@Autowired
	private Params params;
	
	@Autowired
	private ProcessService processService;
	
	private Map<String, Instance> runningInstances;
	
	@Autowired
	private SigarUtil sigarUtil;
	
	public InstanceServiceImpl(){
		this.runningInstances = new HashMap<String, Instance>();
	}
	
	public void configureTomcatInstance(Instance instance){
		try{
			String template = this.loadTomcatConfigTemplate();
			for(ServerPort serverPort : instance.getServerPorts().values()){
				String portKey = serverPort.getType() + "Port";
				template =	template.replace(
								"{" + portKey + "}", 
								serverPort.getPort() + ""
							);
			}
			
			FileWriter fileWriter =	new FileWriter(this.getInstancePath(instance) + "/conf/server.xml");
	
			fileWriter.write(template);
			fileWriter.close();
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public void deleteInstance(Instance instance){
		if(	!runningInstances.containsKey(
				instance.getApplication().getAccount().getId() + instance.getApplication().getName()
			)
		){
			if(!FileUtil.deleteDir(new File(this.getInstancePath(instance)))){
				throw new RuntimeException("Instance's file system could not be deleted.");
			}
		}else{
			throw new RuntimeException("Instance is running.");
		}
	}
	
	private String getInstancePath(Instance instance){
		return this.params.getApplicationFolder() + "/" +
			instance.getApplication().getAccount().getId() + "/" + 
			instance.getApplication().getName();
	}
	
	public Map<String, Instance> getRunningInstances(){
		return this.runningInstances;
	}
	
	private String loadTomcatConfigTemplate(){
		try{
			String fileContent = "";
			
			FileReader fr = new FileReader(context.getRealPath(InstanceServiceImpl.TOMCAT_CONFIG_TEMPLATE)); 
			BufferedReader br = new BufferedReader(fr);
			
			String s; 
			while((s = br.readLine()) != null) { 
				fileContent += s + "\n";
			} 
			fr.close();
			
			return fileContent;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public void startInstance(Instance instance){
		try{
			this.commandExecutor.startInstance(this.getInstancePath(instance));
			
			long processId = 0; 
			while(processId == 0){
				Thread.sleep(200);
				processId =	this.sigarUtil.getPId(
								instance.getApplication().getAccount().getId() + "/" + 
										instance.getApplication().getName() + "/bin/bootstrap.jar"
							);
			}
			
			this.processService.createProcess(new Process(processId, instance.getServer()));
		}catch(InterruptedException interruptedEx){
			throw new RuntimeException(interruptedEx);
		}
	}
	
	public void stopInstance(Instance instance){
		this.processService.deleteProcess(
			new Process(
				this.sigarUtil.getPId(
					instance.getApplication().getAccount().getId() + "/" + 
						instance.getApplication().getName() + "/bin/bootstrap.jar"
				), 
				instance.getServer()
			)
		);
		
		this.commandExecutor.stopInstance(this.getInstancePath(instance));
	}
}
