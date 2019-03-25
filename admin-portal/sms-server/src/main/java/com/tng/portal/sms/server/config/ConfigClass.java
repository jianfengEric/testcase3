package com.tng.portal.sms.server.config;

import com.tng.portal.log.client.service.LogClientService;
import com.tng.portal.log.client.service.impl.LogClientServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Dell on 2018/8/6.
 */
@Configuration
//@ImportResource("classpath:schedule.xml")
public class ConfigClass {

    @Bean("logClientService")
    public LogClientService createLogClientService() {
        LogClientServiceImpl logClientService = new LogClientServiceImpl();
        logClientService.setApplicationCode("SMS");
        return logClientService;
    }
}
