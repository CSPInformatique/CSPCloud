package com.cspinformatique.cspCloud.server.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.commons.entity.ApplicationKit;
import com.cspinformatique.cspCloud.commons.factory.ApplicationKitFactory;
import com.cspinformatique.cspCloud.server.repository.ApplicationKitRepository;

@Component
public class ApplicationKitJDBCRepository implements ApplicationKitRepository{
	private final String SQL_SELECT_APPLICATION_KITS =	"SELECT " +
														"	name," +
														"	type," +
														"	path " +
														"FROM " +
														"	applicationKits";
	
	private final String SQL_SELECT_APPLICATION_KIT =	"SELECT " +
														"	name," +
														"	type," +
														"	path " +
														"FROM " +
														"	applicationKits " +
														"WHERE" +
														"	name = ? ";
	
	private final String SQL_SELECT_APP_ID =	"SELECT " +
												"	applicationKits.name," +
												"	applicationKits.type," +
												"	applicationKits.path " +
												"FROM " +
												"	applicationKits," +
												"	applications " +
												"WHERE " +
												"	applicationKits.name = applications.applicationKit AND " +
												"	applications.id = ? ";
	
	@Autowired protected JdbcTemplate jdbcTemplate;
	
	@Override
	public ApplicationKit getApplicationKit(int applicationId){
		return this.jdbcTemplate.queryForObject(
			this.SQL_SELECT_APP_ID, 
			new RowMapper<ApplicationKit>(){
				@Override
				public ApplicationKit mapRow(ResultSet resultSet, int id) throws SQLException {
					return ApplicationKitFactory.getApplicationKit(
						resultSet.getString("name"), 
						resultSet.getString("path"), 
						resultSet.getString("type")
					);
				}
			}, 
			applicationId
		);
	}
	
	@Override
	public ApplicationKit getApplicationKitByName(String name) {
		return this.jdbcTemplate.queryForObject(
			this.SQL_SELECT_APPLICATION_KIT, 
			new RowMapper<ApplicationKit>(){
				@Override
				public ApplicationKit mapRow(ResultSet resultSet, int id) throws SQLException {
					return ApplicationKitFactory.getApplicationKit(
						resultSet.getString("name"), 
						resultSet.getString("path"), 
						resultSet.getString("type")
					);
				}
			}, 
			name
		);
	}
	
	@Override
	public List<ApplicationKit> getApplicationKits() {
		List<ApplicationKit> applicationkits = new ArrayList<ApplicationKit>();
		 
		List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(this.SQL_SELECT_APPLICATION_KITS);
		for (Map<String, Object> row : rows) {			
			applicationkits.add(ApplicationKitFactory.getApplicationKit(
				(String)row.get("name"), 
				(String)row.get("path"), 
				(String)row.get("type")
			));
		}
	 
		return applicationkits;
	}

}
