package edu.byu.mtc.api.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName="accessControlFilter",
	displayName="Access Control Filter",
	description="Provides headers necissary for x-domain ajax calls",
	value="/*")
public class AccessControlFilter implements Filter {
	Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("Access Control Filter Initialized");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = ((HttpServletResponse)response);

		String origin = httpServletRequest.getHeader("Origin");
		String method = httpServletRequest.getHeader("Access-Control-Request-Method");
		String headers = httpServletRequest.getHeader("Access-Control-Request-Headers");

		if(origin != null)
			httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
		if(method != null)
			httpServletResponse.addHeader("Access-Control-Allow-Methods", method);
		if(headers != null)
			httpServletResponse.addHeader("Access-Control-Allow-Headers", headers);
		
		httpServletResponse.addHeader("Access-Control-Expose-Headers", "Location");

		chain.doFilter(request,response);
	}

	@Override
	public void destroy() {
		logger.info("Access Control Filter Destroyed");
	}
}
