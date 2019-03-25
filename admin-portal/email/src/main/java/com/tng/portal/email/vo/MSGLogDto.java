package com.tng.portal.email.vo;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by dong on 2017/10/27.
 */

public class MSGLogDto implements Serializable{
    private Long requestId;//EMAIL_CONTENT.ID
    private String requestReceivedDate;//EMAIL_CONTENT.SCHEDULE_SEND_TIME
    private String requestReceivedTime;//EMAIL_CONTENT.SCHEDULE_SEND_TIME
    private String module;//EMAIL_CONTENT.APPLICATION_CODE
    private String loginId;
    private String login;//EMAIL_ACCOUNT.ACCOUNT
    private String gateway;//EMAIL_ACCOUNT.EMAIL_HOST_CODE
    private String sender;//EMAIL_CONTENT.SENDER_EMAIL
    private String recipient;//EMAIL_RECIPIENT.RECIPIENT_EMAIL
    private String content;//EMAIL_CONTENT.CONTENT
    private HashMap<String,String> attachment;//EMAIL_ATTACHMENT.ATTACHMENT_NAME
    private String status;//EMAIL_CONTENT.STATUS
    private String responseCode;//EMAIL_RECIPIENT.SERVER_RESPONSE_CODE
    private String responseContent;//EMAIL_RECIPIENT.SERVER_RESPONSE_MSG
    private String responseTime;//EMAIL_RECIPIENT.SERVER_RESPONSE_TIMESTAMP
    private int reSendRetryCount;//EMAIL_RECIPIENT.RESEND_COUNT


    public MSGLogDto() {

    }


	public Long getRequestId() {
		return requestId;
	}


	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}


	public String getRequestReceivedDate() {
		return requestReceivedDate;
	}


	public void setRequestReceivedDate(String requestReceivedDate) {
		this.requestReceivedDate = requestReceivedDate;
	}


	public String getRequestReceivedTime() {
		return requestReceivedTime;
	}


	public void setRequestReceivedTime(String requestReceivedTime) {
		this.requestReceivedTime = requestReceivedTime;
	}


	public String getModule() {
		return module;
	}


	public void setModule(String module) {
		this.module = module;
	}


	public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getGateway() {
		return gateway;
	}


	public void setGateway(String gateway) {
		this.gateway = gateway;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public HashMap<String,String> getAttachment() {
		return attachment;
	}


	public void setAttachment(HashMap<String,String> attachment) {
		this.attachment = attachment;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getResponseCode() {
		return responseCode;
	}


	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}


	public String getResponseContent() {
		return responseContent;
	}


	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}


	public String getResponseTime() {
		return responseTime;
	}


	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}


	public int getReSendRetryCount() {
		return reSendRetryCount;
	}


	public void setReSendRetryCount(int reSendRetryCount) {
		this.reSendRetryCount = reSendRetryCount;
	}
    
    

}
