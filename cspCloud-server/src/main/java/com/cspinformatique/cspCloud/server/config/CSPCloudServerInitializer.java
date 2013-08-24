package com.cspinformatique.cspCloud.server.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class CSPCloudServerInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
	    AnnotationConfigWebApplicationContext ctx =  new AnnotationConfigWebApplicationContext();
	    
	    ctx.register(WebappConfig.class);;
	 
	    // Add the servlet mapping manually and make it initialize automatically
	    Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
	    servlet.addMapping("/");
	    servlet.setLoadOnStartup(1);
	}

}
