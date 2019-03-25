package com.tng.portal.sms.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SMS_PROVIDER")
@JsonInclude(Include.NON_NULL)
public class SMSProvider implements Cloneable {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	private String id;
	
	@Column(name="PROVIDER_NAME", unique=true, nullable=false, length=200)
	private String providerName;
	
	@Column(name="ENDPOINT_URL", unique=true, nullable=false, length=500)
	private String endpointUrl;
	
	@Column(name="HTTP_METHOD", nullable=false, length=4)
	private String httpMethod;
	
	@Column(name="USERNAME", length=50)
	private String username;
	
	@Column(name="PASSWORD", length=50)
	private String password;
	
	@Column(name="LONG_SMS", nullable=false, length=1)
	private boolean longSMS;
	
	@Column(name="SPECIAL_CHARACTER", nullable=false, length=1)
	private boolean specialCharacter;
	
	@Column(name="SEND_FOREIGN_COUNTRY", nullable=false, length=1)
	private boolean sendForeignCountry;
	
	@Column(name="FAST_SMS", nullable=false, length=1)
	private boolean fastSMS;
	
	@Column(name="STATUS", nullable=false, length=10)
	private String status;
	
	@Column(name="REMARK", length=500)
	private String remark;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE", nullable=false)
	private Date createDate;
	
	@Column(name="CREATE_BY", nullable=false, length=50)
	private String createBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	
	@Column(name="UPDATE_BY", length=50)
	private String updateBy;
	
	@JsonIgnore
	@OneToMany(cascade=CascadeType.MERGE,mappedBy = "smsProvider")
    private List<SMSServiceApplication> smsServiceApplications;

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public List<SMSServiceApplication> getSmsServiceApplications() {
		return smsServiceApplications;
	}

	public void setSmsServiceApplications(List<SMSServiceApplication> smsServiceApplications) {
		this.smsServiceApplications = smsServiceApplications;
	}
	
}
