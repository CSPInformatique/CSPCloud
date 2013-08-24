package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.commons.factory.InstanceFactory;
import com.cspinformatique.cspCloud.server.repository.ApplicationRepository;
import com.cspinformatique.cspCloud.server.repository.DeploymentRepository;
import com.cspinformatique.cspCloud.server.repository.InstanceRepository;
import com.cspinformatique.cspCloud.server.repository.ServerPortRepository;
import com.cspinformatique.cspCloud.server.repository.ServerRepository;

@Component
public class InstanceJDBCRepository implements InstanceRepository {
	static final Logger logger = Logger.getLogger(InstanceJDBCRepository.class);
	
	private static final String SQL_APPLICATION_INSTANCES =	"SELECT " +
														"	serverId, " +
														"	status, " +
														"	lastDeployment " +
														"FROM " +
														"	instances " +
														"WHERE " +
														"	appId = ?";

	private final String SQL_APPS_MISSING_INSTANCES =	"SELECT " +
														"	id "+
														"FROM " +
														"	applications " +
														"WHERE " +
														"	numberOfInstances > ( " +
														"		SELECT " +
														"			count(*) " +
														"		FROM " +
														"			instances " +
														"		WHERE " +
														"			appId = applications.id " +
														"	) ";
	
	private final String SQL_DELETE_INSTANCE =	"DELETE FROM " +
												"	instances " +
												"WHERE " +
												"	appId = ? and " +
												"	serverId = ?";
	
	private static final String SQL_INSERT_QUERY =	"INSERT INTO instances ( " +
													"	serverId, " +
													"	appId, " +
													"	status " +
													") VALUES ( " +
													"	?,?,? " +
													") ";
	
	private final String SQL_SERVER_INSTANCES =	"SELECT " +
												"	appId " +
												"FROM " +
												"	instances " +
												"WHERE " +
												"	serverId = ? ";
	
	private final String SQL_SELECT_INSTANCE =	"SELECT " +
												"	status," +
												"	lastDeployment " +
												"FROM " +
												"	instances " +
												"WHERE " +
												"	serverId = ? AND " +
												"	appId = ?";
	
	private final String SQL_SELECT_INSTANCES =	"SELECT " +
												"	serverId, " +
												"	appId " +
												"FROM " +
												"	instances";
	
	private final String SQL_UPDATE_DEPLOYMENT =	"UPDATE " +
													"	instances " +
													"SET " +
													"	lastDeployment = ? " +
													"WHERE " +
													"	appId = ? AND " +
													"	serverId = ? ";

	private final String SQL_UPDATE_RUNNING_STATUS =	"UPDATE " +
														"	instances " +
														"SET " +
														"	status = ? " +
														"WHERE " +
														"	appId = ? AND " +
														"	serverId = ?";
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private DeploymentRepository deploymentRepository;
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServerRepository serverRepository;
	
	@Autowired
	private ServerPortRepository serverPortRepository;
	
	@Override
	public void createInstance(Instance instance) {
		this.jdbcTemplate.update(
			SQL_INSERT_QUERY, 
			instance.getServer().getId(), 
			instance.getApplication().getId(), 
			instance.getStatus()
		);
	}
	
	@Override
	public void deleteInstance(int applicationId, int serverId){
		logger.debug("Deleting instance - applicationId : " + applicationId + " serverId : " + serverId);
		this.jdbcTemplate.update(SQL_DELETE_INSTANCE, applicationId, serverId);
	}
	
	@Override
	public List<Instance> getApplicationInstances(final Application application){
		return this.jdbcTemplate.query(
			SQL_APPLICATION_INSTANCES, 
			new RowMapper<Instance>(){
				@Override
				public Instance mapRow(ResultSet resultSet, int rowNum) throws SQLException {					
					return getInstance(application, serverRepository.loadServer(resultSet.getInt("serverId")));
				}
			}, 
			application.getId()
		);
	}
	
	@Override
	public List<Application> getApplicationsMissingInstances(){
		return this.jdbcTemplate.query(
			SQL_APPS_MISSING_INSTANCES, 
			new RowMapper<Application>(){
				@Override
				public Application mapRow(ResultSet resultSet, int rowNum) throws SQLException{
					return applicationRepository.getApplicationById(resultSet.getInt("id"));
				}
			}
		);
	}
	
	@Override
	public Instance getInstance(final Application application, final Server server){
		return this.jdbcTemplate.queryForObject(
			SQL_SELECT_INSTANCE, 
			new RowMapper<Instance>(){
				@Override
				public Instance mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return InstanceFactory.getInstance(
						application, 
						server,
						deploymentRepository.getDeployment(resultSet.getInt("lastDeployment")),
						serverPortRepository.loadInstancePorts(server.getId(), application.getId()),
						resultSet.getString("status")
					);
				}
			}, 
			server.getId(),
			application.getId()
		);
	}
	
	@Override
	public List<com.cspinformatique.cspCloud.commons.entity.Instance> getInstances(){
		List<Instance> instances = this.jdbcTemplate.query(
			SQL_SELECT_INSTANCES, 
			new RowMapper<Instance>(){
				@Override
				public Instance mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getInstance(
						applicationRepository.getApplicationById(resultSet.getInt("appId")), 
						serverRepository.loadServer(resultSet.getInt("serverId"))
					);
				}
			}
		);
		
		logger.debug(instances.size() + " instances found.");
		
		return instances;
	}
	
	@Override
	public List<Integer> getServerApplications(int serverId){
		return this.jdbcTemplate.query(
			SQL_SERVER_INSTANCES, 
			new RowMapper<Integer>(){
				@Override
				public Integer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return Integer.valueOf(resultSet.getInt("appId"));
				}
			}, 
			serverId
		);
	}
	
	@Override
	public void updateLastDeployment(Instance instance, int deploymentId) {
		this.jdbcTemplate.update(
			SQL_UPDATE_DEPLOYMENT,
			deploymentId,
			instance.getApplication().getId(),
			instance.getServer().getId()
		);
	};
	
	@Override
	public void updateRunningStatus(Instance instance){
		this.jdbcTemplate.update(
			SQL_UPDATE_RUNNING_STATUS, 
			instance.getStatus(), 
			instance.getApplication().getId(), 
			instance.getServer().getId()
		);
	}
}
