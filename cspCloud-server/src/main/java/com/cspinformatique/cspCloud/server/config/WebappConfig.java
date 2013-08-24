package com.cspinformatique.cspCloud.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan("com.cspinformatique.cspCloud.server")
public class WebappConfig {
	@Bean
	public InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		
		resolver.setPrefix("/WEB-INF/views/");
	    resolver.setSuffix(".html");
	 
	    return resolver;
	}
}
