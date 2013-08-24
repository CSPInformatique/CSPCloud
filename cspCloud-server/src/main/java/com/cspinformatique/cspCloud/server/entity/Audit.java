package com.cspinformatique.cspCloud.server.entity;

import java.util.Date;

public class Audit {
	public static final String TYPE_ALERT = "alert";
	public static final String TYPE_ERROR = "error";
	public static final String TYPE_INFO = "info";
	public static final String TYPE_WARNING = "warning";
	
	private long id;
	private String message;
	private Date timestamp;
	private String type;
	
	public Audit(){
		
	}
	
	public Audit(long id, String message, Date timestamp, String type){
		this.id = id;
		this.message = message;
		this.timestamp = timestamp;
		this.type = type;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message){
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp){
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
}
