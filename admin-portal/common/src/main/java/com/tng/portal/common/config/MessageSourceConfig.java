package com.tng.portal.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Created by Dell on 2018/8/17.
 */
@Configuration
public class MessageSourceConfig {
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("locale/msg");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
