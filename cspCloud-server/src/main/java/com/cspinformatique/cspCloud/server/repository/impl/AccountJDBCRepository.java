package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Account;
import com.cspinformatique.cspCloud.server.repository.AccountRepository;

@Component
public class AccountJDBCRepository implements AccountRepository {
	private static final String SQL_INSERT_ACCOUNT =	"INSERT INTO accounts (	" +
														"	email, " +
														"	firstName, " +
														"	lastName, " +
														"	password," +
														"	type " +
														") VALUES (?,?,?,?,?)";
	
	private static final String SQL_GET_ACCOUNT_BY_EMAIL = 	"SELECT " +
															"	id " +
															"FROM " +
															"	accounts " +
															"WHERE " +
															"	email = ? ";
	
	private static final String SQL_GET_ACCOUNT_BY_ID = 	"SELECT " +
															"	email," +
															"	firstName, " +
															"	lastName," +
															"	type " +
															"FROM " +
															"	accounts " +
															"WHERE " +
															"	id = ? ";
	
	private static final String SQL_GET_APP_ACCOUNTS = 	"SELECT " +
														"	email " +
														"FROM " +
														"	accounts " +
														"WHERE " +
														"	type = ? ";
	
	private static final String SQL_VALIDATE_EMAIL =	"SELECT " +
														"	1 " +
														"FROM " +
														"	accounts " +
														"WHERE" +
														"	email = ?";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void createAccount(Account account){
		jdbcTemplate.update(
			SQL_INSERT_ACCOUNT, 
			account.getEmail(), 
			account.getFirstName(), 
			account.getLastName(), 
			account.getPassword(),
			account.getType()
		);
	}
	
	public Account getAccount(String email){
		return jdbcTemplate.queryForObject(
			SQL_GET_ACCOUNT_BY_EMAIL,
		    new ParameterizedRowMapper<Account>() {
				@Override
				public Account mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					return getAccount(resultSet.getInt("id"));
				} 
			},
			email
		);
	}
	
	@Override
	public Account getAccount(final int id){
		return jdbcTemplate.queryForObject(
			SQL_GET_ACCOUNT_BY_ID,
		    new ParameterizedRowMapper<Account>() {
				@Override
				public Account mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					Account account = new Account();
					
					account.setId(id);
					account.setFirstName(resultSet.getString("firstName"));
					account.setLastName(resultSet.getString("lastName"));
					account.setEmail(resultSet.getString("email"));
					account.setType(resultSet.getString("type"));
					
					return account;
				} 
			},
			id
		);
	}
	
	public List<Account> getApplicationsAccounts(){
		return this.jdbcTemplate.query(
			SQL_GET_APP_ACCOUNTS, 
			new RowMapper<Account>(){
				@Override
				public Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getAccount(resultSet.getString("email"));
				}
			},
			Account.TYPE_USER
		);
	}
	
	@Override
	public boolean isEmailUnique(Account account){
		try{
			int result = jdbcTemplate.queryForObject(
							SQL_VALIDATE_EMAIL,
							Integer.class,
							account.getEmail()
						);
			
			if(result == 1){
				return true;
			}else{
				return false;
			}
		}catch(IncorrectResultSizeDataAccessException ex){
			return false;
		}
	}
}
