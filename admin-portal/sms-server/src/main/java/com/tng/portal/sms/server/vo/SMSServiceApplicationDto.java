package com.tng.portal.sms.server.vo;

import java.io.Serializable;

public class SMSServiceApplicationDto implements Serializable {
	
	private String applicationCode;
	private String smsProviderId;
	private int priority;
	
	public String getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}
	public String getSmsProviderId() {
		return smsProviderId;
	}
	public void setSmsProviderId(String smsProviderId) {
		this.smsProviderId = smsProviderId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
