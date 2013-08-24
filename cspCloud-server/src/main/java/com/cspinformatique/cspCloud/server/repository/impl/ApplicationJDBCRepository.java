package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.factory.ApplicationFactory;
import com.cspinformatique.cspCloud.server.repository.AccountRepository;
import com.cspinformatique.cspCloud.server.repository.ApplicationKitRepository;
import com.cspinformatique.cspCloud.server.repository.ApplicationRepository;

@Component
public class ApplicationJDBCRepository implements ApplicationRepository {
	private static final String SQL_ACTIVATE_CLUSTERING =	"UPDATE " +
															"	applications " +
															"SET " +
															"	clustering = true " +
															"WHERE " +
															"	id = ?";
	
	private static final String SQL_ACTIVATE_LB =	"UPDATE " +
													"	applications " +
													"SET " +
													"	loadBalanced = true " +
													"WHERE " +
													"	id = ?";
	
	private static final String SQL_DELETE_APPLICATION =	"DELETE FROM " +
															"	applications " +
															"WHERE " +
															"	id = ?";
	
	private static final String SQL_INSERT_APPLICATION =	"INSERT INTO applications ( " +
															"	name, " +
															"	accountId, " +
															"	applicationKit, " +
															"	numberOfInstances, " +
															"	clustered, " +
															"	status " +
															") VALUES ( " +
															"	?,?,?,?,?, ? " +
															") ";
	
	private static final String SQL_SELECT_APPLICATION =	"SELECT " +
															"	applications.name, " +
															"	applications.numberOfInstances, " +
															"	applications.clustered, " +
															"	applications.status, " +
															"	accounts.email " +
															"FROM " +
															"	applications," +
															"	accounts " +
															"WHERE " +
															"	applications.id = ? AND" +
															"	applications.accountId = accounts.id";
	
	private static final String SQL_SELECT_APPLICATIONS =	"SELECT " +
															"	id " +
															"FROM " +
															"	applications";
	
	private static final String SQL_FIND_ID =	"SELECT " +
												"	applications.id " +
												"FROM " +
												"	applications, " +
												"	accounts " +
												"WHERE " +
												"	accounts.id = applications.accountId AND " +
												"	applications.name = ? AND " +
												"	accounts.email = ? ";
	
	private static final String SQL_SELECT_USERS_APPS =	"SELECT " +
														"	id " +
														"FROM " +
														"	applications " +
														"WHERE " +
														"	accountId = ? ";
	
	private static final String SQL_UPDATE_STATUS =	"UPDATE " +
													"	applications " +
													"SET " +
													"	status = ? " +
													"WHERE " +
													"	id = ?";
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ApplicationKitRepository applicationKitRepository;
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Override
	public void activateClustering(int applicationId){
		this.jdbcTemplate.update(SQL_ACTIVATE_CLUSTERING, applicationId);
	}
	
	@Override
	public void activateLoadBalancing(int applicationId){
		this.jdbcTemplate.update(SQL_ACTIVATE_LB, applicationId);
	}
	
	@Override
	public void createApplication(Application application) {
		this.jdbcTemplate.update(
			SQL_INSERT_APPLICATION,
			application.getName(),
			application.getAccount().getId(),
			application.getApplicationKit().getName(),
			application.getNumberOfInstances(),
			application.isClustered(),
			application.getStatus()
		);
	};
	
	@Override
	public void deleteApplication(int id){
		this.jdbcTemplate.update(SQL_DELETE_APPLICATION, id);
	}
	
	@Override
	public int findApplicationId(String name, String email){
		return this.jdbcTemplate.queryForObject(SQL_FIND_ID, Integer.class, name, email);
	}
	
	@Override
	public Application getApplicationById(final int id){
		Application application = this.jdbcTemplate.queryForObject(
			SQL_SELECT_APPLICATION, 
			new RowMapper<Application>(){
				@Override
				public Application mapRow(ResultSet resultSet, int rownum) throws SQLException {				
					return ApplicationFactory.getApplication(
						id,
						resultSet.getString("name"),
						accountRepository.getAccount(resultSet.getString("email")), 
						applicationKitRepository.getApplicationKit(id),
						resultSet.getInt("numberOfInstances"),
						resultSet.getBoolean("clustered"),
						resultSet.getString("status")
					);
				}
			}, 
			id
		);
		
		return application;
	}
	
	@Override
	public List<Application> getApplications(){
		return this.jdbcTemplate.query(SQL_SELECT_APPLICATIONS, new RowMapper<Application>(){
			@Override
			public Application mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return getApplicationById(resultSet.getInt("id"));
			}
		});
	}
	
	@Override
	public List<Integer> getUserApplications(int acountId) {
		return this.jdbcTemplate.query(
			SQL_SELECT_USERS_APPS, 
			new RowMapper<Integer>(){
				@Override
				public Integer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return Integer.valueOf(resultSet.getInt("id"));
				}
			}, 
			acountId
		);
	}
	
	@Override
	public void updateApplicationStatus(int applicationId, String status){		
		this.jdbcTemplate.update(
			SQL_UPDATE_STATUS,
			status,
			applicationId
		);
	}
}
