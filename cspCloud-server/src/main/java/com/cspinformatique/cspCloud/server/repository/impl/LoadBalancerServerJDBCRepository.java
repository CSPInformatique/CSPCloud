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
import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancerServer;
import com.cspinformatique.cspCloud.server.repository.ApplicationRepository;
import com.cspinformatique.cspCloud.server.repository.CSPCloudServerRepository;
import com.cspinformatique.cspCloud.server.repository.InstanceRepository;
import com.cspinformatique.cspCloud.server.repository.LoadBalancerRepository;
import com.cspinformatique.cspCloud.server.repository.LoadBalancerServerRepository;
import com.cspinformatique.cspCloud.server.repository.ServerRepository;
import com.cspinformatique.cspCloud.server.service.LoadBalancerServerService;

@Component
public class LoadBalancerServerJDBCRepository implements LoadBalancerServerRepository {
	private static final String SQL_DELETE_LBSERVERINSTANCE =	"DELETE FROM " +
																"	lbServersInstances " +
																"WHERE " +
																"	lbId = ? AND " +
																"	daCloudServerId = ? AND" +
																"	appId = ? AND " +
																"	serverId = ?";
	
	private static final String SQL_DELETE_LOADBALANCERSERVERS =	"DELETE FROM " +
																	"	loadBalancersServers " +
																	"WHERE " +
																	"	daCloudServerId = ? AND " +
																	"	lbId = ?";
	
	private static final String SQL_INSERT_LBSERVERINSTANCE =	"INSERT INTO lbServersInstances ( " +
																"	lbId, " +
																"	daCloudServerId, " +
																"	appId, " +
																"	serverId " +
																") VALUES ( " +
																"	?,?,?,?" +
																")";
	
	private static final String SQL_INSERT_LOADBALANCERSERVER =	"INSERT INTO loadBalancersServers (" +
																"	lbId, " +
																"	daCloudServerId, " +
																"	port " +
																") VALUES ( " +
																"	?,?,?" +
																")";
	
	private static final String SQL_SELECT_APP_LB_SERVERS =	"SELECT " +
															"	loadbalancersservers.lbId, " +
															"	loadbalancersservers.daCloudServerId, " +
															"	loadbalancersservers.port " +
															"FROM " +
															"	loadbalancersservers, " +
															"	loadbalancers " +
															"WHERE " +
															"	loadbalancers.id = " +
															"		loadbalancersservers.lbId AND " +
															"	loadbalancers.applicationId = ?";
	
	private static final String SQL_SELECT_LOADBALANCERSERVER =	"SELECT " +
																"	port " +
																"FROM " +
																"	loadbalancersservers " +
																"WHERE " +
																"	lbId = ? AND " +
																"	daCloudServerId = ?";
	
	private static final String SQL_SELECT_LOADBALANCERSERVERS =	"SELECT" +
																	"	lbId, " +
																	"	daCloudServerId " +
																	"FROM " +
																	"	loadbalancersservers ";
	
	private static final String SQL_SELECT_LBS_FOR_SERVER =	"SELECT" +
															"	lbId " +
															"FROM " +
															"	loadbalancersservers " +
															"WHERE " +
															"	daCloudServerId = ? ";
	
	private static final String SQL_SELECT_MISSING_SERVERS =	"SELECT " +
																"	loadBalancers.id 'lbId', " +
																"	daCloudServers.id " +
																"		'daCloudServerId' " +
																"FROM " +
																"	loadBalancers, " +
																"	daCloudServers " +
																"WHERE " +
																"	CONCAT_WS(" +
																"		'|'," +
																"		loadBalancers.id, " +
																"		daCloudServers.id" +
																"	) NOT IN ( " +
																"		SELECT " +
																"			CONCAT_WS(" +
																"				'|', " +
																"				lbId, " +
																"				daCloudServerId" +
																"			) " +
																"		FROM " +
																"			loadBalancersServers" +
																"	)";
	
	private static final String SQL_SELECT_MISSING_INSTANCES =	"SELECT " +
																"	appId, " +
																"	serverId " +
																"FROM " +
																"	loadBalancedInstances " +
																"WHERE " +
																" 	lbId = ? AND " +
																"	CONCAT_WS( " +
																"		'|', " +
																"		appId, " +
																"		serverId " +
																"	) NOT IN ( " +
																"		SELECT " +
																"			CONCAT_WS(" +
																"				'|', " +
																"				appId, " +
																"				serverId " +
																"			) " +
																"		FROM " +
																"			lbServersInstances " +
																"		WHERE " +
																"			lbId = " +
																"				lbServersInstances." +
																"					lbId " +
																"				AND " +
																"			daCloudServerId = ?" +
																"	)";
	
	static final Logger logger = Logger.getLogger(LoadBalancerServerJDBCRepository.class);
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private CSPCloudServerRepository daCloudServerRepository;
	
	@Autowired
	private InstanceRepository instanceRepository;
	
	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private LoadBalancerRepository loadBalancerRepository;
	
	@Autowired
	private LoadBalancerServerService loadBalancerServerService;
	
	@Autowired
	private ServerRepository serverRepository;
	
