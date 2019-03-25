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
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPreProductionMoneyPool",
transactionManagerRef = "transactionManagerPreProductionMoneyPool", basePackages = {
		"com.gea.portal.order.repository.preProduction.moneyPool" })
public class PreProductionMoneyPoolDataConfig {
    private static final String PROPERTY_NAME_POOL_MINIDLE = "pool.minIdle";
    private static final String PROPERTY_NAME_POOL_MAXIDLE = "pool.maxIdle";
    private static final String PROPERTY_NAME_POOL_MAXACTIVE = "pool.maxActive";
    
	@Resource
	private Environment env;
	@Autowired
	private JpaProperties jpaProperties;

	@Bean(name = "preProductionMoneyPoolDataSource")
	public DataSource preProductionDataSource() {
		HikariDataSource hds = new HikariDataSource();
		hds.setDriverClassName(env.getRequiredProperty("spring.preProduction.moneyPool.driverClass"));
		hds.setJdbcUrl(env.getRequiredProperty("spring.preProduction.moneyPool.datasource.url"));
		hds.setUsername(env.getRequiredProperty("spring.preProduction.moneyPool.datasource.username"));
		hds.setPassword(env.getRequiredProperty("spring.preProduction.moneyPool.datasource.password"));
		hds.setMinimumIdle(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MINIDLE)));
		hds.setIdleTimeout(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXIDLE)));
		hds.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXACTIVE)));
		return hds;
	}

	@Bean(name = "entityManagerPreProductionMoneyPool")
	public EntityManager entityManagerPreProduction(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPreProduction(builder).getObject().createEntityManager();
	}

	@Bean(name = "entityManagerFactoryPreProductionMoneyPool")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPreProduction(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(preProductionDataSource()).properties(getVendorProperties(preProductionDataSource()))
				.packages("com.gea.portal.order.entity.preProduction.moneyPool").persistenceUnit("preMoneyPoolPersistenceUnit").build();
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

	@Bean(name = "transactionManagerPreProductionMoneyPool")
	PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPreProduction(builder).getObject());
	}

}
