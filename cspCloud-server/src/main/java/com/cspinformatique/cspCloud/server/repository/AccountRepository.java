package com.cspinformatique.cspCloud.server.repository;

import java.util.List;

import com.cspinformatique.cspCloud.commons.entity.Account;

public interface AccountRepository {
	public void createAccount(Account account);
	
	public Account getAccount(String email);
	
	public Account getAccount(int id);
	
	public List<Account> getApplicationsAccounts();
	
	public boolean isEmailUnique(Account account);
}
