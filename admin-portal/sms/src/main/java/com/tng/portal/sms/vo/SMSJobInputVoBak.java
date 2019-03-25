package com.tng.portal.sms.vo;

import java.util.Date;

import com.tng.portal.sms.entity.QuestionTemplate;
import com.tng.portal.sms.entity.QuestionType;
import com.tng.portal.sms.entity.SMSServiceProvider;

public class SMSJobInputVoBak {
	
	private QuestionType type;
	
	private QuestionTemplate question;
	
	private String content;
	
	private String senderAccount;
	
	private String senderName;
	
	private Date sendTime;
	
	private String mobileNumbers;
	
	private SMSServiceProvider serviceProvider;
	
	private boolean isTest;
	
	private Long originalId;

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public QuestionTemplate getQuestion() {
		return question;
	}

	public void setQuestion(QuestionTemplate question) {
		this.question = question;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderAccount() {
		return senderAccount;
	}

	public void setSenderAccount(String senderAccount) {
		this.senderAccount = senderAccount;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getMobileNumbers() {
		return mobileNumbers;
	}

	public void setMobileNumbers(String mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

	public SMSServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(SMSServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public boolean isTest() {
		return isTest;
	}

	public void setTest(boolean isTest) {
		this.isTest = isTest;
	}

	public Long getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}

}
