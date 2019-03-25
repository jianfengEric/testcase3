package com.tng.portal.email.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by Owen on 2018/9/13.
 */
@Configuration
public class HikariCPConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());//sonar modify 

    private static final String PROPERTY_NAME_DATABASE_URL = "spring.datasource.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "spring.datasource.username";
    private static final String PROPERTY_NAME_DATABASE_DRIVER = "spring.datasource.driver-class-name";

    private static final String PROPERTY_NAME_POOL_MINIDLE = "pool.minIdle";
    private static final String PROPERTY_NAME_POOL_MAXIDLE = "pool.maxIdle";
    private static final String PROPERTY_NAME_POOL_MAXACTIVE = "pool.maxActive";

    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {//sonar modify 
        HikariDataSource hds =null;
        try {
            hds = new HikariDataSource();
            hds.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
            hds.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
            hds.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
            hds.setPassword(env.getRequiredProperty("spring.datasource.password"));

            hds.setMinimumIdle(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MINIDLE)));
            hds.setIdleTimeout(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXIDLE)));
            hds.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXACTIVE)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);//sonar modify
        }
        return hds;
    }
}
