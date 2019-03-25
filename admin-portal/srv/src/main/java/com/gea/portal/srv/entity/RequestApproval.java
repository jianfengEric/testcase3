package com.gea.portal.srv.entity;

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
public class RequestApproval implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "batch_id")
	private ServiceBatchChgReq serviceBatchChgReq;

	@Column(name="approval_type")
	private String approvalType;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private RequestApprovalStatus status;

	@Column(name="status_reason")
	private String statusReason;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Column(name="create_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name="create_by")
	private String createBy;

	@Column(name="update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name="update_by")
	private String updateBy;
	
	@Column(name="request_remark")
	private String requestRemark;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public ServiceBatchChgReq getServiceBatchChgReq() {
		return serviceBatchChgReq;
	}

	public void setServiceBatchChgReq(ServiceBatchChgReq serviceBatchChgReq) {
		this.serviceBatchChgReq = serviceBatchChgReq;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}