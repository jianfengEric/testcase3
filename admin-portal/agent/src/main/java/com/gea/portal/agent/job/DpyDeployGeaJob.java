package com.gea.portal.agent.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gea.portal.agent.service.DpyCallerService;

/**
 * Scheduled deployment to GEA
 */
public class DpyDeployGeaJob implements Job{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DpyCallerService dpyCallerService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			dpyCallerService.callDeploy();
		} catch (Exception e) {
			logger.error("error",e);
		}
	}

}
