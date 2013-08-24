package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Cluster;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancer;
import com.cspinformatique.cspCloud.server.repository.ApplicationRepository;
import com.cspinformatique.cspCloud.server.repository.InstanceRepository;
import com.cspinformatique.cspCloud.server.repository.LoadBalancerRepository;
import com.cspinformatique.cspCloud.server.repository.ServerRepository;

@Component
public class LoadBalancerJDBCRepository implements LoadBalancerRepository {
	private static final String SQL_ADD_APP_TO_LOADBALANCER =	"INSERT INTO loadBalancedInstances (" +
																"	lbId, " +
																"	appId, " +
																"	serverId " +
																") VALUES ( " +
																"	?,?,? " +
																")";
	
	private static final String SQL_DELETE_LB_INSTANCES =	"DELETE FROM " +
															"	loadBalancedInstances " +
															"WHERE " +
															"	appId = ? AND" +
															"	serverId = ?";
	
	private static final String SQL_FIND_ID =	"SELECT " +
												"	id " +
												"FROM " +
												"	loadBalancers " +
												"WHERE " +
												"	creationDate = ?";
	
	private static final String SQL_INSERT_LOADBALANCER =	"INSERT INTO loadBalancers (" +
															"	applicationId, " +
															"	type, " +
															"	creationDate " +
															") VALUES ( " +
															"	?,?,? " +
															")";
	
	private static final String SQL_SELECT_APP_LOADBALANCER =	"SELECT " +
																"	id " +
																"FROM " +
																"	loadBalancers " +
																"WHERE " +
																"	applicationId  = ?";
	
	private static final String SQL_SELECT_LB_INSTANCES =	"SELECT " +
															"	appId," +
															"	serverId " +
															"FROM " +
															"	loadbalancedinstances " +
															"WHERE " +
															"	lbId = ?";
	
	private static final String SQL_SELECT_LOADBALANCER =	"SELECT " +
															"	applicationId, " +
															"	type, " +
															"	creationDate " +
															"FROM " +
															"	loadbalancers " +
															"WHERE " +
															"	id = ?";
	
	private static final String SQL_SELECT_LOADBALANCERS =	"SELECT " +
															"	id " +
															"	type, " +
															"	creationDate " +
															"FROM " +
															"	loadbalancers ";
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private InstanceRepository instanceRepository;
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServerRepository serverRepository;
	
	@Override
	public void addInstanceToLoadBalancer(LoadBalancer loadBalancer, Instance instance){
		this.jdbcTemplate.update(SQL_ADD_APP_TO_LOADBALANCER,
			loadBalancer.getId(),
			instance.getApplication().getId(),
			instance.getServer().getId()
		);
	}
	
	@Override
	public void createLoadBalancer(LoadBalancer loadBalancer) {
		Date creationDate = new Date();
		this.jdbcTemplate.update(
			SQL_INSERT_LOADBALANCER, 
			loadBalancer.getApplication().getId(), 
			loadBalancer.getType(),
			creationDate
		);
		
		loadBalancer.setId(this.jdbcTemplate.queryForObject(SQL_FIND_ID, Integer.class, creationDate));
	}
	
	@Override
	public void deleteLoadBalancedInstance(Instance instance){
		this.jdbcTemplate.update(
			SQL_DELETE_LB_INSTANCES, 
			instance.getApplication().getId(), 
			instance.getServer().getId()
		);
	}
	
	@Override
	public LoadBalancer getApplicationLoadBalancer(Application application){
		return this.jdbcTemplate.queryForObject(
			SQL_SELECT_APP_LOADBALANCER, 
			new RowMapper<LoadBalancer>(){
				@Override
				public LoadBalancer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getLoadBalancer(resultSet.getInt("id"));
				}
			},
			application.getId()
		);
	}
	
	@Override
	public LoadBalancer getLoadBalancer(final int id){
		return this.jdbcTemplate.queryForObject(
			SQL_SELECT_LOADBALANCER, 
			new RowMapper<LoadBalancer>(){
				@Override
				public LoadBalancer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return new LoadBalancer(
						applicationRepository.getApplicationById(resultSet.getInt("applicationId")), 
						getLoadBalancedClusters(id), 
						id, 
						getLoadBalancedInstances(id), 
						resultSet.getString("type")
					);
				}		
			},
			id
		);
	}
	
	@Override 
	public List<LoadBalancer> getLoadBalancers(){
		return this.jdbcTemplate.query(
			SQL_SELECT_LOADBALANCERS, 
			new RowMapper<LoadBalancer>(){
				@Override
				public LoadBalancer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getLoadBalancer(resultSet.getInt("id"));
				}
			}
		);
	}
	
	private List<Cluster> getLoadBalancedClusters(int loadBalancerId){
		return new ArrayList<Cluster>();
	}
	
	private List<Instance> getLoadBalancedInstances(int loadBalancerId){
		return this.jdbcTemplate.query(
			SQL_SELECT_LB_INSTANCES,
			new RowMapper<Instance>(){
				@Override
				public Instance mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return instanceRepository.getInstance(
						applicationRepository.getApplicationById(resultSet.getInt("appId")), 
						serverRepository.loadServer(resultSet.getInt("serverId"))
					);
				}	
			},
			loadBalancerId
		);
	}
}
