package com.cspinformatique.cspCloud.server.aop.audit;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;

import com.cspinformatique.cspCloud.server.entity.Audit;
import com.cspinformatique.cspCloud.server.factory.AuditFactory;
import com.cspinformatique.cspCloud.server.params.Params;
import com.cspinformatique.cspCloud.server.service.AuditService;

public class AuditCreator {
	@Autowired
	protected AuditService auditService;
	
	@Autowired
	protected Params params;
	
	protected void createAudit(String message, String type){
		this.auditService.createAudit(AuditFactory.newAudit(message,  type));
		
		if(params.isEmailActive() && !type.equals(Audit.TYPE_INFO)){
			try{
				SimpleEmail email = new SimpleEmail();
				
				String subject = params.getServiceDomain() + " - ";
				if(type.equals(Audit.TYPE_ERROR)){
					subject += "ERROR";
				}else if(type.equals(Audit.TYPE_ALERT)){
					subject += "ALERT";
				}else{
					subject += "WARNING";
				}
				
				email.setHostName(params.getEmailSmtpServer());
//				email.setSSL(params.isEmailSsl());
				email.setAuthentication(params.getEmailUsername(), params.getEmailPassword());
				email.addTo(params.getEmailAlertsReceiver(), params.getServiceDomain());
				email.setFrom(params.getEmailSender(), params.getServiceDomain());
				
				email.setSubject(subject);
				email.setMsg(message);
				
				email.send();
			}catch(EmailException emailEx){
				throw new RuntimeException(emailEx);
			}
		}
	}
}
