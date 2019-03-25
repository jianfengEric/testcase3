package com.gea.portal.agent.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Dell on 2018/10/12.
 */

@Component
public class JobFactoryConfig extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // Method of calling the parent class
        Object jobInstance = super.createJobInstance(bundle);
        // Injecting
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
