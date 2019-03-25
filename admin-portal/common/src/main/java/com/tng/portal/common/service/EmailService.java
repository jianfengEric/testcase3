package com.tng.portal.common.service;


import com.tng.portal.common.entity.EmailMessage;
import com.tng.portal.common.vo.rest.EmailParameterVo;

import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2016/11/17.
 */
public interface EmailService {
	
    void sendByHttp(EmailParameterVo emailParameterVo,Long emailId);

    void sendEmail(EmailParameterVo emailParameterVo);

    List<EmailMessage> getEmailMessages(String job);

    void sendByMQ(EmailParameterVo emailParameterVo);
    
    public EmailMessage saveEmailMessage(Long id,String job, String sender, String receivers, String subject, String message, Integer status,String senderId,String receiversId);

    public EmailMessage findOne(Long id);

    Date getSendDateByReceiver(String accountId);
}
