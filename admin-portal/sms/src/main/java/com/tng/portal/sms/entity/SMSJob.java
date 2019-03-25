package com.tng.portal.sms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SMS_JOB")
@JsonInclude(Include.NON_NULL)
public class SMSJob {
	@Id
	@Column(name="ID", unique=true, nullable=false)
	@GeneratedValue(generator="seq_sms_job", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="seq_sms_job", sequenceName="seq_sms_job", allocationSize=1)
	private Long id;
	
	@Column(name="JOB_ID", unique=true, nullable=false, length=7)
	private String jobId;
	
	@Column(name="CONTENT", nullable=false, length=600)
	private String content;
	
	@ManyToOne
	@JoinColumn(name="SMS_SERVICE_PROVIDER_CODE")
	private SMSServiceProvider smsServiceProvider;
	
	@Column(name="SENDER_ACCOUNT", nullable=false, length=100)
	private String senderAccount;
	
	@Column(name="SENDER_NAME", nullable=false, length=100)
	private String senderName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SEND_TIME", nullable=false)
	private Date sendTime;
	
	@Column(name="IS_TEST", nullable=false, length=1)
	private boolean isTest;
	
	@OneToMany(mappedBy ="job", fetch = FetchType.EAGER)
	private List<SMSJobDetail> jobDetails;
	
	@Column(name="JOB_TYPE", nullable=false, length=10)
	private String jobType;
	
	@Column(name="ORIGINAL_ID", nullable=true)
	private Long originalId;
	
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
	public SMSServiceProvider getSmsServiceProvider() {
		return smsServiceProvider;
	}
	public void setSmsServiceProvider(SMSServiceProvider smsServiceProvider) {
		this.smsServiceProvider = smsServiceProvider;
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
	public boolean getIsTest() {
		return isTest;
	}
	public void setIsTest(boolean isTest) {
		this.isTest = isTest;
	}
	public List<SMSJobDetail> getJobDetails() {
		return jobDetails;
	}
	public void setJobDetails(List<SMSJobDetail> jobDetails) {
		this.jobDetails = jobDetails;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public Long getOriginalId() {
		return originalId;
	}
	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}
}
