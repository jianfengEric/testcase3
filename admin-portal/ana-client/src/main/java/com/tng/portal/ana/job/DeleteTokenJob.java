package com.tng.portal.ana.job;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.common.util.PropertiesUtil;

public class DeleteTokenJob extends QuartzJobBean {

    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;
    
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String expiresMinus = PropertiesUtil.getPropertyValueByKey("token.expires.mins");
		int minus = Integer.parseInt(expiresMinus);
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.MINUTE,-minus);
		date=calendar.getTime();
		anaAccountAccessTokenRepository.deleteByExpriedTime(date);
	}
}

