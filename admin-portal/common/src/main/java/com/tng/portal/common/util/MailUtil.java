package com.tng.portal.common.util;


import com.tng.portal.common.vo.email.EMailInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;


public class MailUtil {

	private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

	private static final String SERVER_HOST = PropertiesUtil.getMailValueByKey("email.server.host");

	private static final String SERVER_PORT = PropertiesUtil.getMailValueByKey("email.server.port");

	private static final String ACCOUNT = PropertiesUtil.getMailValueByKey("email.sender.account");

	private static final String PASSWORD = PropertiesUtil.getMailValueByKey("email.sender.password");

	private static final String FALSE = "false";
	
	public static void sendHtmlMail(EMailInput eMailInput) {
		Transport transport = null;
		try {
			if(!eMailInput.isValid()){
				return;
			}
			Session session = getSession();
			MimeMessage mailMessage = new MimeMessage(session);
			//  Property represents the type of the receiver. Setting sender for mail message 
			mailMessage.setFrom(new InternetAddress(ACCOUNT));
			// Message.RecipientType.TO Property represents the type of the receiver. TO
			mailMessage.addRecipients(Message.RecipientType.TO, eMailInput.getReceivers());
			//  Setting the theme of mail messages 
			mailMessage.setSubject(eMailInput.getSubject(), "utf-8");
			//  Set the time to send mail messages. 
			mailMessage.setSentDate(new Date());
			// MiniMultipart Class is a container class that contains MimeBodyPart Object of type 
			Multipart mainPart = new MimeMultipart();
			//  Create a containing HTML Content MimeBodyPart
			BodyPart html = new MimeBodyPart();
			//  Set up HTML content
			html.setContent(eMailInput.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
		/*	if (null != eMailInput.getAttachFiles() && eMailInput.getAttachFiles().size() > 0) {
				for (File file : eMailInput.getAttachFiles()) {
					//  Add attachments 
					BodyPart messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(file.getPath());
					//  Add the contents of the attachment 
					messageBodyPart.setDataHandler(new DataHandler(source));
					//  Add appendix title 
					//  This is very important. Base64 The encoding conversion ensures that your Chinese appendix title will not become garbled when it is sent. 
					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					messageBodyPart.setFileName("=?GBK?B?" + enc.encode(file.getName().getBytes()) + "?=");
					mainPart.addBodyPart(messageBodyPart);
				}
			}*/
			//  take MiniMultipart Object is set to mail content. 
			mailMessage.setContent(mainPart);
			//  Save mail 
			mailMessage.saveChanges();
			transport = session.getTransport();
			transport.connect();
			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		} catch (MessagingException ex) {
			logger.error("MessagingException",ex);
		}finally {
			if(null != transport){
				try {
					transport.close();
				} catch (MessagingException e) {
					logger.error("MessagingException",e);
				}
			}
		}
	}
    
	private static void sendSimpleEmail(EMailInput input){
		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress(ACCOUNT));
			message.setSubject(input.getSubject());
			message.setText(input.getContent());

			Transport.send(message);
			
		} catch (MessagingException e) {
			logger.info("Sending email exception " + e.getMessage());
		}
	}
	

	private static Session getSession(){
		return Session.getInstance(getProperties(),
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(ACCOUNT, PASSWORD);
					}
				}
		);
	}

	private static Properties getProperties(){
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable",FALSE);
		props.setProperty("mail.debug",FALSE);
		props.setProperty("mail.smtp.timeout",FALSE);
		props.setProperty("mail.smtp.host", SERVER_HOST);
		props.setProperty("mail.smtp.port", SERVER_PORT);
		
		return props;
	}
	
	
}
