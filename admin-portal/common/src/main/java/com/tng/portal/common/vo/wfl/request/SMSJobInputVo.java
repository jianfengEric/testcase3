package com.tng.portal.common.vo.wfl.request;

import java.util.Date;

public class SMSJobInputVo {
	
	/** SMS content */
	private String content;
	
	/** sender's account */
	private String senderAccount;
	
	/** sender's name  */
	private String senderName;
	
	private Date sendTime;
	
	/**  recipient's mobile numbers, separated by commas */
	private String mobileNumbers;
	
	/** is it a test SMS */
	private boolean isTest;
	
	/**  If the message fails, the page is resold. originalId For the original Job id, Otherwise, null */
	private Long originalId;
	
	private String applicationCode;
	
	private String createBy;
	
	/** is it a fast SMS */
	private boolean fastSMS;
	
	private String uploadFileName;

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

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public boolean isFastSMS() {
		return fastSMS;
	}

	public void setFastSMS(boolean fastSMS) {
		this.fastSMS = fastSMS;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
