package com.gea.portal.dpy.entity;

import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Eric on 2018/9/12.
 */

@Entity
@Table(name = "deploy_queue")
public class DeployQueue {
    private long id;
    private String deployVersionNo;
    private String geaParticipantRefId;
    private String deployFullData;
    private Date scheduleDeployDate;
    private Instance deployEnvir;
    private DeployStatus status;
    private String statusReason;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    private DeployType deployType;
    private Long deployRefId;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "deploy_version_no", nullable = true, length = 45)
    public String getDeployVersionNo() {
        return deployVersionNo;
    }

    public void setDeployVersionNo(String deployVersionNo) {
        this.deployVersionNo = deployVersionNo;
    }

    @Basic
    @Column(name = "gea_participant_ref_id", nullable = true, length = 45)
    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    @Basic
    @Column(name = "deploy_full_data", nullable = true)
    public String getDeployFullData() {
        return deployFullData;
    }

    public void setDeployFullData(String deployFullData) {
        this.deployFullData = deployFullData;
    }

    @Basic
    @Column(name = "schedule_deploy_date", nullable = true)
    public Date getScheduleDeployDate() {
        return scheduleDeployDate;
    }

    public void setScheduleDeployDate(Date scheduleDeployDate) {
        this.scheduleDeployDate = scheduleDeployDate;
    }

    @Basic
    @Column(name = "deploy_envir", nullable = true, length = 45)
    @Enumerated(EnumType.STRING)
    public Instance getDeployEnvir() {
        return deployEnvir;
    }

    public void setDeployEnvir(Instance deployEnvir) {
        this.deployEnvir = deployEnvir;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 45)
    @Enumerated(EnumType.STRING)
    public DeployStatus getStatus() {
        return status;
    }

    public void setStatus(DeployStatus status) {
        this.status = status;
    }

    @Basic
    @Column(name = "status_reason", nullable = true, length = 250)
    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "create_by", nullable = true, length = 45)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "update_date", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Basic
    @Column(name = "update_by", nullable = true, length = 45)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "deploy_type")
	@Enumerated(EnumType.STRING)
	public DeployType getDeployType() {
		return deployType;
	}

	public void setDeployType(DeployType deployType) {
		this.deployType = deployType;
	}

    @Basic
    @Column(name = "deploy_ref_id")
    public Long getDeployRefId() {
        return deployRefId;
    }

    public void setDeployRefId(Long deployRefId) {
        this.deployRefId = deployRefId;
    }

}
