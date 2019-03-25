package com.tng.portal.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.tng.portal.common.util.ApplicationContext.Key;

import java.util.Properties;

/**
 * Created by Owen on 2016/11/24.
 */
public class PropertiesUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	
	private static String filter = "";
	
    public static String getMQValueByKey(String key){
        try {
             Resource resource = new ClassPathResource("/rabbitMQ"+filter+".properties");
             Properties properties = PropertiesLoaderUtils.loadProperties(resource);
             return properties.getProperty(key);
         }catch (Exception e){
             logger.error("Exception",e);
             return null;
         }
     }
    public static String getMailValueByKey(String key){
        try {
            Resource resource = new ClassPathResource("/email"+filter+".properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.getProperty(key);
        }catch (Exception e){
            logger.error("Exception",e);
            return null;
        }
    }
    public static String getAppValueByKey(String key){
        try {
            Resource resource = new ClassPathResource("/application"+filter+".properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            return properties.getProperty(key);
        }catch (Exception e){
            logger.error("Exception",e);
            return null;
        }
    }
    public static String getPropertyValueByKey(String key){
        try {
            Resource resource = new ClassPathResource("app_client"+filter+".properties");
            Properties appProps = PropertiesLoaderUtils.loadProperties(resource);
            return appProps.getProperty(key);
        }catch (Exception e){
            logger.error("Exception",e);
            return null;
        }

    }
    
    public static String getLogValueByKey(String key){
        try {
            Resource resource = new ClassPathResource("log_client.properties");
            Properties appProps = PropertiesLoaderUtils.loadProperties(resource);
            return appProps.getProperty(key);
        }catch (Exception e){
            logger.error("Exception",e);
            return null;
        }

    }
    
    public static String getServiceName(){
        return getAppValueByKey(Key.serviceName);
    }
    
	public static void setFilter(String filter) {
		PropertiesUtil.filter = "-"+filter;
	}
	
	public static void setFilter(String[] args) {
		if(args != null && args.length > 0){
    		String filter = args[0];
    		filter = filter.replace("--spring.profiles.active=", "");
    		setFilter(filter);
    	}
	}

}
