package com.tng.portal.sms.vo;

import java.io.Serializable;
import java.util.Date;

public class SMSJobDetailDto implements Serializable{
	private Long id;

	private String mobileNumber;
	
	private String status;
	
	private Date scheduleTime;
	
	private Date statusChgTimestamp;
	
	private Date sentTimestamp;
	
	private Date responseTimestamp;
	
	private String providerResponse;
	
	private String systemResponse;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Date getStatusChgTimestamp() {
		return statusChgTimestamp;
	}

	public void setStatusChgTimestamp(Date statusChgTimestamp) {
		this.statusChgTimestamp = statusChgTimestamp;
	}

	public Date getSentTimestamp() {
		return sentTimestamp;
	}

	public void setSentTimestamp(Date sentTimestamp) {
		this.sentTimestamp = sentTimestamp;
	}

	public Date getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(Date responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public String getProviderResponse() {
		return providerResponse;
	}

	public void setProviderResponse(String providerResponse) {
		this.providerResponse = providerResponse;
	}

	public String getSystemResponse() {
		return systemResponse;
	}

	public void setSystemResponse(String systemResponse) {
		this.systemResponse = systemResponse;
	}

	
}
