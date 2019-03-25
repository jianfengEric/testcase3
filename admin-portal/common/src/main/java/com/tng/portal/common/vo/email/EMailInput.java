package com.tng.portal.common.vo.email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class EMailInput {

	@ApiModelProperty(value="sender email")
	private String sender;
	@ApiModelProperty(value="email recipient, comma separated")
	private String receivers;
	@ApiModelProperty(value="email subject")
	private String subject;
	@ApiModelProperty(value="email content")
	private String content;
	@ApiModelProperty(value="email schedule send time, timestamp")
	private Long scheduleSendTime;
	@ApiModelProperty(value="application code")
	private String applicationCode;
	@ApiModelProperty(value="email smtp gateway code")
	private String smtpGateway;
	@ApiModelProperty(value="email attachment url list")
	private List<String> attachmentsUrl;
	@ApiModelProperty(value="email content type ,TXT-text/HTM-html")
	private String contentType;
	
	public EMailInput(){}
	
	public EMailInput(String subject, String receivers, String content){
		this.subject = subject;
		this.receivers = receivers;
		this.content = content;		
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	@JsonIgnore
	public boolean isValid(){
		if(subject != null && !subject.trim().equals("") && 
			content != null && !content.trim().equals("") &&	
			receivers != null && !receivers.trim().equals("")){
			
			return true;
		}
		return false;
	}
	
	public String getReceivers() {
		return receivers;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getScheduleSendTime() {
		return scheduleSendTime;
	}

	public void setScheduleSendTime(Long scheduleSendTime) {
		this.scheduleSendTime = scheduleSendTime;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getSmtpGateway() {
		return smtpGateway;
	}

	public void setSmtpGateway(String smtpGateway) {
		this.smtpGateway = smtpGateway;
	}

	public List<String> getAttachmentsUrl() {
		return attachmentsUrl;
	}

	public void setAttachmentsUrl(List<String> attachmentsUrl) {
		this.attachmentsUrl = attachmentsUrl;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
