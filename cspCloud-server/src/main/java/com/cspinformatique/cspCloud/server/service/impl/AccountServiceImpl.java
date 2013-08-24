package com.cspinformatique.cspCloud.server.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cspinformatique.cspCloud.commons.entity.Account;
import com.cspinformatique.cspCloud.commons.exception.CSPCloudException;
import com.cspinformatique.cspCloud.server.repository.AccountRepository;
import com.cspinformatique.cspCloud.server.service.AccountService;

@Component
@Transactional
public class AccountServiceImpl implements AccountService{
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void createAccount(
		Account account,
		String ftpFolder
	){
		// Creating the account in the database,
		this.accountRepository.createAccount(account);
			
		// Create the ftp folder.
		this.createFtpFolder(account, ftpFolder);
	}
	
	private void createFtpFolder(Account account, String ftpFolder){
		if(!new java.io.File(ftpFolder).mkdirs()){
			throw new CSPCloudException("FTP folder creation error");
		}
	}
	
	@Override
	public Account getAccount(String email){
		return this.accountRepository.getAccount(email);
	}
	
	@Override
	public Account getAccount(int id){
		return this.accountRepository.getAccount(id);
	}
	
	@Override
	public List<Account> getApplicationsAccounts(){
		return this.accountRepository.getApplicationsAccounts();
	}
	
	@Override
	public String hashPassword(String passwordToHash){
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");

	        md.update(passwordToHash.getBytes());
	 
	        byte byteData[] = md.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			throw new CSPCloudException(ex);
		}
	}
}
