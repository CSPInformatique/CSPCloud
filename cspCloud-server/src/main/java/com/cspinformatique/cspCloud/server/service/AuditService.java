package com.cspinformatique.cspCloud.server.service;

import java.util.Date;
import java.util.List;

import com.cspinformatique.cspCloud.server.entity.Audit;

public interface AuditService {
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
	
	public List<Audit> getAudits(
		Audit audit,
		int begin, 
		int end, 
		String orderBy, 
		boolean asc
	);
	
	public int getAuditsCount();
	
	public int getAuditsCount(Audit audit);
}
