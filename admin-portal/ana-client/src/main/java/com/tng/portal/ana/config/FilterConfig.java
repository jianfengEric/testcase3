package com.tng.portal.ana.config;
 
import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tng.portal.ana.filter.ModuleSecurityFilter;
 
 
/**
 *  filter config
 */
@Configuration
public class FilterConfig   {
 
    @Bean
    public FilterRegistrationBean apiSecurityFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(moduleSecurityFilter());
        registration.addUrlPatterns("/remote/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("moduleSecurityFilter");
        registration.setOrder(1);
        return registration;
    }
    
    @Bean
    public Filter moduleSecurityFilter() {
        return new ModuleSecurityFilter();
    }

 
 
 
}
