package com.tng.portal.ana.filter;

import com.tng.portal.ana.entity.AnaModuleSecurity;
import com.tng.portal.ana.service.AnaModuleSecurityService;
import com.tng.portal.common.util.JsonUtils;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * api  Permissions filtering 
 */
public class ModuleSecurityFilter implements Filter {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private AnaModuleSecurityService anaModuleSecurityService;
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
    	HttpServletResponse response = (HttpServletResponse) servletResponse;
    	String consumer = httpRequest.getHeader("consumer");
    	String apiKey = httpRequest.getHeader("apiKey");
    	String requestURI = httpRequest.getRequestURI();
    	logger.error("consumer:{},apiKey:{},requestURI:{}", consumer,apiKey,requestURI);
    	
    	if(StringUtils.isBlank(consumer) || StringUtils.isBlank(apiKey)){
    		errorReturn(response, "0001","consumer and apiKey can not be empty");
			return ;
    	}
        AnaModuleSecurity anaModuleSecurity = anaModuleSecurityService.findByConsumer(consumer);
        if(anaModuleSecurity == null){
        	errorReturn(response, "0002","You Are Not Unauthorized to Access the System!");
        	return ;
        }
        if(!apiKey.equals(anaModuleSecurity.getApiKey())){
        	errorReturn(response, "0003","api key error!");
        	return ;
        }
        for(String item : anaModuleSecurity.getApiUrlPath().split(",")){
        	if(item.equals(requestURI)){
        		filterChain.doFilter(servletRequest, servletResponse);
        		return;
        	}
        }

        errorReturn(response, "0004","You Are Not Unauthorized to Access the '"+requestURI+"'!");
    	return ;


    }

    @Override
    public void destroy() {

    }
    
    private void errorReturn(HttpServletResponse response,String errCode,String msg) throws IOException{
    	logger.error("ModuleSecurityFilter error " + msg);
    	RestfulResponse<Object> res = new RestfulResponse<>();
    	res.setErrorCode(errCode);
    	res.setMessageEN(msg);
    	res.setMessageZhCN(msg);
    	res.setMessageZhHK(msg);
    	
    	response.setStatus(HttpServletResponse.SC_OK);
		response.setHeader("Content-Type", "application/json");
		ServletOutputStream os = response.getOutputStream();
		os.println(JsonUtils.toJSon(res));
		os.close();
    }
    
    
}
