package com.gea.portal.agent.config;

import com.gea.portal.agent.job.SchedulerJobConfig;
import com.gea.portal.agent.util.QuartzUtil;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

/**
 * Created by Dell on 2018/10/12.
 */

@Configuration
public class SchedulerListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    SchedulerJobConfig schedulerJobConfig;

    @Autowired
    JobFactoryConfig jobFactoryConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            schedulerJobConfig.schedulerJob();
            logger.info("SynchronizedData job  start...");
        } catch (SchedulerException e) {
        	logger.error("error",e);
        }
    }

    @Bean(name = "SchedulerFactoryBeanConfig")
    public SchedulerFactoryBean mySchedulerFactory() throws IOException {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(10);
        bean.setJobFactory(jobFactoryConfig);
        bean.setQuartzProperties(QuartzUtil.quartzProperties());
        bean.setAutoStartup(true);
        return bean;
    }

}
