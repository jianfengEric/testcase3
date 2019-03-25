package com.tng.portal.sms.server.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tng.portal.common.entity.AnaApplication;

@Entity
@Table(name = "SMS_JOB")
@JsonInclude(Include.NON_NULL)
public class SMSJob implements Cloneable {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_sms_job", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_sms_job", sequenceName="seq_sms_job", allocationSize=1)
	private Long id;
	
	@Column(name="reference_id", unique=true, nullable=false, length=7)
	private String referenceId;
	
	@ManyToOne
	@JoinColumn(name="sms_provider_id")
	private SMSProvider smsProvider;
	
	@Column(name="SENDER_ACCOUNT_ID")
	private String senderAccountId;

	@ManyToOne
	@JoinColumn(name="APPLICATION_CODE")
	private AnaApplication anaApplication;
	
	@Column(name="CONTENT", nullable=false, length=600)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="original_job_id")
	private SMSJob originalJob;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable=false)
	private Date createDate;

	@OneToMany(mappedBy ="job", fetch = FetchType.EAGER)
	private List<SMSJobDetail> jobDetails;
	
	@Column(name="STATUS", nullable=false, length=10)
	private String status;
	
	@Column(name="TERMINATE_REASON", nullable=false, length=500)
	private String terminateReason;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public SMSProvider getSmsProvider() {
		return smsProvider;
	}

	public void setSmsServiceProvider(SMSProvider smsProvider) {
		this.smsProvider = smsProvider;
	}

	public AnaApplication getAnaApplication() {
		return anaApplication;
	}

	public void setAnaApplication(AnaApplication anaApplication) {
		this.anaApplication = anaApplication;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SMSJob getOriginalJob() {
		return originalJob;
	}

	public void setOriginalJob(SMSJob originalJob) {
		this.originalJob = originalJob;
	}

	public List<SMSJobDetail> getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(List<SMSJobDetail> jobDetails) {
		this.jobDetails = jobDetails;
	}

	public String getSenderAccountId() {
		return senderAccountId;
	}

	public void setSenderAccountId(String senderAccountId) {
		this.senderAccountId = senderAccountId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminateReason() {
		return terminateReason;
	}

	public void setTerminateReason(String terminateReason) {
		this.terminateReason = terminateReason;
	}

	public void setSmsProvider(SMSProvider smsProvider) {
		this.smsProvider = smsProvider;
	}
}
