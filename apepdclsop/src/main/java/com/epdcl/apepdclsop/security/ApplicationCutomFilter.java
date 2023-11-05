package com.epdcl.apepdclsop.security;

import org.apache.logging.log4j.core.config.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Component
@Order(value = 1)
public class ApplicationCutomFilter implements Filter {

	private final Logger logger = LogManager.getLogger(this.toString());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("########## Initiating Custom filter ##########");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		XSSRequestWrapper requestWrapper = new 
				XSSRequestWrapper(req);
		 HttpServletResponse res = (HttpServletResponse) response;
		//String remote_addr = request.getRemoteAddr();
		//requestWrapper.addHeader("remote_addr", remote_addr);
		// Goes to default servlet
		 // Protection against Type 1 Reflected XSS attacks
		    res.addHeader("X-XSS-Protection", "1; mode=block");
		    // Disabling browsers to perform risky mime sniffing
		    res.addHeader("X-Content-Type-Options", "nosniff");
			 res.addHeader("X-Frame-Options", "SAMEORIGIN"); 
			 // res.addHeader("X-Frame-Options", "DENY"); 
		chain.doFilter(requestWrapper, response);
	}

	@Override
	public void destroy() {
		logger.info("########## Destroy Custom filter ##########");
	}
}
