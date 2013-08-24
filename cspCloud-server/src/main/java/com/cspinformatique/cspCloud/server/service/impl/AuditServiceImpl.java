package com.cspinformatique.cspCloud.server.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cspinformatique.cspCloud.server.entity.Audit;
import com.cspinformatique.cspCloud.server.repository.AuditRepository;
import com.cspinformatique.cspCloud.server.service.AuditService;

@Component
public class AuditServiceImpl implements AuditService {
	@Autowired
	private AuditRepository auditRepository;
	
	@Override
	public void createAudit(Audit audit) {
		this.auditRepository.createAudit(audit);
	}

	@Override
	public List<Audit> getAudits(
		Audit audit, 
		int begin, 
		int end, 
		String orderBy, 
		boolean asc
	) {
		return this.getAudits(audit, null, null, begin, end, orderBy, asc);
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
		return this.auditRepository.getAudits(audit, minDate, maxDate, begin, end, orderBy, asc);
	}
	
	@Override
	public int getAuditsCount(){
		return this.auditRepository.getAuditsCount(new Audit());
	}
	
	@Override
	public int getAuditsCount(Audit audit){
		return this.auditRepository.getAuditsCount(audit);
	}

}
