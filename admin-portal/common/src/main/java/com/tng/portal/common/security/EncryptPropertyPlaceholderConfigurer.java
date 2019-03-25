/*
package com.tng.portal.common.security;

import java.util.Properties;

import com.tng.portal.common.util.CoderUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	String[] propertyNames = new String[]{"dataSource.password"};
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		for(String propertyName: propertyNames){
			props.setProperty(propertyName, CoderUtil.decrypt(props.getProperty(propertyName)));
		}
		super.processProperties(beanFactoryToProcess, props);
	}

}
*/
