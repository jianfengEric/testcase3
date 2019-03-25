package com.tng.portal.common.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;

/**
 * approval时从各模块传递数据到apv 
 */
public class RequestApprovalDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private ApprovalType approvalType;
	private RequestApprovalStatus status;
	private String statusReason;
	private Instance currentEnvir;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createDate;
	private String createBy;
	private String createUserName;
	private Date updateDate;
	private String updateBy;
	
	private String participantName;
	
	private String participantId;
	private String geaParticipantRefId;
	
	private String ewpMoneyPoolId;
	private String geaMoneyPoolRefId;
	
	private String ewpCompanyFormId;
	private String ewpServiceId;
	private String ewpServiceSettlementId;
	private String ewpGatewayConfigId;
	private String ewpStatusChangeId;
	
	private String ewpMpChangeReqId;
	private String ewpPoolAdjustId;
	private String ewpDepositCashoutId;
	
	private String exchRateFileId;
	
	private String batchId;
	
	private String requestRemark;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ApprovalType getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(ApprovalType approvalType) {
		this.approvalType = approvalType;
	}
	public RequestApprovalStatus getStatus() {
		return status;
	}
	public void setStatus(RequestApprovalStatus status) {
		this.status = status;
	}
	public String getStatusReason() {
		return statusReason;
	}
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	public Instance getCurrentEnvir() {
		return currentEnvir;
	}
	public void setCurrentEnvir(Instance currentEnvir) {
		this.currentEnvir = currentEnvir;
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
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public String getGeaParticipantRefId() {
		return geaParticipantRefId;
	}
	public void setGeaParticipantRefId(String geaParticipantRefId) {
		this.geaParticipantRefId = geaParticipantRefId;
	}
	public String getEwpCompanyFormId() {
		return ewpCompanyFormId;
	}
	public void setEwpCompanyFormId(String ewpCompanyFormId) {
		this.ewpCompanyFormId = ewpCompanyFormId;
	}
	public String getEwpServiceId() {
		return ewpServiceId;
	}
	public void setEwpServiceId(String ewpServiceId) {
		this.ewpServiceId = ewpServiceId;
	}
	public String getEwpServiceSettlementId() {
		return ewpServiceSettlementId;
	}
	public void setEwpServiceSettlementId(String ewpServiceSettlementId) {
		this.ewpServiceSettlementId = ewpServiceSettlementId;
	}
	public String getEwpMoneyPoolId() {
		return ewpMoneyPoolId;
	}
	public void setEwpMoneyPoolId(String ewpMoneyPoolId) {
		this.ewpMoneyPoolId = ewpMoneyPoolId;
	}
	public String getEwpGatewayConfigId() {
		return ewpGatewayConfigId;
	}
	public void setEwpGatewayConfigId(String ewpGatewayConfigId) {
		this.ewpGatewayConfigId = ewpGatewayConfigId;
	}
	public String getEwpStatusChangeId() {
		return ewpStatusChangeId;
	}
	public void setEwpStatusChangeId(String ewpStatusChangeId) {
		this.ewpStatusChangeId = ewpStatusChangeId;
	}
	public String getEwpMpChangeReqId() {
		return ewpMpChangeReqId;
	}
	public void setEwpMpChangeReqId(String ewpMpChangeReqId) {
		this.ewpMpChangeReqId = ewpMpChangeReqId;
	}
	public String getEwpPoolAdjustId() {
		return ewpPoolAdjustId;
	}
	public void setEwpPoolAdjustId(String ewpPoolAdjustId) {
		this.ewpPoolAdjustId = ewpPoolAdjustId;
	}
	public String getEwpDepositCashoutId() {
		return ewpDepositCashoutId;
	}
	public void setEwpDepositCashoutId(String ewpDepositCashoutId) {
		this.ewpDepositCashoutId = ewpDepositCashoutId;
	}
	public String getExchRateFileId() {
		return exchRateFileId;
	}
	public void setExchRateFileId(String exchRateFileId) {
		this.exchRateFileId = exchRateFileId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getGeaMoneyPoolRefId() {
		return geaMoneyPoolRefId;
	}
	public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
		this.geaMoneyPoolRefId = geaMoneyPoolRefId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getRequestRemark() {
		return requestRemark;
	}
	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
	
}
