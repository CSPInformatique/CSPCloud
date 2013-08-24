package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.Process;
import com.cspinformatique.cspCloud.server.repository.ProcessRepository;

@Component
public class ProcessJDBCRepository implements ProcessRepository {
	private static final String SQL_INSERT_PROCESS =	"INSERT INTO processes (" +
														"	id, serverUrl " +
														") VALUES ( " +
														"	?,? " +
														")";
	
	private static final String SQL_DELETE_PROCESS =	"DELETE FROM " +
														"	processes " +
														"WHERE " +
														"	id = ? AND " +
														"	serverUrl = ?";
	
	private static final String SQL_SELECT_PROCESSES =	"SELECT " + 
														"	id " +
														"FROM " +
														"	processes " +
														"WHERE " +
														"	serverUrl = ?";
	
	@Autowired private JdbcTemplate jdbcTemplate;
			
	@Override
	public void createProcess(Process process) {
		this.jdbcTemplate.update(SQL_INSERT_PROCESS, process.getId(), process.getServer().getServerUrl());
	}

	@Override
	public void deleteProcess(Process process) {
		this.jdbcTemplate.update(SQL_DELETE_PROCESS, process.getId(), process.getServer().getServerUrl());	
	}
	
	public List<Long> getProcesses(String serverUrl){
		return this.jdbcTemplate.query(SQL_SELECT_PROCESSES, new RowMapper<Long>(){
			@Override
			public Long mapRow(ResultSet resultSet, int rowNum) throws SQLException{
				return resultSet.getLong("id");
			}
		},
		serverUrl);
	}

}
