package com.gea.portal.eny.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    @Bean(name = "preProductionDataSource")
    @Primary
    @Qualifier("preProductionDataSource")
    @ConfigurationProperties(prefix = "spring.preproduction.datasource")
    public DataSource preProductionDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "productionDataSource")
    @Qualifier("productionDataSource")
    @ConfigurationProperties(prefix = "spring.production.datasource")
    public DataSource productionDataSource() {
        return DataSourceBuilder.create().build();
    }


}
