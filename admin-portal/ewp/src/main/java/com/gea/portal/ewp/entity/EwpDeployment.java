package com.gea.portal.ewp.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;


/**
 * The persistent class for the ewp_deploy_to_prod database table.
 * 
 */
@Entity
@Table(name="ewp_deployment")
public class EwpDeployment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="deploy_note")
	private String deployNote;

	@Column(name="deploy_version_no")
	private String deployVersionNo;

	@Column(name="participant_id")
	private Long participantId;

	@Column(name="deploy_envir")
	@Enumerated(EnumType.STRING)
	private Instance deployEnvir;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="schedule_deploy_date")
	private Date scheduleDeployDate;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private DeployStatus status;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to EwpRecordEnvMap
	@OneToMany(mappedBy="ewpDeployToProd")
	private List<EwpRecordEnvMap> ewpRecordEnvMaps;

	@Column(name = "request_approval_id")
	private Long requestApprovalId;
	
	public EwpDeployment() {
	}

	public Instance getDeployEnvir() {
		return deployEnvir;
	}

	public void setDeployEnvir(Instance deployEnvir) {
		this.deployEnvir = deployEnvir;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDeployNote() {
		return this.deployNote;
	}

	public void setDeployNote(String deployNote) {
		this.deployNote = deployNote;
	}

	public String getDeployVersionNo() {
		return this.deployVersionNo;
	}

	public void setDeployVersionNo(String deployVersionNo) {
		this.deployVersionNo = deployVersionNo;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public Date getScheduleDeployDate() {
		return this.scheduleDeployDate;
	}

	public void setScheduleDeployDate(Date scheduleDeployDate) {
		this.scheduleDeployDate = scheduleDeployDate;
	}

	public DeployStatus getStatus() {
		return status;
	}

	public void setStatus(DeployStatus status) {
		this.status = status;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public List<EwpRecordEnvMap> getEwpRecordEnvMaps() {
		return this.ewpRecordEnvMaps;
	}

	public void setEwpRecordEnvMaps(List<EwpRecordEnvMap> ewpRecordEnvMaps) {
		this.ewpRecordEnvMaps = ewpRecordEnvMaps;
	}

	public EwpRecordEnvMap removeEwpRecordEnvMap(EwpRecordEnvMap ewpRecordEnvMap) {
		getEwpRecordEnvMaps().remove(ewpRecordEnvMap);
		ewpRecordEnvMap.setEwpDeployToProd(null);

		return ewpRecordEnvMap;
	}

	public Long getRequestApprovalId() {
		return requestApprovalId;
	}

	public void setRequestApprovalId(Long requestApprovalId) {
		this.requestApprovalId = requestApprovalId;
	}
}