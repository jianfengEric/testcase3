package com.gea.portal.apv.entity;

import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "approval_result")
public class ApprovalResult {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Basic
    @Column(name = "gea_participant_ref_id", nullable = true, length = 45)
    private String geaParticipantRefId;
    @Basic
    @Column(name = "gea_moneypool_ref_id", nullable = true, length = 45)
    private String geaMoneypoolRefId;
    @Basic
    @Column(name = "ewp_apv_req_id", nullable = true)
    private Long ewpApvReqId;
    @Basic
    @Column(name = "mp_apv_req_id", nullable = true)
    private Long mpApvReqId;
    @Basic
    @Column(name = "srv_apv_req_id", nullable = true)
    private Long srvApvReqId;
    @Column(name = "exch_rate_apv_req_id")
    private Long exchRateApvReqId;
    @Basic
    @Column(name = "charge_apv_req_id", nullable = true)
    private Long chargeApvReqId;
    @Basic
    @Column(name = "approval_remark", nullable = true, length = 250)
    private String approvalRemark;
    @Basic
    @Column(name = "approval_status", nullable = true, length = 45)
    @Enumerated(EnumType.STRING)
    private RequestApprovalStatus approvalStatus;
    @Basic
    @Column(name = "current_envir", nullable = true, length = 45)
    @Enumerated(EnumType.STRING)
    private Instance currentEnvir;
    @Basic
    @Column(name = "request_user_id", nullable = true)
    private String requestUserId;
    @Basic
    @Column(name = "request_user_name", nullable = true, length = 45)
    private String requestUserName;
    @Basic
    @Column(name = "request_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Basic
    @Column(name = "approval_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;
    @Basic
//    @Column(name = "approval_by", nullable = true, length = 45)
    @OneToOne
    @JoinColumn(name = "approval_by")
    private AnaAccount approvalBy;
    @ManyToOne
    @JoinColumn(name = "approval_category_id", referencedColumnName = "id")
    private ApprovalCategoryItem approvalCategoryItem;
    @Column(name = "deploy_queue_ref_id")
    private Long deployQueueRefId;


    public Long getDeployQueueRefId() {
		return deployQueueRefId;
	}

	public void setDeployQueueRefId(Long deployQueueRefId) {
		this.deployQueueRefId = deployQueueRefId;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }


    public String getGeaMoneypoolRefId() {
        return geaMoneypoolRefId;
    }

    public void setGeaMoneypoolRefId(String geaMoneypoolRefId) {
        this.geaMoneypoolRefId = geaMoneypoolRefId;
    }

    public Long getEwpApvReqId() {
        return ewpApvReqId;
    }

    public void setEwpApvReqId(Long ewpApvReqId) {
        this.ewpApvReqId = ewpApvReqId;
    }


    public Long getMpApvReqId() {
        return mpApvReqId;
    }

    public void setMpApvReqId(Long mpApvReqId) {
        this.mpApvReqId = mpApvReqId;
    }


    public Long getChargeApvReqId() {
        return chargeApvReqId;
    }

    public void setChargeApvReqId(Long chargeApvReqId) {
        this.chargeApvReqId = chargeApvReqId;
    }


    public String getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
    }

    public String getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(String requestUserId) {
        this.requestUserId = requestUserId;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public AnaAccount getApprovalBy() {
        return approvalBy;
    }

    public void setApprovalBy(AnaAccount approvalBy) {
        this.approvalBy = approvalBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApprovalResult that = (ApprovalResult) o;
        return id == that.id &&
                Objects.equals(geaParticipantRefId, that.geaParticipantRefId) &&
                Objects.equals(geaMoneypoolRefId, that.geaMoneypoolRefId) &&
                Objects.equals(ewpApvReqId, that.ewpApvReqId) &&
                Objects.equals(mpApvReqId, that.mpApvReqId) &&
                Objects.equals(chargeApvReqId, that.chargeApvReqId) &&
                Objects.equals(approvalRemark, that.approvalRemark) &&
                Objects.equals(approvalStatus, that.approvalStatus) &&
                Objects.equals(currentEnvir, that.currentEnvir) &&
                Objects.equals(requestUserId, that.requestUserId) &&
                Objects.equals(requestUserName, that.requestUserName) &&
                Objects.equals(requestDate, that.requestDate) &&
                Objects.equals(approvalDate, that.approvalDate) &&
                Objects.equals(approvalBy, that.approvalBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, geaParticipantRefId, geaMoneypoolRefId, ewpApvReqId, mpApvReqId, chargeApvReqId, approvalRemark, approvalStatus, currentEnvir, requestUserId, requestUserName, requestDate, approvalDate, approvalBy);
    }

    public ApprovalCategoryItem getApprovalCategoryItem() {
        return approvalCategoryItem;
    }

    public void setApprovalCategoryItem(ApprovalCategoryItem approvalCategoryItem) {
        this.approvalCategoryItem = approvalCategoryItem;
    }

    public RequestApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(RequestApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Instance getCurrentEnvir() {
        return currentEnvir;
    }

    public void setCurrentEnvir(Instance currentEnvir) {
        this.currentEnvir = currentEnvir;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Long getExchRateApvReqId() {
        return exchRateApvReqId;
    }

    public void setExchRateApvReqId(Long exchRateApvReqId) {
        this.exchRateApvReqId = exchRateApvReqId;
    }

	public Long getSrvApvReqId() {
		return srvApvReqId;
	}

	public void setSrvApvReqId(Long srvApvReqId) {
		this.srvApvReqId = srvApvReqId;
	}
}
