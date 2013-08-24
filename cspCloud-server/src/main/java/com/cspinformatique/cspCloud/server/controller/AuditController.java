package com.cspinformatique.cspCloud.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cspinformatique.cspCloud.server.entity.Audit;
import com.cspinformatique.cspCloud.server.service.AuditService;

@Controller
@RequestMapping("/audits")
public class AuditController {
	private static final int RESULT_PER_PAGE = 20;
	
	@Autowired
	private AuditService auditService;
	
	@RequestMapping(method=RequestMethod.POST)
	public String search(
		Model model, 
		@RequestParam("begin") Integer begin,
		@RequestParam("end") Integer end,
		@RequestParam("orderBy") String orderBy,
		@RequestParam("asc") Boolean asc,
		@ModelAttribute("audit") Audit audit,
		BindingResult bindingResult
	){
		if(end == 0) end = begin + 20;
		if(orderBy == null) orderBy = "timestamp";
		if(asc == null) asc = false;
		
		model.addAttribute("audit", audit);
		model.addAttribute("audits", this.auditService.getAudits(audit, begin, end, orderBy, asc));
		
		int searchCount = this.auditService.getAuditsCount(audit);
		model.addAttribute("searchCount", searchCount);
		
		if(end + RESULT_PER_PAGE >= searchCount){
			end = searchCount;
		}
		
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("asc", asc);
		
		return "audit/view";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getAudits(
		Model model,
		Audit audit,
		BindingResult bindingResult
	){
		return this.search(model, 0, 20, null, null, audit, bindingResult);
	}
}
