package com.cspinformatique.cspCloud.commons.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface CSPCRestTemplate {
	public ResponseEntity<?> exchange(
		String url, 
		HttpMethod method, 
		HttpEntity<?> requestEntity, 
		Class<?> responseType
	);
}
