package com.tng.portal.log.client.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by Zero on 2016/12/13.
 */
@EnableAsync
@Configuration
@ComponentScan("com.tng.portal.log.client")
public class AppConfig{
}
