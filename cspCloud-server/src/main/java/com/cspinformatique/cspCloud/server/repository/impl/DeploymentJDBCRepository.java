package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Deployment;
import com.cspinformatique.cspCloud.server.repository.ApplicationRepository;
import com.cspinformatique.cspCloud.server.repository.DeploymentRepository;

@Component
public class DeploymentJDBCRepository implements DeploymentRepository{
	private static final String SQL_DELETE_DEPLOYMENT =	"DELETE FROM " +
														"	deployments " +
														"WHERE " +
														"	appId = ?";
	
	private static final String SQL_INSERT_DEPLOYMENT =	"INSERT INTO deployments (" +
														"	appId, " +
														"	packageUrl, " +
														"	contextUrl, " +
														"	deploymentDate " +
														") VALUES (" +
														"	?,?,?,? " +
														")";
	
	private static final String SQL_SELECT_DEPLOYMENT =	"SELECT " +
														"	appId, " +
														"	packageUrl, " +
														"	contextUrl, " +
														"	deploymentDate " +
														"FROM " +
														"	deployments " +
														"WHERE " +
														"	id = ?";
	
	private static final String SQL_SELECT_LAST_DEPLOYMENT =	"SELECT " +
																"	* " +
																"FROM (" +
																"	SELECT " +
																"		id, " +
																"		packageUrl, " +
																"		contextUrl, " +
																"		deploymentDate " +
																"	FROM " +
																"		deployments " +
																"	WHERE " +
																"		appId = ? " +
																"	ORDER BY " +
																"		deploymentDate DESC " +
																") as t1 " +
																"LIMIT 1";
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Override
	public void createDeployment(Deployment deployment){
		this.jdbcTemplate.update(
			SQL_INSERT_DEPLOYMENT,
			deployment.getApplication().getId(),
			deployment.getPackageUrl(),
			deployment.getContextUrl(),
			deployment.getDate()
		);				
	}
	
	@Override
	public void deleteDeployments(int applicationId){
		this.jdbcTemplate.update(SQL_DELETE_DEPLOYMENT, applicationId);
	}
	
	@Override
	public Deployment getDeployment(final int id){
		try{
			return this.jdbcTemplate.queryForObject(
				SQL_SELECT_DEPLOYMENT, 
				new RowMapper<Deployment>(){
					@Override
					public Deployment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return new Deployment(
							id, 
							applicationRepository.getApplicationById(resultSet.getInt("appId")), 
							resultSet.getString("packageUrl"), 
							resultSet.getString("contextUrl"),
							new Date(resultSet.getTimestamp("deploymentDate").getTime())
						);
					}}, 
				id
			);
		}catch(EmptyResultDataAccessException emptyResultDataAccessEx){
			return null;
		}
	}
	
	@Override
	public Deployment getLastDeployment(final int applicationId){
		try{
			return this.jdbcTemplate.queryForObject(
				SQL_SELECT_LAST_DEPLOYMENT, 
				new RowMapper<Deployment>(){
					@Override
					public Deployment mapRow(ResultSet resultSet, int rowNum) throws SQLException {
						return new Deployment(
							resultSet.getInt("id"), 
							applicationRepository.getApplicationById(applicationId), 
							resultSet.getString("packageUrl"), 
							resultSet.getString("contextUrl"),
							new Date(resultSet.getTimestamp("deploymentDate").getTime())
						);
					}}, 
				applicationId
			);
		}catch(EmptyResultDataAccessException emptyResultDataAcessEx){
			return null;
		}
	}
}
