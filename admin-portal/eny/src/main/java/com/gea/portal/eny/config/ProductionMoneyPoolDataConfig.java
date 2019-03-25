package com.gea.portal.eny.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryProduction",
        transactionManagerRef="transactionManagerProduction",
        basePackages= { "com.gea.portal.eny.repository.pro" })
public class ProductionMoneyPoolDataConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("productionDataSource")
    private DataSource productionDataSource;



    @Bean(name = "entityManagerProduction")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryProduction(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryProduction")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryProduction (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(productionDataSource)
                .properties(getVendorProperties(productionDataSource))
                .packages("com.gea.portal.eny.entity.pro")
                .persistenceUnit("productionPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerProduction")
    PlatformTransactionManager transactionManagerProduction(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryProduction(builder).getObject());
    }


}