	@Override
	public void addInstanceToLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	){
		this.jdbcTemplate.update(
			SQL_INSERT_LBSERVERINSTANCE,
			loadBalancerServer.getLoadBalancer().getId(),
			loadBalancerServer.getCSPCloudServer().getId(),
			instance.getApplication().getId(),
			instance.getServer().getId()
		);
	}
	
	@Override
	public void createLoadBalancerServer(LoadBalancerServer loadBalancerServer){
		this.jdbcTemplate.update(
			SQL_INSERT_LOADBALANCERSERVER,
			loadBalancerServer.getLoadBalancer().getId(),
			loadBalancerServer.getCSPCloudServer().getId(),
			loadBalancerServer.getPort()
		);
	}
	
	@Override
	public void deleteLoadBalancerServer(int daCloudServerId, int loadBalancerId){
		logger.debug("Deleting daCloudServerId : " + daCloudServerId + " loadBalancerId : " + loadBalancerId);
		
		int result = this.jdbcTemplate.update(SQL_DELETE_LOADBALANCERSERVERS, daCloudServerId, loadBalancerId);
		
		logger.debug(result + " rows deleted.");
	}
	
	@Override
	public List<LoadBalancerServer> getApplicationLoadBalancerServers(Application application){
		return this.jdbcTemplate.query(
			SQL_SELECT_APP_LB_SERVERS, 
			new RowMapper<LoadBalancerServer>(){
				@Override
				public LoadBalancerServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return new LoadBalancerServer(
						daCloudServerRepository.getServer(resultSet.getInt("daCloudServerId")), 
						loadBalancerRepository.getLoadBalancer(resultSet.getInt("lbId")), 
						resultSet.getInt("port")
					);
				}
			},
			application.getId()
		);
	}
	
	@Override
	public LoadBalancerServer getLoadBalancerServer(
		final int loadBalancerId, 
		final int daCloudServerId
	){
		return this.jdbcTemplate.queryForObject(
			SQL_SELECT_LOADBALANCERSERVER, 
			new RowMapper<LoadBalancerServer>(){
				@Override
				public LoadBalancerServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return new LoadBalancerServer( 
						daCloudServerRepository.getServer(daCloudServerId),
						loadBalancerRepository.getLoadBalancer(loadBalancerId),
						resultSet.getInt("port")
					);
				}
			},
			loadBalancerId,
			daCloudServerId
		);
	}
	
	public List<LoadBalancerServer> getLoadBalancerServers(){
		return this.jdbcTemplate.query(
			SQL_SELECT_LOADBALANCERSERVERS, 
			new RowMapper<LoadBalancerServer>(){
				@Override
				public LoadBalancerServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getLoadBalancerServer(
						resultSet.getInt("lbId"), 
						resultSet.getInt("daCloudServerId")
					);
				}
			}
		);
	}
	
	@Override
	public List<LoadBalancerServer> getLoadBalancerServers(final int daCloudServerId){
		return this.jdbcTemplate.query(
			SQL_SELECT_LBS_FOR_SERVER, 
			new RowMapper<LoadBalancerServer>(){
				@Override
				public LoadBalancerServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getLoadBalancerServer(
						resultSet.getInt("lbId"), 
						daCloudServerId
					);
				}
			},
			daCloudServerId
		);
	}
	
	@Override
	public List<LoadBalancerServer> getMissingLoadBalancerServer(){
		return this.jdbcTemplate.query(SQL_SELECT_MISSING_SERVERS, new RowMapper<LoadBalancerServer>(){
			@Override
			public LoadBalancerServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				CSPCloudServer server = daCloudServerRepository.getServer(resultSet.getInt("daCloudServerId"));
				
				return new LoadBalancerServer(
					server,
					loadBalancerRepository.getLoadBalancer(resultSet.getInt("lbId")),
					loadBalancerServerService.getNewPortFromServer(server)
				);
			}
		});
	}
	
	@Override
	public List<Instance> getMissingLoadBalancerServerInstances(LoadBalancerServer loadBalancerServer){
		return this.jdbcTemplate.query(
			SQL_SELECT_MISSING_INSTANCES,	
			new RowMapper<Instance>(){
				@Override
				public Instance mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return instanceRepository.getInstance(
						applicationRepository.getApplicationById(resultSet.getInt("appId")), 
						serverRepository.loadServer(resultSet.getInt("serverId"))
					);
				}
			},
			loadBalancerServer.getLoadBalancer().getId(),
			loadBalancerServer.getCSPCloudServer().getId()
		);
	}
	
	@Override
	public void removeInstanceFromLoadBalancerServer(
		LoadBalancerServer loadBalancerServer, 
		Instance instance
	){
		logger.debug("Deleting " + instance + " from " + loadBalancerServer);
		int result =	this.jdbcTemplate.update(
							SQL_DELETE_LBSERVERINSTANCE, 
							loadBalancerServer.getLoadBalancer().getId(),
							loadBalancerServer.getCSPCloudServer().getId(),
							instance.getApplication().getId(),
							instance.getServer().getId()
						);
		
		logger.debug(result + " rows deleted");
	}
}
