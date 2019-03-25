package com.gea.portal.mp.entity;

import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "request_approval")
public class RequestApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gea_participant_ref_id")
    private String geaParticipantRefId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ewp_money_pool_id")
    private EwpMoneyPool ewpMoneyPool;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ewp_mp_change_req_id")
    private EwpMpChangeReq ewpMpChangeReq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ewp_pool_adjust_id")
    private EwpPoolAdjustment ewpPoolAdjustment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ewp_deposit_cashout_id")
    private EwpPoolDepositCashOut ewpPoolDepositCashOut;

    @Column(name = "approval_type")
    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestApprovalStatus status;

    @Column(name = "status_reason")
    private String statusReason;

    @Column(name = "current_envir")
    @Enumerated(EnumType.STRING)
    private Instance currentEnvir;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @Column(name = "request_remark")
    private String requestRemark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public EwpMoneyPool getEwpMoneyPool() {
        return ewpMoneyPool;
    }

    public void setEwpMoneyPool(EwpMoneyPool ewpMoneyPool) {
        this.ewpMoneyPool = ewpMoneyPool;
    }

    public EwpMpChangeReq getEwpMpChangeReq() {
        return ewpMpChangeReq;
    }

    public void setEwpMpChangeReq(EwpMpChangeReq ewpMpChangeReq) {
        this.ewpMpChangeReq = ewpMpChangeReq;
    }

    public EwpPoolAdjustment getEwpPoolAdjustment() {
        return ewpPoolAdjustment;
    }

    public void setEwpPoolAdjustment(EwpPoolAdjustment ewpPoolAdjustment) {
        this.ewpPoolAdjustment = ewpPoolAdjustment;
    }

    public EwpPoolDepositCashOut getEwpPoolDepositCashOut() {
        return ewpPoolDepositCashOut;
    }

    public void setEwpPoolDepositCashOut(EwpPoolDepositCashOut ewpPoolDepositCashOut) {
        this.ewpPoolDepositCashOut = ewpPoolDepositCashOut;
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

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}
