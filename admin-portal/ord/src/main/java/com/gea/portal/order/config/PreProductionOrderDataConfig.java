package com.gea.portal.order.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author christ
 * @create 2018-11-23
 **/
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPreProduction", 
transactionManagerRef = "transactionManagerPreProduction", basePackages = {
		"com.gea.portal.order.repository.preProduction.order" })
public class PreProductionOrderDataConfig {
    private static final String PROPERTY_NAME_POOL_MINIDLE = "pool.minIdle";
    private static final String PROPERTY_NAME_POOL_MAXIDLE = "pool.maxIdle";
    private static final String PROPERTY_NAME_POOL_MAXACTIVE = "pool.maxActive";
    
	@Resource
	private Environment env;
	@Autowired
	private JpaProperties jpaProperties;

	@Bean(name = "preProductionDataSource")
	public DataSource preProductionDataSource() {
		HikariDataSource hds = new HikariDataSource();
		hds.setDriverClassName(env.getRequiredProperty("spring.preProduction.driverClass"));
		hds.setJdbcUrl(env.getRequiredProperty("spring.preProduction.datasource.url"));
		hds.setUsername(env.getRequiredProperty("spring.preProduction.datasource.username"));
		hds.setPassword(env.getRequiredProperty("spring.preProduction.datasource.password"));
		hds.setMinimumIdle(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MINIDLE)));
		hds.setIdleTimeout(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXIDLE)));
		hds.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXACTIVE)));
		return hds;
	}

	@Bean(name = "entityManagerPreProduction")
	public EntityManager entityManagerPreProduction(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPreProduction(builder).getObject().createEntityManager();
	}

	@Bean(name = "entityManagerFactoryPreProduction")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPreProduction(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(preProductionDataSource()).properties(getVendorProperties(preProductionDataSource()))
				.packages("com.gea.portal.order.entity.preProduction.order").persistenceUnit("prePersistenceUnit").build();
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

	@Bean(name = "transactionManagerPreProduction")
	PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPreProduction(builder).getObject());
	}

}
