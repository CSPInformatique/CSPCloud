package com.cspinformatique.cspCloud.commons.entity;

import java.io.Serializable;

public class Account implements Serializable {
	private static final long serialVersionUID = 6871715010664269570L;
	
	public static final String SESSION_ATTRIBUTE = "account";
	
	public static final String TYPE_ADMIN = "admin";
	public static final String TYPE_USER = "user";
	public static final String TYPE_SYSTEM = "system";
	
	private int id;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String type;
	
	public Account(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
