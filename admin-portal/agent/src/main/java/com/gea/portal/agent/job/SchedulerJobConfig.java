package com.gea.portal.agent.job;

import com.gea.portal.agent.util.SpringUtil;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Timed task 
 */
@Component
public class SchedulerJobConfig {

    @Value("${delete.token.job.cron}")
    private String deleteTokenJobCron;
    @Value("${dpy.deploy.gea.job.cron}")
    private String dpyDeployGeaJobCron;

    public void schedulerJob() throws SchedulerException {
        ApplicationContext annotationContext = SpringUtil.getApplicationContext();
        StdScheduler stdScheduler = (StdScheduler) annotationContext.getBean("SchedulerFactoryBeanConfig");
        Scheduler scheduler =stdScheduler;
        scheduler.clear();
        startDpyDeployGeaJob(scheduler);
//        startAnaDeleteTokenJob(scheduler);
        scheduler.start();
    }

    public void startDpyDeployGeaJob(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(DpyDeployGeaJob.class).withIdentity("dpyDeployGeaJob", "dpyDeployGeaJobGroup").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(dpyDeployGeaJobCron);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("dpyDeployGeaTrigger", "dpyDeployGeaTriggerGroup")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void startAnaDeleteTokenJob(Scheduler scheduler) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(DeleteTokenGeaJob.class).withIdentity("anaDeleteTokenJob", "anaDeleteTokeGroup").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(deleteTokenJobCron);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("anaDeleteTokeTrigger", "anaDeleteTokeTriggerGroup")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, trigger);
    }


}
