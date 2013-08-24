package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.ServerPort;
import com.cspinformatique.cspCloud.commons.factory.ServerPortFactory;
import com.cspinformatique.cspCloud.server.repository.ServerPortRepository;

@Component
public class ServerPortJDBCRepository implements ServerPortRepository {
	private static final Logger logger = Logger.getLogger(ServerPortJDBCRepository.class);
	
	private static final String SQL_DELETE_PORT =	"DELETE FROM " +
													"	serverPorts " +
													"WHERE " +
													"	serverId = ? and " +
													"	port = ?";

	private static final String SQL_INSERT =	"INSERT INTO serverports ( " +
													"	serverId, " +
													"	port, " +
													"	type, " +
													"	appId " +
													") VALUES ( " +
													"	?,?,?,?" +
													") ";
	
	private static final String SQL_SELECT_INSTANCE_PORTS=	"SELECT " +
															"	port," +
															"	type " +
															"FROM " +
															"	serverports " +
															"WHERE " +
															"	serverId = ? AND " +
															"	appId = ?";
	
	private static final String SQL_SELECT_SERVER_PORTS=	"SELECT " +
															"	port," +
															"	type " +
															"FROM " +
															"	serverports " +
															"WHERE " +
															"	serverId = ?";
	
	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Override
	public void deletePort(int serverId, int port){
		this.jdbcTemplate.update(SQL_DELETE_PORT, serverId, port);
	}
	
	@Override
	public List<ServerPort> loadServerPorts(int serverId){
		return this.jdbcTemplate.query(
			SQL_SELECT_SERVER_PORTS, 
			new RowMapper<ServerPort>(){
				@Override
				public ServerPort mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return ServerPortFactory.getServerPor(
						resultSet.getInt("port"), 
						resultSet.getString("type")
					);
				}
			}, 
			serverId
		);
	}
	
	@Override
	public Map<Integer, ServerPort> loadInstancePorts(int serverId, int appId) {
		Map<Integer, ServerPort> ports = new HashMap<Integer, ServerPort>();
		 
		List<ServerPort> serverPorts = this.jdbcTemplate.query(
			SQL_SELECT_INSTANCE_PORTS, 
			new RowMapper<ServerPort>(){
				@Override
				public ServerPort mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return ServerPortFactory.getServerPor(
						resultSet.getInt("port"), 
						resultSet.getString("type")
					);
				}
			}, 
			serverId,
			appId
		);
		
		for(ServerPort serverPort : serverPorts){
			ports.put(serverPort.getPort(), serverPort);
		}
		
		logger.debug(ports.size() + " ports found.");
		
		return ports;
	}

	@Override
	public void save(ServerPort serverPort, Instance instance) {
		this.jdbcTemplate.update(
			SQL_INSERT, 
			instance.getServer().getId(), 
			serverPort.getPort(), 
			serverPort.getType(), 
			instance.getApplication().getId()
		);
	}
}
