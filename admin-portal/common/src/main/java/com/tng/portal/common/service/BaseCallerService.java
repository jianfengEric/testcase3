package com.tng.portal.common.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;

/**
 * Base Caller Service 父类
 */
public abstract class BaseCallerService {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected AnaApplicationService anaApplicationService;
	
	protected abstract String getTargetModule();

	protected String getTargetUrl(){
		AnaApplication anaa = anaApplicationService.findByCode(getTargetModule());
		String a = anaa.getInternalEndpoint();
    	return a;
    }
	
	protected String getTargetService(){
		return PropertiesUtil.getAppValueByKey(getTargetModule().toLowerCase() + ".service.name");
	}

	protected Map<String, String> setApiKeyParams(Map<String, String> params){
		params.put("apiKey", PropertiesUtil.getAppValueByKey(getTargetModule().toLowerCase() + ".comment.api.key"));
		params.put("consumer", PropertiesUtil.getAppValueByKey("service.name"));
		return params;
	}
	
	protected boolean isMq(){
		String style = PropertiesUtil.getAppValueByKey("communication.style");
		return ApplicationContext.Communication.MQ.equals(style);
	}

}
