package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.server.entity.Audit;
import com.cspinformatique.cspCloud.server.repository.AuditRepository;

@Component
public class AuditJDBCRepository implements AuditRepository {
	static final Logger logger = Logger.getLogger(AuditJDBCRepository.class);
	
	private static final String SQL_INSERT_AUDIT =	"INSERT INTO AUDITS (" +
													"	message, " +
													"	timestamp, " +
													"	type " +
													") VALUES ( " +
													"	?,?,? " +
													")";
	
	private static final String SQL_SELECT_AUDITS =	"SELECT " +
													"	id, " +
													"	message, " +
													"	timestamp, " +
													"	type " +
													"FROM " +
													"	AUDITS " +
													"{where} " +
													"ORDER BY " +
													"	{orderBy} " +
													"LIMIT ?, ?";
	
	private static final String SQL_SELECT_AUDITS_COUNT =	"SELECT " +
															"	count(*) " +
															"FROM " +
															"	AUDITS " +
															"{where}";
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Override
	public void createAudit(Audit audit) {
		this.jdbcTemplate.update(
			SQL_INSERT_AUDIT, 
			audit.getMessage(), 
			audit.getTimestamp(), 
			audit.getType()
		);
	}
	
	private void addWhereCondition(StringBuffer whereClause, String condition){
		if(!whereClause.toString().equals("")){
			whereClause.append("AND ");
		}else{
			whereClause.append("WHERE ");
		}
		whereClause.append(condition);
	}
	
	private void getSearchWhereClause(
		Audit audit,  
		StringBuffer whereClause, 
		ArrayList<Object> parameters
	){
		this.getSearchWhereClause(audit, null, null, whereClause, parameters);
	}
	
	private void getSearchWhereClause(
		Audit audit, 
		Date minDate, 
		Date maxDate, 
		StringBuffer whereClause, 
		ArrayList<Object> parameters
	){
		
		if(audit.getType() != null && !audit.getType().equals("")){
			this.addWhereCondition(whereClause, "type = ? ");
			parameters.add(audit.getType());
		}
		
		if(audit.getMessage() != null && !audit.getMessage().trim().equals("")){
			this.addWhereCondition(whereClause, "message like ?");
			parameters.add("%" + audit.getMessage() + "%");
		}	
		
		if(minDate != null){
			this.addWhereCondition(whereClause, "timestamp > ?");
			parameters.add(minDate);
		}
		
		if(maxDate != null){
			this.addWhereCondition(whereClause, "timestamp < ?");
			parameters.add(maxDate);
		}
	}

	@Override
	public List<Audit> getAudits(
		Audit audit, 
		Date minDate, 
		Date maxDate, 
		int begin, 
		int end, 
		String orderBy, 
		boolean asc
	) {
		ArrayList<Object> parameters = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		
		this.getSearchWhereClause(audit, minDate, maxDate, whereClause, parameters);
		
		String orderType = "ASC";
		if(!asc){
			orderType = "DESC";
		}

		parameters.add(begin);
		parameters.add(end - begin);
		
		return this.jdbcTemplate.query(
			SQL_SELECT_AUDITS.replace(
				"{where}", 
				whereClause.toString()
			).replace(
				"{orderBy}",
				orderBy + " " + orderType
			),
			parameters.toArray(),
			new RowMapper<Audit>(){
				@Override
				public Audit mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					return new Audit(
						resultSet.getLong("id"),
						resultSet.getString("message"),
						new Date(resultSet.getTimestamp("timestamp").getTime()),
						resultSet.getString("type")
					);
				}
			}
		);
	}
	
	public int getAuditsCount(Audit audit){
		ArrayList<Object> parameters = new ArrayList<Object>();
		StringBuffer whereClause = new StringBuffer();
		
		this.getSearchWhereClause(audit, whereClause, parameters);
		
		return this.jdbcTemplate.queryForObject(
			SQL_SELECT_AUDITS_COUNT.replace("{where}", whereClause.toString()),
			Integer.class,
			parameters.toArray()
		);
	}

}
