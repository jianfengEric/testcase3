package com.tng.portal.sms.server.soa;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.soa.AbstractSOABasicService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;
import com.tng.portal.log.client.service.LogClientService;
import com.tng.portal.sms.server.service.SMSJobService;


@Service("smsServerSOAService")
public class SMSServerSOAService  extends AbstractSOABasicService{
	private static final Logger logger = LoggerFactory.getLogger(SMSServerSOAService.class);

	@Autowired
	@Qualifier("logClientService")
	private LogClientService logClientService;
	
	@Autowired
	private SMSJobService smsJobService;
	
    @Autowired
    private AccountService accountService;


	private static final String serviceName = PropertiesUtil.getAppValueByKey("sms.server.service.name");
	
	@PostConstruct
	public void initConnectToMQ(){
		if(ApplicationContext.Modules.SMM.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))
				&&ApplicationContext.Env.integrated_client.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
			logger.info("SMSServerSOAService connectToMQ start!!");
			connectToMQ();
			logger.info("SMSServerSOAService connectToMQ end!!");
		}else{
			logger.info("SMSServerSOAService will not run!!");
		}

	}
	
    /**
     * create sms job
     * 
     * @param vo
     * 			sms info
     * 
     * @return
     */
    public RestfulResponse<Boolean> addJob(SMSJobInputVo vo) {
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	try{
    		smsJobService.addJob(vo);
    	} catch(Exception e){
    		logger.error("Create job exception",e);
	        logClientService.logError(e);
	        restResponse.setFailStatus();
	        return restResponse;
		}
    	restResponse.setData(true);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    public RestfulResponse<String> terminateJob(Long id) {
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	try{
    		restResponse.setData(smsJobService.terminateJob(id));
    	} catch(Exception e){
    		logger.error("Terminate job exception",e);
	        logClientService.logError(e);
	        restResponse.setFailStatus();
	        return restResponse;
		}
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    public RestfulResponse<String> findMobile(Long id, String status, String mobile) {
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	try{
    		restResponse.setData(smsJobService.findMobile(id, status, mobile));
    	} catch(Exception e){
    		logger.error("find mobile exception",e);
	        logClientService.logError(e);
	        restResponse.setFailStatus();
	        return restResponse;
		}
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
	 /**
     * swap user's authentication info by token
     * 
     * @param token
     * 			token string
     * 
     * @return
     */
	 public void tokenAuthentication(String token){
		 UserDetails userDetails = new UserDetails(accountService.getAuthuserInfoByToken(token));
		 if(null!=userDetails){
             AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,token,null);
             SecurityContextHolder.getContext().setAuthentication(authentication);
         }
	 }
	
	 public String getServiceName(){
		 return serviceName;
	 }


	 public Object getHandleInstance(){		 
		 return this;
	 }
}