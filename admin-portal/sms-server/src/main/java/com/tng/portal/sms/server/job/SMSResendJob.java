package com.tng.portal.sms.server.job;

import com.tng.portal.sms.server.constant.SMSServiceApplicationStatus;
import com.tng.portal.sms.server.constant.SMSStatus;
import com.tng.portal.sms.server.entity.SMSJob;
import com.tng.portal.sms.server.entity.SMSJobDetail;
import com.tng.portal.sms.server.entity.SMSServiceApplication;
import com.tng.portal.sms.server.repository.JobDetailRepository;
import com.tng.portal.sms.server.repository.JobRepository;
import com.tng.portal.sms.server.repository.SMSServiceApplicationRepository;
import com.tng.portal.sms.server.service.SMSSendService;
import com.tng.portal.sms.server.service.impl.SendSMSThread;
import com.tng.portal.sms.server.util.StringUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Owen on 2017/7/26.
 */
@Component
public class SMSResendJob extends QuartzJobBean {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobDetailRepository jobDetailRepository;
    @Autowired
    private SMSServiceApplicationRepository smsServiceApplicationRepository;
    @Autowired
    private SMSSendService smsSendService;

    private static final int waitMin = 10;

    private static final int contentLength = 160;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<SMSJob> list = jobRepository.findByStatusAndCount(SMSStatus.NEW.getDesc(), SMSStatus.QUEUE.getDesc());
        if(null == list || list.isEmpty()){
            return;
        }
        List<SMSServiceApplication> appList = smsServiceApplicationRepository.findByStatus(SMSServiceApplicationStatus.ACT.getDesc());
        list = batchModifyStatus(list);// modify state 
        for (SMSJob smsJob : list) {
            for (SMSJobDetail smsJobDetail : smsJob.getJobDetails()) {
                if(!smsJobDetail.getStatus().equals(SMSStatus.NEW.getDesc()) && !smsJobDetail.getStatus().equals(SMSStatus.QUEUE.getDesc())){
                    continue;
                }
                Date now = new Date();
                if (null != smsJobDetail.getScheduleTime() && smsJobDetail.getScheduleTime().compareTo(now) != 0 && smsJobDetail.getScheduleTime().after(now)) {
                    continue;
                }
                boolean isLong = false;
                boolean isSpecial = false;
                boolean isForeignCountry = false;
                if (smsJob.getContent().length() > contentLength) {
                    isLong = true;
                }
                if (StringUtil.checkStrSpecial(smsJob.getContent())) {
                    isSpecial = true;
                }
                if (!smsJobDetail.getMobileNumber().startsWith("852")) {
                    isForeignCountry = true;
                }
                List<SMSServiceApplication> result = getProviders(appList, smsJob.getAnaApplication().getCode(), isLong, isSpecial, isForeignCountry);
                if (null == result || result.isEmpty()) {
                    smsJobDetail.setStatus(SMSStatus.FAIL.getDesc());
                    smsJobDetail.setSentTimestamp(new Date());
                    smsJobDetail.setStatusChgTimestamp(new Date());
                    jobDetailRepository.save(smsJobDetail);
                    continue;
                }
                SendSMSThread send = new SendSMSThread(jobDetailRepository, smsSendService, result, smsJobDetail, smsJob.getContent());
                send.run();
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SMSJob>  batchModifyStatus(List<SMSJob> list){
        List<SMSJobDetail> all = list.stream().map(SMSJob::getJobDetails).flatMap(item->item.stream()).distinct().collect(Collectors.toList());
        List<SMSJobDetail> news = all.stream().filter(item -> item.getStatus().equals(SMSStatus.NEW.getDesc())).map(item->{
            item.setStatus(SMSStatus.QUEUE.getDesc());
            item.setStatusChgTimestamp(new Date());
            return item;
        }).collect(Collectors.toList());
        jobDetailRepository.save(news);


        List<SMSJobDetail> queues = all.stream().filter(item -> item.getStatus().equals(SMSStatus.QUEUE.getDesc())).filter(item->checkDate(item.getStatusChgTimestamp())).map(item -> {
            item.setStatus(SMSStatus.FAIL.getDesc());
            item.setStatusChgTimestamp(new Date());
            return item;
        }).collect(Collectors.toList());
        jobDetailRepository.save(queues);

        return list;
    }

    public List<SMSServiceApplication> getProviders(List<SMSServiceApplication>  appList, String applicationCode, boolean isLong, boolean isSpecial, boolean isForeignCountry){
        List<SMSServiceApplication> orderList =appList.stream().filter(item -> item.getAnaApplication().getCode().equals(applicationCode)).sorted(Comparator.comparing(SMSServiceApplication::getPriority)).collect(Collectors.toList());
        return orderList.stream() .filter(item -> {
            boolean l = true;
            if (isLong) {
                l = item.getSmsProvider().isLongSMS() == isLong;
            }
            boolean s = true;
            if (isSpecial) {
                s = item.getSmsProvider().isSpecialCharacter() == isSpecial;
            }
            boolean f = true;
            if (isForeignCountry) {
                f = item.getSmsProvider().isSendForeignCountry() == isForeignCountry;
            }
            return l && s && f;
        }).collect(Collectors.toList());
    }

    public static boolean checkDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Calendar now = Calendar.getInstance();
        if (c.after(now)) {
            return false;
        }
        if (c.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
            return true;
        }
        if (c.get(Calendar.MONTH) < now.get(Calendar.MONTH)) {
            return true;
        }
        long day = (now.getTimeInMillis() - c.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        long hour = (now.getTimeInMillis() - c.getTimeInMillis()) / (1000 * 60 * 60);
        long mi = (now.getTimeInMillis() - c.getTimeInMillis()) / (1000 * 60);
        return day > 0 || hour > 0 || mi > waitMin;
    }

}
