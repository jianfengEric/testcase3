package com.tng.portal.common.vo.sms;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SMSProviderDto implements Serializable {
	private String id;
	
	private String providerName;
	
	private String endpointUrl;
	
	private String httpMethod;
	
	private String username;
	
	private String password;
	
	private boolean longSMS;
	
	private boolean specialCharacter;
	
	private boolean sendForeignCountry;
	
	private boolean fastSMS;
	
	private String status;
	
	private String remark;
	
	private int priority;
	
	private String healthStatus;
	
	private String applicationCode;
	
	private Date lastUsedTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLongSMS() {
		return longSMS;
	}

	public void setLongSMS(boolean longSMS) {
		this.longSMS = longSMS;
	}

	public boolean isSpecialCharacter() {
		return specialCharacter;
	}

	public void setSpecialCharacter(boolean specialCharacter) {
		this.specialCharacter = specialCharacter;
	}

	public boolean isSendForeignCountry() {
		return sendForeignCountry;
	}

	public void setSendForeignCountry(boolean sendForeignCountry) {
		this.sendForeignCountry = sendForeignCountry;
	}

	public boolean isFastSMS() {
		return fastSMS;
	}

	public void setFastSMS(boolean fastSMS) {
		this.fastSMS = fastSMS;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public Date getLastUsedTime() {
		return lastUsedTime;
	}

	public void setLastUsedTime(Date lastUsedTime) {
		this.lastUsedTime = lastUsedTime;
	}

}
