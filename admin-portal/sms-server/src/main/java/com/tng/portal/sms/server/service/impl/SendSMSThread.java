package com.tng.portal.sms.server.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.sms.server.constant.SMSStatus;
import com.tng.portal.sms.server.entity.SMSJobDetail;
import com.tng.portal.sms.server.entity.SMSServiceApplication;
import com.tng.portal.sms.server.repository.JobDetailRepository;
import com.tng.portal.sms.server.service.SMSSendService;
import com.tng.portal.sms.server.vo.SendSMSResponseVo;

/**
 * Created by Owen on 2017/7/28.
 */
public class SendSMSThread implements Runnable {

    private JobDetailRepository jobDetailRepository;
    private SMSSendService smsSendService;
    private List<SMSServiceApplication> providerList;
    private SMSJobDetail jodDetail;
    private String content;
    private static final String SMS_SEND_MODE = PropertiesUtil.getAppValueByKey("sms.send.mode");

    public SendSMSThread(JobDetailRepository jobDetailRepository, SMSSendService smsSendService, List<SMSServiceApplication> providerList, SMSJobDetail jodDetail, String content) {
        this.jobDetailRepository = jobDetailRepository;
        this.smsSendService = smsSendService;
        this.providerList = providerList;
        this.jodDetail = jodDetail;
        this.content = content;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void run() {
        SendSMSResponseVo rspVo = null;
        Date sendTime = new Date();
        for (int i = 0; i < this.providerList.size(); i++) {
        	if("virtual".equals(SMS_SEND_MODE)){
				rspVo = smsSendService.sendVirtualSMS(this.providerList.get(i).getSmsProvider().getId(), this.jodDetail.getMobileNumber(), this.content);
			}else if("real".equals(SMS_SEND_MODE)){
				rspVo = smsSendService.sendSMS(this.providerList.get(i).getSmsProvider().getId(), this.jodDetail.getMobileNumber(), this.content);
			}
            if (null != rspVo && SMSStatus.SUCCESS.getDesc().equals(rspVo.getStatus())) {
                break;
            }
        }
        if(null != rspVo){
            this.jodDetail.setStatus(rspVo.getStatus());
            this.jodDetail.setSentTimestamp(sendTime);
            this.jodDetail.setResponseTimestamp(new Date());
            this.jodDetail.setProviderResponse(rspVo.getRspMsg());
            this.jodDetail.setSystemResponse(rspVo.getErrorMsg());
            this.jodDetail.setSmsProvider(rspVo.getSmsProvider());
        }
        jobDetailRepository.save(this.jodDetail);
    }

}
