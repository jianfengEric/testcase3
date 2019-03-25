package com.gea.portal.agent.job;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tng.portal.common.service.AnaApplicationService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;

/**
 * Time cleaningtoken
 */
public class DeleteTokenGeaJob implements Job{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/*@Autowired
	private DeleteTokenService deleteTokenService;*/
	
	@Autowired
	private HttpClientUtils httpClientUtils;
	
	@Autowired
	private AnaApplicationService anaApplicationService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			//删除子模块的token
			String[] modulesArr = new String[]{
					ApplicationContext.Modules.EWP,
					ApplicationContext.Modules.MP,
					ApplicationContext.Modules.APV,
					ApplicationContext.Modules.TRE,
					ApplicationContext.Modules.SRV,
					ApplicationContext.Modules.ORD
					};
			for(String module : modulesArr){
				Map<String,String> params = new HashMap<>();
				params.put("consumer", PropertiesUtil.getAppValueByKey("service.name"));
				params.put("apiKey", PropertiesUtil.getAppValueByKey(module.toLowerCase() + ".comment.api.key"));
				httpClientUtils.httpGet(anaApplicationService.findByCode(module).getInternalEndpoint().concat("/remote/account/delete-ana-token"), String.class, params);
			}
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
	

}
