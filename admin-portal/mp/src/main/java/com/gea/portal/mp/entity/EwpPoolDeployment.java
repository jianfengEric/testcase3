package com.gea.portal.mp.entity;

import com.tng.portal.common.enumeration.DeployStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "ewp_pool_deployment")
public class EwpPoolDeployment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gea_participant_ref_id")
    private String geaParticipantRefId;

    @OneToOne
    @JoinColumn(name = "request_approval_id")
    private RequestApproval requestApproval;

    @Column(name = "schedule_deploy_date")
    private Date scheduleDeployDate;

    @Column(name = "deploy_note")
    private String deployNote;

    @Column(name = "deploy_version_no")
    private String deployVersionNo;

    @Column(name = "deploy_envir")
    private String deployEnvir;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DeployStatus status;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public String getDeployEnvir() {
        return deployEnvir;
    }

    public void setDeployEnvir(String deployEnvir) {
        this.deployEnvir = deployEnvir;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public RequestApproval getRequestApproval() {
        return requestApproval;
    }

    public void setRequestApproval(RequestApproval requestApproval) {
        this.requestApproval = requestApproval;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
}
