package com.gea.portal.ewp.entity;

import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the request_approval database table.
 * 
 */
@Entity
@Table(name="request_approval")
@NamedQuery(name="RequestApproval.findAll", query="SELECT a FROM RequestApproval a")
public class RequestApproval implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private RequestApprovalStatus status;

	@Column(name="status_reason")
	private String statusReason;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Column(name="create_date"/*, insertable = false*/, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name="create_by")
	private String createBy;

	@Column(name = "ewp_company_form_id")
	private String ewpCompanyFormId;

	@Column(name = "ewp_service_id")
	private String ewpServiceId;
	
	@Column(name= "ewp_service_settlement_id")
	private String ewpServiceSettlementId;

	@Column(name = "ewp_money_pool_id")
	private String ewpMoneyPoolId;

	@Column(name = "ewp_gateway_config_id")
	private String ewpGatewayConfigId;

	@Column(name = "ewp_status_change_id")
	private String ewpStatusChangeId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="participant_id")
	private EwalletParticipant ewalletParticipant;

	@Column(name = "approval_type")
	@Enumerated(EnumType.STRING)
	private ApprovalType approvalType;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name = "request_remark")
	private String requestRemark;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EwalletParticipant getEwalletParticipant() {
		return ewalletParticipant;
	}

	public void setEwalletParticipant(EwalletParticipant ewalletParticipant) {
		this.ewalletParticipant = ewalletParticipant;
	}

	public ApprovalType getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(ApprovalType approvalType) {
		this.approvalType = approvalType;
	}
	
	public String getEwpServiceSettlementId() {
		return ewpServiceSettlementId;
	}

	public void setEwpServiceSettlementId(String ewpServiceSettlementId) {
		this.ewpServiceSettlementId = ewpServiceSettlementId;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}