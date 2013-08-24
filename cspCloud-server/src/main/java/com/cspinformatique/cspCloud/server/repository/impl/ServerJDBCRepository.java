package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Server;
import com.cspinformatique.cspCloud.server.repository.ProcessRepository;
import com.cspinformatique.cspCloud.server.repository.ServerRepository;

@Component
public class ServerJDBCRepository implements ServerRepository {
	private static final String SQL_ACTIVATE_SERVER =	"UPDATE " +
														"	servers " +
														"SET " +
														"	status = ? " +
														"WHERE " +
														"	id = ?";
			
	
	private static final String SQL_DELETE_SERVER =	"DELETE FROM " +
													"	servers " +
													"WHERE " +
													"	id = ?";
	
	private static final String SQL_INSERT =	"INSERT INTO SERVERS (" +
												"	hostname, " +
												"	ip, " +
												"	adminPort, " +
												"	adminContext," +
												"	status " +
												") VALUES (" +
												"	?,?,?,?,?" +
												") ";
	
	private static final String SQL_SELECT_SERVER =	"SELECT " +
														"	id," +
														"	hostname," +
														"	ip," +
														"	adminPort," +
														"	adminContext," +
														"	status " +
														"FROM " +
														"	servers " +
														"WHERE" +
														"	id = ? ";
	
	private static final String SQL_SELECT_SERVERS =	"SELECT " +
														"	id " +
														"FROM " +
														"	servers ";
	
	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ProcessRepository processRepository;
	
	public void createServer(Server server){
		this.jdbcTemplate.update(
			SQL_INSERT, 
			server.getHostname(), 
			server.getIpAddress(), 
			server.getServerPort(), 
			server.getAdminContext(),
			server.getStatus()
		);
	}
	
	@Override
	public void activateServer(Server server){
		this.jdbcTemplate.update(SQL_ACTIVATE_SERVER, Server.STATUS_ACTIVE, server.getId());
	}
	
	@Override
	public void deleteServer(Server server){
		this.jdbcTemplate.update(SQL_DELETE_SERVER, server.getId());
	}
	
	@Override
	public Server loadServer(final int serverId){
		return this.jdbcTemplate.queryForObject(SQL_SELECT_SERVER, new RowMapper<Server>(){
			@Override
			public Server mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Server server = new Server();
				
				server.setId(resultSet.getInt("id"));
				server.setHostname(resultSet.getString("hostname"));
				server.setIpAddress(resultSet.getString("ip"));
				server.setServerPort(resultSet.getInt("adminPort"));
				server.setAdminContext(resultSet.getString("adminContext"));
				server.setProcesses(processRepository.getProcesses(server.getHostname()));
				server.setStatus(resultSet.getString("status"));
				
				return server;
			}
			
		},
		serverId);
	}
	
	@Override
	public List<Integer> loadServers() {
		return this.jdbcTemplate.query(
			SQL_SELECT_SERVERS, 
			new RowMapper<Integer>(){			
				@Override
				public Integer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return Integer.valueOf(resultSet.getInt("id"));
				}
			}
		);
	}

}
