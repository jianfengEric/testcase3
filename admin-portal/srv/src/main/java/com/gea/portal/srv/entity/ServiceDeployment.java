package com.gea.portal.srv.entity;

import com.tng.portal.common.enumeration.DeployStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="service_deployment")
public class ServiceDeployment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="request_approval_id")
	private RequestApproval requestApproval;

	@Column(name="deploy_envir")
	private String deployEnvir;

	@Column(name="schedule_deploy_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduleDeployDate;

	@Column(name="deploy_note")
	private String deployNote;

	@Column(name="deploy_version_no")
	private String deployVersionNo;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private DeployStatus status;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeployEnvir() {
		return deployEnvir;
	}

	public void setDeployEnvir(String deployEnvir) {
		this.deployEnvir = deployEnvir;
	}

	public Date getScheduleDeployDate() {
		return scheduleDeployDate;
	}

	public void setScheduleDeployDate(Date scheduleDeployDate) {
		this.scheduleDeployDate = scheduleDeployDate;
	}

	public String getDeployNote() {
		return deployNote;
	}

	public void setDeployNote(String deployNote) {
		this.deployNote = deployNote;
	}

	public String getDeployVersionNo() {
		return deployVersionNo;
	}

	public void setDeployVersionNo(String deployVersionNo) {
		this.deployVersionNo = deployVersionNo;
	}

	public DeployStatus getStatus() {
		return status;
	}

	public void setStatus(DeployStatus status) {
		this.status = status;
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

	public RequestApproval getRequestApproval() {
		return requestApproval;
	}

	public void setRequestApproval(RequestApproval requestApproval) {
		this.requestApproval = requestApproval;
	}
}