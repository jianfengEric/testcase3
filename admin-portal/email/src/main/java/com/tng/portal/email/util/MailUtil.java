package com.tng.portal.email.util;


import com.sun.mail.smtp.SMTPTransport;
import com.tng.portal.email.entity.EmailAttachment;
import com.tng.portal.email.entity.EmailRecipient;
import com.tng.portal.email.vo.EmailAccountDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class MailUtil {

	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private MimeMessage mimeMessage;
	private Session session;
	private Multipart multipart;
	private Properties properties;

	/**
	 * Initialize MailSender with smtp server properties
	 *
	 * @throws Exception
	 */
	private MailUtil(EmailAccountDto emailAccount) throws Exception {
		initProperties(emailAccount);
		session = Session.getInstance(properties);
		mimeMessage = new MimeMessage(session);
		multipart = new MimeMultipart();
	}



	private boolean setSubject(String subject) {
		try {
			if (StringUtils.isBlank(subject)){
				return false;
			}
			mimeMessage.setSubject(subject);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	private static final String CONTENT_TYPE_PLAIN = "text/plain;charset=utf-8";
	private static final String CONTENT_TYPE_HTML = "text/html;charset=utf-8";

	private boolean setBody(String content, String type) {
		try {
			if (StringUtils.isBlank(content)) {
				return false;
			}
			BodyPart bodyPart = new MimeBodyPart();
			String contentType = CONTENT_TYPE_PLAIN; //TXT
			if(StringUtils.isNotBlank(type) && "HTM".equals(type)){
				contentType = CONTENT_TYPE_HTML;
			}
			bodyPart.setContent(content, contentType);
			multipart.addBodyPart(bodyPart);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	private boolean setFrom(String defaultSenderEmail, String senderEmail){
		if(StringUtils.isBlank(defaultSenderEmail) && StringUtils.isBlank(senderEmail)){
			return false;
		}
		try {
			if (StringUtils.isNotBlank(defaultSenderEmail)) {
				mimeMessage.setFrom(new InternetAddress(defaultSenderEmail));
			} else {
				mimeMessage.setFrom(new InternetAddress(senderEmail));
			}
			return true;
		} catch (AddressException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (MessagingException e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	private boolean setRecipients( String recipients, String sendType) {
		if(StringUtils.isBlank(recipients)){
			return false;
		}
		try {
			if(StringUtils.isNotBlank(sendType)){
				if("CC".equals(sendType)){
					mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(recipients));
				}else if("BCC".equals(sendType)){
					mimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(recipients));
				}else {
					mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
				}
			}else {
				mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	private boolean setAttachments(List<EmailAttachment> emailAttachments) {
		if (emailAttachments == null || emailAttachments.isEmpty()) {
			return true;
		}
		try {
			for (EmailAttachment emailAttachment : emailAttachments) {
				File file = new File(emailAttachment.getAttachmentPath());
				MimeBodyPart attachment = new MimeBodyPart();
				attachment.setDataHandler(new DataHandler(new FileDataSource(file)));
				attachment.setFileName(emailAttachment.getAttachmentName());
				multipart.addBodyPart(attachment);
			}
			return true;

		} catch (Exception e) {
			logger.error("MailSender.setAttachments Exception", e);
			return false;
		}
	}



	public static EmailRecipient sendEmail(EmailAccountDto emailAccount, EmailRecipient emailRecipient) {
		try {
			MailUtil mailUtil = new MailUtil(emailAccount);
			if(!mailUtil.setFrom(emailAccount.getDefaultSenderEmail(), emailRecipient.getEmailContent().getSenderEmail())){
				return failResponse(emailRecipient);
			}
			if(!mailUtil.setRecipients(emailRecipient.getRecipientEmail(), emailRecipient.getSendType())){
				return failResponse(emailRecipient);
			}
			if(!mailUtil.setSubject(emailRecipient.getEmailContent().getSubject())){
				return failResponse(emailRecipient);
			}
			if(!mailUtil.setBody(emailRecipient.getEmailContent().getContent(), emailRecipient.getEmailContent().getContentType())){
				return failResponse(emailRecipient);
			}
			if(!mailUtil.setAttachments(emailRecipient.getEmailContent().getEmailAttachments())){
				return failResponse(emailRecipient);
			}
			return mailUtil.send(emailAccount, emailRecipient);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return failResponse(emailRecipient);
	}

	private static EmailRecipient failResponse(EmailRecipient emailRecipient){
		if(!"NEW".equals(emailRecipient.getStatus())){
			emailRecipient.setResendCount(emailRecipient.getResendCount() + 1);
		}
		emailRecipient.setStatus("FAIL");
		emailRecipient.setSentTime(new Date());
		emailRecipient.setUpdateDate(new Date());
		emailRecipient.setStatusChgTime(new Date());
		return emailRecipient;
	}

	private EmailRecipient send(EmailAccountDto emailAccount, EmailRecipient emailRecipient) {
		try {
			mimeMessage.setSentDate(new Date());
			mimeMessage.setContent(multipart);
			mimeMessage.saveChanges();

			SMTPTransport transport = (SMTPTransport) session.getTransport();
			transport.connect(emailAccount.getAccount(), emailAccount.getPassword());
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

			int lastReturnCode = transport.getLastReturnCode();
			String lastServerResponse = transport.getLastServerResponse();

			transport.close();

			if(!"NEW".equals(emailRecipient.getStatus())){
				emailRecipient.setResendCount(emailRecipient.getResendCount() + 1);
			}
			emailRecipient.setServerResponseCode(String.valueOf(lastReturnCode));
			emailRecipient.setServerResponseMsg(lastServerResponse);
			emailRecipient.setServerResponseTimestamp(new Date());
			if(emailRecipient.getResendCount() > 0) {
				emailRecipient.setPrevAttemptSent(new Date());
			}
			if (lastReturnCode == 250) {
				emailRecipient.setStatus("SUC");
			} else {
				emailRecipient.setStatus("FAIL");
			}
		}  catch (Exception e) {
			logger.error(e.getMessage(), e);
			emailRecipient.setStatus("FAIL");
		}

		emailRecipient.setSentTime(new Date());
		emailRecipient.setUpdateDate(new Date());
		emailRecipient.setStatusChgTime(new Date());
		return emailRecipient;
	}

	private void initProperties(EmailAccountDto emailAccount) {
		properties = new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", emailAccount.getHost1());
		properties.setProperty("mail.smtp.port", String.valueOf(emailAccount.getPort()));
		if(null != emailAccount.getRequireAuth() && emailAccount.getRequireAuth().intValue() == 1) {
            properties.setProperty("mail.smtp.auth", "true");
        }else {
            properties.setProperty("mail.smtp.auth", "false");
        }
		//------- When needed SSL Add when validation 
		if (StringUtils.isNotBlank(emailAccount.getSecureType())) {
            if ("SSL".equals(emailAccount.getSecureType())) {
                final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
                properties.put("mail.smtp.socketFactory.class", SSL_FACTORY);
                properties.put("mail.smtp.socketFactory.fallback", "false");
                properties.put("mail.smtp.socketFactory.port", String.valueOf(emailAccount.getPort()));
            } else if ("TLS".equals(emailAccount.getSecureType())) {
                properties.setProperty("mail.smtp.starttls.enable", "true");
            }
        }
	}
	
	
}
