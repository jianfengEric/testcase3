package com.tng.portal.ana.authentication;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.tng.portal.ana.bean.TokenType;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.constant.AccountStatus;
import com.tng.portal.ana.service.TokenService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.util.JWTTokenUtil;
import com.tng.portal.ana.util.ToolUtil;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.serialize.JsonObjectMapper;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Zero on 2016/11/10.
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Qualifier("tokenServiceImpl")
    @Autowired
    private TokenService tokenServiceImpl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)){
            throw new AuthenticationServiceException("Expecting a http request");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String accessToken = httpRequest.getHeader("token");
        if(StringUtils.isBlank(accessToken) || "null".equalsIgnoreCase(accessToken) || "undefined".equalsIgnoreCase(accessToken) ){
            accessToken = request.getParameter("token");
        }
        String ip = ToolUtil.getRemoteHost(httpRequest);
        try {
            if(StringUtils.isNotBlank(accessToken)){
                UserDetails userDetails = null;
                try{
                    userDetails = userService.getUserDetailByToken(accessToken);
                }catch (Exception e){
                    log.error(e.getMessage());
                }
                if(!ApplicationContext.Env.standalone.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))
                        && !PropertiesUtil.getServiceName().equals(ApplicationContext.Modules.ANA)){
                    userDetails = tokenServiceImpl.updateToken(userDetails,accessToken);
                }else{
                    if(PropertiesUtil.getServiceName().equals(ApplicationContext.Modules.SMM) ||
                    		PropertiesUtil.getServiceName().equals(ApplicationContext.Modules.MSG)){
                        userDetails = tokenServiceImpl.updateClientToken(userDetails,accessToken);
                    }
                }
                if(null!=userDetails){
                    if(userDetails.getAccount().getStatus().equals(AccountStatus.NotVerified)||
                            userDetails.getAccount().getStatus().equals(AccountStatus.Active)){
                        AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,accessToken,ip);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }else {
                        StringBuilder sb = new StringBuilder("GEA Participant status is ");
                        sb.append(userDetails.getAccount().getStatus().name()).append(".\n");
                        sb.append("Not authorized to access ").append(PropertiesUtil.getServiceName()).append(".");
                        writeResponse((HttpServletResponse)response, 403, sb.toString());
                    }
                }else{
                    writeResponse((HttpServletResponse)response, 4003, "Login timeout or password has been reset, please login again.");
                }
            }
        } catch (BusinessException e) {
            log.error(e.getErrormsg());
            throw new AuthenticationServiceException(e.getMessage());
        }catch (Exception e) {
        	log.error("Exception",e);
            throw new AuthenticationServiceException(e.getMessage());
		}
        chain.doFilter(request, response);
    }


    private void writeResponse(HttpServletResponse response, int errorCode, String errorMsg) {
        PrintWriter out = null;
        try {
            RestfulResponse restResponse = new RestfulResponse();
            restResponse.setFailStatus();
            restResponse.setErrorCode(String.valueOf(errorCode));
            restResponse.setMessageEN(errorMsg);
            restResponse.setMessageZhCN(errorMsg);
            restResponse.setMessageZhHK(errorMsg);
            String resultJson = new JsonObjectMapper().writeValueAsString(restResponse);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.write(resultJson);
        } catch (Exception e) {
            log.info("write response error"+ e.getMessage());
        } finally {
            try {
                if(Objects.nonNull(out)) {
                    out.flush();
                    out.close();
                }
            }catch (Exception ee){
                log.error(ee.getMessage());
            }
        }

    }
}
