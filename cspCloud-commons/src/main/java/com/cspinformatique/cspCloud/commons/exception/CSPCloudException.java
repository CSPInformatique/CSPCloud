package com.cspinformatique.cspCloud.commons.exception;

public class CSPCloudException extends RuntimeException {
	private static final long serialVersionUID = 8637313803583685946L;

	public CSPCloudException(){
		super();
	}
	
	public CSPCloudException(String message){
		super(message);
	}
	
	public CSPCloudException(Throwable cause){
		super(cause);
	}
	
	public CSPCloudException(String message, Throwable cause){
		super(message, cause);
	}
}
