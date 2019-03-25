package com.gea.portal.eny.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryPreProduction",// Configuring connection factories  entityManagerFactory
        transactionManagerRef = "transactionManagerPreProduction", // To configure   Thing Manager   transactionManager
        basePackages = {"com.gea.portal.eny.repository.pre"}// Set up dao(repo Location
)
public class PreProductionMoneyPoolDataConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("preProductionDataSource")
    private DataSource preProductionDataSource;


    /**
     *
     * @param builder
     * @return
     */
    @Bean(name = "entityManagerFactoryPreProduction")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPreProduction(EntityManagerFactoryBuilder builder) {

        return builder
                // set up data sources 
                .dataSource(preProductionDataSource)
                // Setting data source properties 
                .properties(getVendorProperties(preProductionDataSource))
                // Setting entity class location . Scan all with  @Entity  Annotated class 
                .packages("com.gea.portal.eny.entity.pre")
                // Spring Will be EntityManagerFactory Inject into Repository In . become pregnant  EntityManagerFactory after ,
                // Repository You can use it to create.  EntityManager     , Then? Entity You can perform operations on the database. 
                .persistenceUnit("preProductionPersistenceUnit")
                .build();

    }

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Bean(name = "transactionManagerPreProduction")
    @Primary
    PlatformTransactionManager transactionManagerPreProduction(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryPreProduction(builder).getObject());
    }


}
