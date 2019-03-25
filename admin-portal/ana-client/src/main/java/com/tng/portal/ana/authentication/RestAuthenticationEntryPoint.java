package com.tng.portal.ana.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String message = "{\"status\":{\"code\":401,\"message\":\"You Are Not Unauthorized to Access the System!\"}}";
	private String contentType = "application/json";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException
	{
		if(request.getMethod() != null && request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString())){
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS));
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD));
		}else{
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("Content-Type", contentType);
			PrintWriter writer = response.getWriter();
			writer.println(message);
			writer.flush();
			writer.close();
		}
	}

}
