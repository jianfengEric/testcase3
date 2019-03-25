package com.tng.portal.email.service.impl;

import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.DateUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.email.EMailInput;
import com.tng.portal.email.entity.*;
import com.tng.portal.email.exception.ErrorCode;
import com.tng.portal.email.repository.*;
import com.tng.portal.email.service.EmailAccountService;
import com.tng.portal.email.service.EmailService;
import com.tng.portal.email.util.FileUtil;
import com.tng.portal.email.vo.EmailAccountDto;
import com.tng.portal.email.vo.EmailAttachmentDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Owen on 2016/11/17.
 */
@Service("emailService")
@Transactional
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private EmailMessageRepository emailMessageRepository;

    @Autowired
    private EmailContentRepository emailContentRepository;

    @Autowired
    private EmailRecipientRepository emailRecipientRepository;

    @Autowired
    private EmailAccountQuotaRepository emailAccountQuotaRepository;

    @Autowired
    private EmailAttachmentRepository emailAttachmentRepository;

    @Autowired
    private EmailAccountRepository emailAccountRepository;

    @Autowired
    private EmailAccountService emailAccountService;

    private static final int MAX_SEND_TOTAL_COUNT = 20;

    private static final int MAX_RESEND_COUNT = 3;

    private static String currentDate = null;

    /**
     * According to the job query has been sent mailing information list
     * @param job email template code
     * @return
     */
    @Override
    public List<EmailMessage> getEmailMessages(String job) {
        return emailMessageRepository.findByJob(job);
    }

    @Override
    public void sendEmailByMQ(EMailInput emailInput) {
    	emailInput.setContentType("HTM");
        Assert.notNull(emailInput, "error");
        if (StringUtils.isBlank(emailInput.getSubject())) {
            return;
        }
        if (StringUtils.isBlank(emailInput.getReceivers())) {
            return;
        }
        if (StringUtils.isBlank(emailInput.getContent())) {
            return;
        }
        try {
            String serviceName = PropertiesUtil.getAppValueByKey("soa.server.name");
            String methodName =  PropertiesUtil.getAppValueByKey("soa.method.name");
            RabbitMQUtil.sendRequestToSOAService( serviceName, methodName, MqParam.gen(emailInput.getSubject()), MqParam.gen(emailInput.getReceivers()), MqParam.gen(emailInput.getContent()));
        }catch (Exception e){
            logger.error("email send error:" + e);
            throw new BusinessException(104001, "fail");
        }
    }

    private static final String REGEX_EMAIL = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    /**
     *
     * @param emailInput
     */
    @Override
    public void sendEmail(EMailInput emailInput) {
        if (null == emailInput || StringUtils.isBlank(emailInput.getSubject())
                || StringUtils.isBlank(emailInput.getContent()) || StringUtils.isBlank(emailInput.getReceivers())) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode());
        }
        if (!emailFormatValidate(emailInput.getReceivers())
                || (StringUtils.isNotBlank(emailInput.getSender()) && !emailFormatValidate(emailInput.getSender()))) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode());
        }
        List<EmailAttachmentDto> attachmentList = null;
        try {
            attachmentList = downloadEmailAttachments(emailInput.getAttachmentsUrl());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(ErrorCode.INVALID_PARAMETER.getCode());
        }
        long totalSize = 0;
        if (!attachmentList.isEmpty()) {
            Optional<Long> reduce = attachmentList.stream().map(EmailAttachmentDto::getFileSize).reduce((o1, o2) -> o1 + o2);//sonar modify
            if(reduce.isPresent()){
                totalSize =reduce.get();//sonar modify 
            }
            totalSize = totalSize / 1024 / 1024;//MB
            EmailAccountDto emailAccount = getEmailAccount(emailInput.getSmtpGateway(), totalSize);
            if (null == emailAccount) {
                throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
            }
        }
        if (StringUtils.isBlank(emailInput.getSender())) {
            emailInput.setSender(" ");
        }
        emailInput.setContentType("HTM");

        EmailContent emailContent = saveEmailContent(emailInput, attachmentList);
        if (null != emailContent && null != emailContent.getEmailRecipients() && !emailContent.getEmailRecipients().isEmpty()) {
            int count = emailContent.getEmailRecipients().size();
            if (count > MAX_SEND_TOTAL_COUNT) {
                count = MAX_SEND_TOTAL_COUNT;
            }
            for (int i = 0; i < count; i++) {
                EmailRecipient recipient = emailContent.getEmailRecipients().get(i);
                send(emailInput.getSmtpGateway(), totalSize, recipient);
            }
        }


    }

    private static boolean emailFormatValidate(String emails) {
        boolean tag = true;
        if (StringUtils.isBlank(emails)){
            return false;
        }
        String[] list = emails.split(",");
        for (String mail : list) {
            Pattern regex = Pattern.compile(REGEX_EMAIL);
            Matcher matcher = regex.matcher(mail);
            tag = matcher.matches();
            if (!tag) {
                break;
            }
        }
        return tag;
    }

    private List<EmailAttachmentDto> downloadEmailAttachments(List<String> attachmentsUrl) throws IOException {
        if(null != attachmentsUrl && !attachmentsUrl.isEmpty()){
            List<EmailAttachmentDto> attachmentList = new ArrayList<>();
            for (String url : attachmentsUrl) {
                File file = FileUtil.downLoadFromUrl(url);
                if(null != file){
                    EmailAttachmentDto attachmentDto = new EmailAttachmentDto();
                    attachmentDto.setFileName(file.getName());
                    attachmentDto.setFilePath(file.getPath());
                    attachmentDto.setFileSize(file.length());
                    attachmentList.add(attachmentDto);
                }
            }
            return attachmentList;
        }
        return new ArrayList<>();
    }

    private EmailContent saveEmailContent(EMailInput emailInput, List<EmailAttachmentDto> attachmentList) {
        try {
            Assert.notNull(emailInput);
            EmailContent emailContent = new EmailContent();
            emailContent.setSenderEmail(emailInput.getSender());
            emailContent.setSubject(emailInput.getSubject());
            if (null != emailInput.getScheduleSendTime() && emailInput.getScheduleSendTime().longValue() > 0) {
                Date date = new Date(emailInput.getScheduleSendTime());
                if(date.after(new Date())){
                    emailContent.setScheduleSendTime(new Date(emailInput.getScheduleSendTime()));
                }
            }
            emailContent.setContent(emailInput.getContent());
            emailContent.setCreateDate(new Date());
            emailContent.setStatus("NEW");
            emailContent.setApplicationCode(emailInput.getApplicationCode());
            emailContent.setContentType(emailInput.getContentType());
            EmailContent content = emailContentRepository.save(emailContent);

            List<EmailRecipient> recipients = new ArrayList<>();
            if (StringUtils.isNotBlank(emailInput.getReceivers())) {
                String[] receivers = emailInput.getReceivers().split(",");
                for (String receiver : receivers) {
                    EmailRecipient recipient = new EmailRecipient();
                    recipient.setEmailContent(content);
                    recipient.setRecipientEmail(receiver);
                    recipient.setSendType("NOR");
                    recipient.setStatus("NEW");
                    recipient = emailRecipientRepository.save(recipient);
                    recipients.add(recipient);
                }
                content.setEmailRecipients(recipients);
            }

            List<EmailAttachment> emailAttachments = new ArrayList<>();
            if (null != attachmentList && !attachmentList.isEmpty()) {
                for (EmailAttachmentDto attachment : attachmentList) {
                    EmailAttachment emailAttachment = new EmailAttachment();
                    emailAttachment.setEmailContent(content);
                    emailAttachment.setAttachmentName(attachment.getFileName());
                    emailAttachment.setAttachmentPath(attachment.getFilePath());
                    emailAttachmentRepository.save(emailAttachment);
                    emailAttachments.add(emailAttachment);
                }
                content.setEmailAttachments(emailAttachments);
            }

            return content;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(ErrorCode.EMAIL_SEND_ERROR.getCode());
        }
    }

    @Override
    public EmailAccountDto getEmailAccount(String hostCode,Long hostSizeLimit) {
        if(StringUtils.isBlank(currentDate)
                ||!DateUtils.formatDate(new Date(),DateCode.dateFormatDd).equals(currentDate)){
            Calendar calendar = Calendar.getInstance();
            //D & M
            emailAccountQuotaRepository.updateEmailAccountQuota("D");
            Date preDate = DateUtils.parseDate(currentDate, DateCode.dateFormatDd);
            if(preDate==null||calendar.get(Calendar.MONTH)!=preDate.getMonth()){
                emailAccountQuotaRepository.updateEmailAccountQuota("M");
            }
            currentDate = DateUtils.formatDate(calendar.getTime(),DateCode.dateFormatDd);
        }
        EmailAccount emailAccount = emailAccountService.getEmailAccount(hostCode,hostSizeLimit);
        if(emailAccount == null){
            if(hostSizeLimit == null || hostSizeLimit.intValue() <= 0){
                throw new BusinessException(ErrorCode.EMAIL_GATEWAY_NOT_EXIST_ERROR.getCode());
            }else{
                throw new BusinessException(ErrorCode.EMAIL_ATTACHMENTS_MAX_SIZE_ERROR.getCode());
            }
        }else{
            EmailAccountDto emailAccountDto = new EmailAccountDto();
            emailAccountDto.setDefaultSenderEmail(emailAccount.getDefault_sender_email());
            emailAccountDto.setPassword(emailAccount.getPassword());
            emailAccountDto.setAccount(emailAccount.getAccount());
            emailAccountDto.setSecureType(emailAccount.getEmailHost().getSecure_type());
            emailAccountDto.setPort(emailAccount.getEmailHost().getPort());
            emailAccountDto.setRequireAuth(emailAccount.getEmailHost().getRequire_auth());
            emailAccountDto.setHost2(emailAccount.getEmailHost().getHost2());
            emailAccountDto.setHost1(emailAccount.getEmailHost().getHost1());
            emailAccountDto.setAccountId(emailAccount.getId());
            return emailAccountDto;
        }
    }

    @Override
    public void sendEmails() {
        List<String> status = new ArrayList<>();
        //SENT - sent but waiting for server response result; QUE- in queue to be sent; SUC - sent and success; FAIL - sent but failed. CAN - cancelled
        status.add("NEW");
        status.add("FAIL");
        Page<EmailRecipient> page = emailRecipientRepository.find(status, MAX_RESEND_COUNT,new PageRequest(0, MAX_SEND_TOTAL_COUNT));
       if(null != page && null != page.getContent() && !page.getContent().isEmpty()){
           List<EmailRecipient> list = page.getContent();
           for (int i = 0; i < list.size(); i++) {
               EmailRecipient recipient = list.get(i);
               List<EmailAttachment>  emailAttachments = recipient.getEmailContent().getEmailAttachments();
               long totalSize = 0;
               if(null != emailAttachments && !emailAttachments.isEmpty()){
                    for(EmailAttachment emailAttachment : emailAttachments){
                        File file = new File(emailAttachment.getAttachmentPath());
                        if(null != file){
                            totalSize+=file.length();
                        }
                    }
               }
               totalSize = totalSize/1024/1024;//mb
               try {
                   send(null, totalSize, recipient);
               }catch (Exception e){
                   logger.error(e.getMessage(), e);
               }
           }
       }
    }

    private void send(String smtpGateway, long totalSize, EmailRecipient emailRecipient) {
        EmailAccountDto emailAccount = null;
        try {
            emailAccount = this.getEmailAccount(smtpGateway, totalSize);
            Assert.notNull(emailAccount);
            if (StringUtils.isBlank(emailAccount.getAccount()) || StringUtils.isBlank(emailAccount.getPassword())
                    || (StringUtils.isBlank(emailAccount.getHost1()) && StringUtils.isBlank(emailAccount.getHost2()))) {
                throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);//sonar modify
            throw new BusinessException(ErrorCode.EMAIL_ACCOUNT_NOT_EXIST_ERROR.getCode());
        }


        try {
            EmailRecipient recipient = com.tng.portal.email.util.MailUtil.sendEmail(emailAccount, emailRecipient);
            recipient = emailRecipientRepository.save(recipient);
            if (recipient.getEmailContent().getEmailRecipients().stream().allMatch(item -> "SUC".equals(item.getStatus()) || "FAIL".equals(item.getStatus()))) {
                EmailContent emailContent = recipient.getEmailContent();
                if (StringUtils.isNotBlank(emailAccount.getDefaultSenderEmail())) {
                    emailContent.setSenderEmail(emailAccount.getDefaultSenderEmail());
                } else {
                    emailContent.setSenderEmail(emailRecipient.getEmailContent().getSenderEmail());
                }
                emailContent.setStatus("SENT");
                emailContent.setStatusChgTime(new Date());
                emailContentRepository.save(emailContent);
            }
            saveSendCounter(emailAccount.getAccountId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void saveSendCounter(Long emailAccountId) {
        try {
            EmailAccountQuota emailAccountQuota = emailAccountQuotaRepository.findOne(emailAccountId);
            Assert.notNull(emailAccountQuota);
            emailAccountQuota.setSend_counter(Long.valueOf(emailAccountQuota.getSend_counter().intValue() + 1L));//sonar modify
            emailAccountQuotaRepository.save(emailAccountQuota);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
