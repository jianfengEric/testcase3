package com.tng.portal.common.service.impl;


import com.tng.portal.common.entity.EmailMessage;
import com.tng.portal.common.entity.EmailTemplate;
import com.tng.portal.common.service.AnaApplicationService;
import com.tng.portal.common.service.EmailService;
import com.tng.portal.common.util.*;
import com.tng.portal.common.vo.email.EMailInput;
import com.tng.portal.common.vo.rest.EmailParameterVo;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Owen on 2016/11/17.
 */
@Transactional
@Service("commonEmailService")
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;
    
    @Autowired
    private AnaApplicationService anaApplicationService;

    @Override
    public void sendByMQ(EmailParameterVo emailParameterVo) {
        EmailTemplate emailTemplate = findEmailJob(emailParameterVo.getJob());
        if (emailParameterVo == null || null == emailTemplate) {
            return;
        }

        String sender = "";
        String receivers = "";
        String subject = "";
        String message = "";

        if (null != emailParameterVo.getSubject() && !emailParameterVo.getSubject().isEmpty()) {
            subject = emailParameterVo.getSubject();
        } else {
            subject = setTemplateAttributes(emailTemplate.getSubject(), emailParameterVo.getTemplateInput());
        }
        if (null != emailParameterVo.getSender() && !emailParameterVo.getSender().isEmpty()) {
            sender = emailParameterVo.getSender();
        } else {
            sender = emailTemplate.getSender();
        }
        if (null != emailParameterVo.getMessage() && !emailParameterVo.getMessage().isEmpty()) {
            message = emailParameterVo.getMessage();
        } else {
            message = setTemplateAttributes(emailTemplate.getEmailTemplate(), emailParameterVo.getTemplateInput());
        }
        if (null != emailParameterVo.getReceivers() && !emailParameterVo.getReceivers().isEmpty()) {
            receivers = emailParameterVo.getReceivers();
        } else {
            receivers = emailTemplate.getReceivers();
        }
        try {
            String serviceName = PropertiesUtil.getMailValueByKey("soa.email.service.name");
            String methodName =  PropertiesUtil.getMailValueByKey("soa.email.method.name");
            EMailInput eMailInput = new EMailInput();
            eMailInput.setApplicationCode(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName));
            eMailInput.setContent(message);
            eMailInput.setReceivers(receivers);
            eMailInput.setSender(sender);
            eMailInput.setSubject(subject);
            RabbitMQUtil.sendRequestToSOAService( serviceName, methodName, MqParam.gen(eMailInput));
        }catch (Exception e){
            logger.error("send email error:" + e);
        }
    }

    /**
     * test
     * @param emailParameterVo
     */
    @Override
    @Async
    public void sendByHttp(EmailParameterVo emailParameterVo,Long emailId)  {
        if(null == emailParameterVo ) {
            return;
        }
        int status = 1;
        String sender = "";
        String receivers = "";
        String subject = "";
        String message = "";
        try {
            EmailTemplate emailTemplate = findEmailJob(emailParameterVo.getJob());
            if(null != emailTemplate){
                subject = setTemplateAttributes(emailTemplate.getSubject(), emailParameterVo.getTemplateInput());
                sender = emailTemplate.getSender();
                message = setTemplateAttributes(emailTemplate.getEmailTemplate(), emailParameterVo.getTemplateInput());
                receivers = StringUtils.isBlank(emailParameterVo.getReceivers())? emailTemplate.getReceivers():emailParameterVo.getReceivers();
            }else {
                subject = emailParameterVo.getSubject();
                sender = emailParameterVo.getSender();
                message = emailParameterVo.getMessage();
                receivers = emailParameterVo.getReceivers();
            }

            EMailInput input = new EMailInput();
            String applicationCode = PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName);
            input.setApplicationCode(applicationCode);
            input.setContent(message);
            input.setReceivers(receivers);
            input.setSender(sender);
            input.setSubject(subject);
            if(ApplicationContext.Communication.HTTP.equalsIgnoreCase(PropertiesUtil.getAppValueByKey("communication.style"))){
                Map<String, String> params= new HashMap<>();
                params.put("apiKey", PropertiesUtil.getAppValueByKey("email.comment.api.key"));
                params.put("consumer", applicationCode);
                RestfulResponse response = httpClientUtils.httpPost(anaApplicationService.findByCode("MSG").getInternalEndpoint()+"/remote/sendEmail", input, RestfulResponse.class, null, params);
            }else{
                String serviceName = PropertiesUtil.getMailValueByKey("soa.email.service.name");
                String methodName =  PropertiesUtil.getMailValueByKey("soa.email.method.name");
                RabbitMQUtil.sendRequestToSOAService( serviceName, methodName, MqParam.gen(input));
            }



        } catch (Exception e) {
            status = 0;
            logger.error("send email error:" + e);
        }finally {
            saveEmailMessage(emailId,emailParameterVo.getJob(), sender, receivers, subject, message, status,emailParameterVo.getSenderId(),emailParameterVo.getReceiversId());
        }

    }

    @Override
    public void sendEmail(EmailParameterVo emailParameterVo) {
        if(null == emailParameterVo ) {
            return;
        }
        int status = 1;
        if (null == emailParameterVo.getSubject() || emailParameterVo.getSubject().isEmpty()) {
            return;
        }
        if (null == emailParameterVo.getMessage() || emailParameterVo.getMessage().isEmpty()) {
            return;
        }
        if (null == emailParameterVo.getReceivers() || emailParameterVo.getReceivers().isEmpty()) {
            return;
        }
        try {
            EMailInput input = new EMailInput(emailParameterVo.getSubject(), emailParameterVo.getReceivers(), emailParameterVo.getMessage());
            MailUtil.sendHtmlMail(input);
        } catch (Exception e) {
            status = 0;
            logger.error("send email error:" + e);
        }finally {
            saveEmailMessage(null,emailParameterVo.getJob(), null, emailParameterVo.getReceivers(), emailParameterVo.getSubject(), emailParameterVo.getMessage(), status,emailParameterVo.getSenderId(),emailParameterVo.getReceiversId());
        }
    }

    private EmailTemplate findEmailJob(String job) {
        if(null == job || job.isEmpty()){
            return null;
        }
        String sql = "SELECT id, job, subject, sender, receivers, email_template FROM email_job WHERE job = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, job);
        List<Object[]> list = query.getResultList();
        return buildEmailJob(list);
    }

    private EmailTemplate buildEmailJob(List<Object[]> list) {
        Optional<Object[]> optional = list.stream().findFirst();
        if(optional.isPresent()){
            Object[] objects = optional.get();
            EmailTemplate emailTemplate = new EmailTemplate();
            emailTemplate.setId(Long.valueOf(objects[0].toString()));
            emailTemplate.setJob(objects[1].toString());
            emailTemplate.setSubject(objects[2].toString());
            emailTemplate.setSender(objects[3].toString());
            emailTemplate.setReceivers(null == objects[4] ? "" : objects[4].toString());
            emailTemplate.setEmailTemplate(objects[5].toString());
            return emailTemplate;
        }
        return null;
    }


    /**
     * According to the job query has been sent mailing information list
     * @param job email template code
     * @return
     */
    @Override
    public List<EmailMessage> getEmailMessages(String job) {
        String sql = "SELECT a.* FROM email_message a WHERE a.job = ?";
        Query query =  entityManager.createNativeQuery(sql);
        query.setParameter(1, job);
        List<Object[]> list = query.getResultList();
        return buildEmailMessages(list);
    }

    private List<EmailMessage> buildEmailMessages(List<Object[]> resultList) {
        List<EmailMessage> list = new ArrayList<>();
        if(null == resultList || resultList.isEmpty())
            return list;
        for (Object[] objects : resultList){
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setId(Long.valueOf(objects[0].toString()));
            emailMessage.setJob(objects[1].toString());
            emailMessage.setSubject(objects[2].toString());
            emailMessage.setSender(objects[3].toString());
            emailMessage.setReceivers(objects[4].toString());
            emailMessage.setMessage(objects[5].toString());
            emailMessage.setSendTime(new Date(((Timestamp) objects[6]).getTime()));
            emailMessage.setStatus(Integer.valueOf(objects[7].toString()));
            list.add(emailMessage);
        }
        return list;
    }

    @Override
    public EmailMessage saveEmailMessage(Long id,String job, String sender, String receivers, String subject, String message, Integer status,String senderId,String receiversId) {
    	EmailMessage emailMessage = new EmailMessage();
    	if(id != null){
    		emailMessage = entityManager.find(EmailMessage.class, id);
        }
    	emailMessage.setJob(job);
		emailMessage.setSubject(subject);
		emailMessage.setSender(sender);
		emailMessage.setReceivers(receivers);
		emailMessage.setMessage(message);
		emailMessage.setSendTime(new Date());
		emailMessage.setStatus(status);
		emailMessage.setSenderId(senderId);
		emailMessage.setReceiversId(receiversId);
		entityManager.persist(emailMessage);
		return emailMessage;
    }




    /**
     * Replace template data
     * @param template
     * @param templateInput data
     * @return
     */
    private String setTemplateAttributes(String template, Map<String, String> templateInput){
        if(null == templateInput || templateInput.keySet().isEmpty()){
            return template;
        }
        // Generating regular expressions for matching patterns 
        String patternString = "\\$\\{(" + StringUtils.join(templateInput.keySet(), "|") + ")\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, templateInput.get(matcher.group(1))==null?"":templateInput.get(matcher.group(1)).replace("$", "\\$"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

	@Override
	public EmailMessage findOne(Long id) {
		return entityManager.find(EmailMessage.class, id);
	}

    @Override
    public Date getSendDateByReceiver(String accountId) {
        String sql = "SELECT SEND_TIME FROM email_message WHERE STATUS = 2 AND RECEIVERS_ID = ? ORDER BY SEND_TIME ASC LIMIT 1";
        Query query =  entityManager.createNativeQuery(sql);
        query.setParameter(1, accountId);
        List list = query.getResultList();
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return (Date)list.get(0);
    }
}
