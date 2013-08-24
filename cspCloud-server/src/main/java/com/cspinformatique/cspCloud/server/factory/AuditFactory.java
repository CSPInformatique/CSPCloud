package com.cspinformatique.cspCloud.server.factory;

import java.util.Date;

import com.cspinformatique.cspCloud.server.entity.Audit;

public abstract class AuditFactory {
	public static Audit newAudit(String message, String type){
		return new Audit(0, message, new Date(), type);
	}
}
