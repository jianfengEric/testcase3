package com.tng.portal.ana.vo;

import java.util.Date;
import java.util.List;

public class SMSJobQueryVo {
	
	private Long id;
	
	private String referenceId;
	
	private String jobId;
	
	private String content;
	
	private String senderId;
	
	private String senderName;
	
	private Date createDate;
	
	private String mobileNumbers;
	
	private int successCount;
	
	private int failCount;
	
	private int resendCount;
	
	private int totalCount;
	
	private List<String> failMobiles;
	
	private List<String> resendMobileNumbers;
	
	private String applicationCode;
	
	private Long originalId;
	
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getMobileNumbers() {
		return mobileNumbers;
	}

	public int getResendCount() {
		return resendCount;
	}

	public void setResendCount(int resendCount) {
		this.resendCount = resendCount;
	}

	public void setMobileNumbers(String mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<String> getFailMobiles() {
		return failMobiles;
	}

	public void setFailMobiles(List<String> failMobiles) {
		this.failMobiles = failMobiles;
	}

	public List<String> getResendMobileNumbers() {
		return resendMobileNumbers;
	}

	public void setResendMobileNumbers(List<String> resendMobileNumbers) {
		this.resendMobileNumbers = resendMobileNumbers;
	}

	public Long getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
