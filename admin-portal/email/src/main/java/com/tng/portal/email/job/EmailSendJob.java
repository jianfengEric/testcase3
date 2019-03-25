package com.tng.portal.email.job;


import com.tng.portal.email.service.EmailService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class EmailSendJob extends QuartzJobBean{

    private static final Logger logger = LoggerFactory.getLogger(EmailSendJob.class);

    @Autowired
    @Qualifier("emailService")
    private EmailService emailService;
    
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        emailService.sendEmails();
    }

}
