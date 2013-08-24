package com.cspinformatique.cspCloud.commons.entity;

import java.io.Serializable;

public class ApplicationKit implements Serializable{
	private static final long serialVersionUID = 6655371169634697061L;

	public static final String TYPE_TOMCAT = "tomcat";
	
	private String name;
	private String path;
	private String type;
	
	public ApplicationKit(){
		
	}
	
	public ApplicationKit(String name, String path, String type){
		this.name = name;
		this.path = path;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public String getPath() {
		return path;
	}
	public String getType() {
		return type;
	}

	public String toString(){
		return this.getName();
	}
}
