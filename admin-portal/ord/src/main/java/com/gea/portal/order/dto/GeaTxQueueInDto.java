package com.gea.portal.order.dto;

import com.google.gson.annotations.SerializedName;

import javax.persistence.Transient;

public class GeaTxQueueInDto {

	@SerializedName(value = "geaRefId")
	private String transferId;

	@SerializedName(value = "participant.refId")
	private String participantId;

	@SerializedName(value = "participant.name")
	private String participantName;

	@SerializedName(value = "quotation.tngIssuer.service.descEn")
	private String serviceType;

	@SerializedName(value = "status")
	private String status;

	@SerializedName(value = "geaStatusCode")
	private String geaStatus;

	@SerializedName(value = "csRemark")
	private String remark;

	@SerializedName(value = "createDatetime")
	private String submissionDateTime;

	@SerializedName(value = "updateDatetime")
	private String updateTime;

	private String participantIdByLogin;


	@Transient
	private String beginSubmissionDateTime;

	@Transient
	private String endSubmissionDateTime;

	@Transient
	private String beginUpdateTime;

	@Transient
	private String endUpdateTime;


	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
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

	public String getBeginSubmissionDateTime() {
		return beginSubmissionDateTime;
	}

	public void setBeginSubmissionDateTime(String beginSubmissionDateTime) {
		this.beginSubmissionDateTime = beginSubmissionDateTime;
	}

	public String getEndSubmissionDateTime() {
		return endSubmissionDateTime;
	}

	public void setEndSubmissionDateTime(String endSubmissionDateTime) {
		this.endSubmissionDateTime = endSubmissionDateTime;
	}

	public String getBeginUpdateTime() {
		return beginUpdateTime;
	}

	public void setBeginUpdateTime(String beginUpdateTime) {
		this.beginUpdateTime = beginUpdateTime;
	}

	public String getEndUpdateTime() {
		return endUpdateTime;
	}

	public void setEndUpdateTime(String endUpdateTime) {
		this.endUpdateTime = endUpdateTime;
	}

	public String getSubmissionDateTime() {
		return submissionDateTime;
	}

	public void setSubmissionDateTime(String submissionDateTime) {
		this.submissionDateTime = submissionDateTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getParticipantIdByLogin() {
		return participantIdByLogin;
	}

	public void setParticipantIdByLogin(String participantIdByLogin) {
		this.participantIdByLogin = participantIdByLogin;
	}

	public String getGeaStatus() {
		return geaStatus;
	}

	public void setGeaStatus(String geaStatus) {
		this.geaStatus = geaStatus;
	}
}