package com.gea.portal.tre.entity;

import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Dell on 2018/9/14.
 */
@Table(name = "exchange_rate_deployment")
@Entity
public class ExchangeRateDeployment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "request_approval_id")
    private Long requestApprovalId;
    @Column(name = "deploy_envir")
    @Enumerated(EnumType.STRING)
    private Instance deployEnvir;
    @Column(name = "schedule_deploy_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDeployDate;
    @Column(name = "deploy_note")
    private String deployNote;
    @Column(name = "deploy_version_no")
    private String deployVersionNo;
    @Column(name = "status")
    private String status;
    @Column(name = "pre_rate_file_id")
    private String preRateFileId;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestApprovalId() {
        return requestApprovalId;
    }

    public void setRequestApprovalId(Long requestApprovalId) {
        this.requestApprovalId = requestApprovalId;
    }

    public Instance getDeployEnvir() {
        return deployEnvir;
    }

    public void setDeployEnvir(Instance deployEnvir) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreRateFileId() {
        return preRateFileId;
    }

    public void setPreRateFileId(String preRateFileId) {
        this.preRateFileId = preRateFileId;
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
}
