package com.cspinformatique.cspCloud.server.repository;

import java.util.Date;
import java.util.List;

import com.cspinformatique.cspCloud.server.entity.Audit;

public interface AuditRepository {
	public void createAudit(Audit audit);
	
	public List<Audit> getAudits(
		Audit audit, 
		Date minDate, 
		Date maxDate, 
		int begin, 
		int end, 
		String orderBy, 
		boolean asc
	);
	
	public int getAuditsCount(Audit audit);
}
