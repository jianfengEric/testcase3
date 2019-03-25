package com.gea.portal.agent.service;

import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for calling dpy Interface to other modules 
 */
@Service
public class DpyCallerService extends BaseCallerService{
	

	@Autowired
	private HttpClientUtils httpClientUtils;

	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.DPY;
	}

	public RestfulResponse<String> callDeploy(){
		String url = super.getTargetUrl().concat("/remote/v1/deploy-task");
		Map<String, String> param = new HashMap<>();
		super.setApiKeyParams(param);
		logger.error("url +{}",url);
		return httpClientUtils.httpGet(url, RestfulResponse.class, param);
	}


}
