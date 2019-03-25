package com.tng.portal.ana.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.tng.portal.common.util.JsonUtils;
import com.tng.portal.common.vo.rest.RestfulResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Zero on 2016/11/22.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ERROR_MESSAGE = "Your Access is denied by the System!";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
    	RestfulResponse<Object> res = new RestfulResponse<>();
    	res.setErrorCode("403");
    	res.setMessageEN(ERROR_MESSAGE);
    	res.setMessageZhCN(ERROR_MESSAGE);
    	res.setMessageZhHK(ERROR_MESSAGE);
    	
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Type", "application/json");
        PrintWriter writer = response.getWriter();
        writer.println(JsonUtils.toJSon(res));
        writer.flush();
        writer.close();
    }
}
