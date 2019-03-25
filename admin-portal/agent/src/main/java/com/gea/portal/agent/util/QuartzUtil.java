package com.gea.portal.agent.util;

import com.tng.portal.common.util.PropertiesUtil;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Timed task utility class 
 *
 */
public class QuartzUtil {
	
	private QuartzUtil(){}

	/**
     * set a property 
     */
    public static Properties quartzProperties() {
	    String quartzInstanceName = PropertiesUtil.getAppValueByKey("org.quartz.scheduler.instanceName");
	    String myDSDriver = PropertiesUtil.getAppValueByKey("org.quartz.dataSource.myDS.driver");
	    String myDSURL = PropertiesUtil.getAppValueByKey("org.quartz.dataSource.myDS.URL");
	    String myDSUser = PropertiesUtil.getAppValueByKey("org.quartz.dataSource.myDS.user");
	    String myDSPassword = PropertiesUtil.getAppValueByKey("org.quartz.dataSource.myDS.password");
	    String myDSMaxConnections = PropertiesUtil.getAppValueByKey("org.quartz.dataSource.myDS.maxConnections");
    	
        Properties prop = new Properties();
        prop.put("quartz.scheduler.instanceName", quartzInstanceName);
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
        prop.put("org.quartz.scheduler.jmx.export", "true");
         
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        prop.put("org.quartz.jobStore.dataSource", "quartzDataSource");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.isClustered", "true");
         
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "20000");
        prop.put("org.quartz.jobStore.dataSource", "myDS");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
        prop.put("org.quartz.jobStore.misfireThreshold", "120000");
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS WHERE LOCK_NAME = ? FOR UPDATE");
         
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "10");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
         
        prop.put("org.quartz.dataSource.myDS.driver", myDSDriver);
        prop.put("org.quartz.dataSource.myDS.URL", myDSURL);
        prop.put("org.quartz.dataSource.myDS.user", myDSUser);
        prop.put("org.quartz.dataSource.myDS.password", myDSPassword);
        prop.put("org.quartz.dataSource.myDS.maxConnections", myDSMaxConnections);
         
        prop.put("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingJobHistoryPlugin");
        prop.put("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        prop.put("org.quartz.plugin.shutdownhook.cleanShutdown", "true");
        return prop;
    }


    /**
     * Create trigger factory 
     * @param jobDetail
     * @param cronExpression
     * @return
     */
    public static CronTriggerFactoryBean genCronTriggerFactoryBean(JobDetail jobDetail, String cronExpression) { 
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean(); 
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression (cronExpression);
        return factoryBean; 
    }
    
    
    /**
     * Establish job Interface to other modules 
     * @param jobClass
     * @param groupName
     * @param targetObject
     * @return
     */
    public static JobDetailFactoryBean createJobDetail(Class<?> jobClass, String groupName, String targetObject) { 
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean(); 
        factoryBean.setJobClass(jobClass); 
        factoryBean.setDurability(true); 
        factoryBean.setRequestsRecovery(true);
        factoryBean.setGroup(groupName);
        Map<String, String> map = new HashMap<>();
        map.put("targetObject", targetObject);
        map.put("targetMethod", "execute");
        factoryBean.setJobDataAsMap(map);
        return factoryBean; 
    } 
}
