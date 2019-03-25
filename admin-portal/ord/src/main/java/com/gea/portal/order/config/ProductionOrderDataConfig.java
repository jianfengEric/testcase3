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
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryProduction", transactionManagerRef = "transactionManagerProduction", basePackages = {
		"com.gea.portal.order.repository.production.order" })
public class ProductionOrderDataConfig {
	private static final String PROPERTY_NAME_POOL_MINIDLE = "pool.minIdle";
	private static final String PROPERTY_NAME_POOL_MAXIDLE = "pool.maxIdle";
	private static final String PROPERTY_NAME_POOL_MAXACTIVE = "pool.maxActive";

	@Resource
	private Environment env;

	@Autowired
	private JpaProperties jpaProperties;

	@Bean(name = "productionDataSource")
	public DataSource productionDataSource() {
		HikariDataSource hds = new HikariDataSource();
		hds.setDriverClassName(env.getRequiredProperty("spring.production.driverClass"));
		hds.setJdbcUrl(env.getRequiredProperty("spring.production.datasource.url"));
		hds.setUsername(env.getRequiredProperty("spring.production.datasource.username"));
		hds.setPassword(env.getRequiredProperty("spring.production.datasource.password"));
		hds.setMinimumIdle(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MINIDLE)));
		hds.setIdleTimeout(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXIDLE)));
		hds.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty(PROPERTY_NAME_POOL_MAXACTIVE)));

		return hds;
	}

	@Bean(name = "entityManagerProduction")
	public EntityManager entityManagerProduction(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryProduction(builder).getObject().createEntityManager();
	}

	@Bean(name = "entityManagerFactoryProduction")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryProduction(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(productionDataSource()).properties(getVendorProperties(productionDataSource()))
				.packages("com.gea.portal.order.entity.production.order").persistenceUnit("productionPersistenceUnit")
				.build();
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

	@Bean(name = "transactionManagerProduction")
	PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryProduction(builder).getObject());
	}
}
