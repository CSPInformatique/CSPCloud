package com.cspinformatique.cspCloud.server.service;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Account;

public interface AccountService {
	public void createAccount(Account account, String ftpFolder);
		
	public Account getAccount(String email);
	
	public Account getAccount(int id);
	
	public List<Account> getApplicationsAccounts();
	
	public String hashPassword(String passwordToHash);
}
