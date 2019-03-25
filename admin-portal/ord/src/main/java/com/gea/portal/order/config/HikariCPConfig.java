package com.gea.portal.order.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author chirst
 * @time 2018-11-24
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.tng.portal.ana.repository", "com.tng.portal.common.repository"})
public class HikariCPConfig {
    private static final String PROPERTY_NAME_DATABASE_URL = "spring.datasource.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "spring.datasource.username";
    private static final String PROPERTY_NAME_DATABASE_DRIVER = "spring.datasource.driver-class-name";

    private static final String PROPERTY_NAME_POOL_MINIDLE = "pool.minIdle";
    private static final String PROPERTY_NAME_POOL_MAXIDLE = "pool.maxIdle";
    private static final String PROPERTY_NAME_POOL_MAXACTIVE = "pool.maxActive";

    @Resource
    private Environment env;
    @Primary
    @Bean
    public DataSource dataSource() {
        HikariDataSource hds = new HikariDataSource();
        hds.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        hds.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        hds.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        hds.setPassword(env.getRequiredProperty("spring.datasource.password"));

        hds.setMinimumIdle(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MINIDLE)));
        hds.setIdleTimeout(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXIDLE)));
        hds.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXACTIVE)));

        return hds;
    }
    
    @Autowired
    private JpaProperties jpaProperties;

    @Primary
    @Bean
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource())
                .properties(getVendorProperties(dataSource()))
                .packages("com.tng.portal.ana.entity"/*, "com.gea.portal.order.entity"*/, "com.tng.portal.common.entity")
                .persistenceUnit("persistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }
    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }

}
