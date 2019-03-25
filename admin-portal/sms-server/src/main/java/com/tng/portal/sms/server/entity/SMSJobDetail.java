package com.tng.portal.sms.server.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SMS_JOB_DETAIL")
@JsonInclude(Include.NON_NULL)
public class SMSJobDetail {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_sms_job_detail", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_sms_job_detail", sequenceName="seq_sms_job_detail", allocationSize=1)
	private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SMS_JOB_ID")
	private SMSJob job;
	
	@Column(name="MOBILE_NUMBER", nullable=false, length=30)
	private String mobileNumber;
	
	@Column(name="STATUS", nullable=false, length=10)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="schedule_time")
	private Date scheduleTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="status_chg_timestamp")
	private Date statusChgTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sent_timestamp")
	private Date sentTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="response_timestamp")
	private Date responseTimestamp;
	
	@Column(name="PROVIDER_RESPONSE", length=1000)
	private String providerResponse;
	
	@Column(name="system_response", length=500)
	private String systemResponse;

	@ManyToOne
	@JoinColumn(name="sms_provider_id")
	private SMSProvider smsProvider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SMSJob getJob() {
		return job;
	}

	public void setJob(SMSJob job) {
		this.job = job;
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

	/**
	 *  When SMS is not sent, it is empty. 
	 * @return
	 */
	public SMSProvider getSmsProvider() {
		return smsProvider;
	}

	public void setSmsProvider(SMSProvider smsProvider) {
		this.smsProvider = smsProvider;
	}

}
