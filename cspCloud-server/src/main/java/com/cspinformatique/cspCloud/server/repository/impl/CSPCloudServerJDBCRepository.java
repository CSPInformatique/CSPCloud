package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.CSPCloudServer;
import com.cspinformatique.cspCloud.server.repository.CSPCloudServerRepository;

@Component
public class CSPCloudServerJDBCRepository implements CSPCloudServerRepository {
	private final String SQL_DELETE_SERVER =	"DELETE FROM " +
												"	CSPCloudServers " +
												"WHERE " +
												"	id = ?";
	
	private final String SQL_SELECT_SERVER =	"SELECT " +
												"	id, " +
												"	hostname, " +
												"	ip, " +
												"	port," +
												"	adminContext " +
												"FROM " +
												"		CSPCloudServers " +
												"WHERE " +
												"	id = ?";
	
	private final String SQL_SELECT_SERVERS =	"SELECT " +
												"	id " +
												"FROM " +
												"	CSPCloudServers";
	
	private final String SQL_INSERT_SERVER =	"INSERT INTO CSPCloudServers ( " +
												"	hostname," +
												"	ip," +
												"	port," +
												"	adminContext " +
												") VALUES ( " +
												"	?,?,?,? " +
												") ";
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Override
	public CSPCloudServer getServer(final int id){
		return this.jdbcTemplate.queryForObject(
			SQL_SELECT_SERVER, 
			new RowMapper<CSPCloudServer>(){
				@Override
				public CSPCloudServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return new CSPCloudServer(
						resultSet.getString("adminContext"), 
						resultSet.getInt("id"), 
						resultSet.getString("ip"), 
						resultSet.getString("hostname"), 
						resultSet.getInt("port")
					);
				}	
			},
			id
		);
	}
	
	@Override
	public List<CSPCloudServer> getServers() {
		return this.jdbcTemplate.query(
			SQL_SELECT_SERVERS, 
			new RowMapper<CSPCloudServer>(){
				@Override
				public CSPCloudServer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return getServer(resultSet.getInt("id"));
				}	
			}
		);
	}

	@Override
	public void registerServer(CSPCloudServer CSPCloudServer) {
		this.jdbcTemplate.update(
			SQL_INSERT_SERVER,
			CSPCloudServer.getHostname(),
			CSPCloudServer.getIp(),
			CSPCloudServer.getPort(),
			CSPCloudServer.getAdminContext()
		);
	}
	
	@Override
	public void unregisterServer(int serverId){
		this.jdbcTemplate.update(SQL_DELETE_SERVER, serverId);
	}
}
