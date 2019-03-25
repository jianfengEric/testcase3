package com.gea.portal.mp.entity;

import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "ewp_money_pool")
public class EwpMoneyPool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gea_participant_ref_id")
    private String geaParticipantRefId;

    @Column(name = "gea_moneypool_ref_id")
    private String geaMoneyPoolRefId;

    @Column(name = "base_currency")
    private String baseCurrency;

    @Column(name = "alert_line")
    private BigDecimal alertLine;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MoneyPoolStatus status;

    @Column(name = "current_envir")
    @Enumerated(EnumType.STRING)
    private Instance currentEnvir;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    @OneToMany(mappedBy="ewpMoneyPool")
    private List<EwpPoolAdjustment> ewpPoolAdjustments;

    @OneToMany(mappedBy="ewpMoneyPool")
    private List<EwpPoolDepositCashOut> ewpPoolDepositCashOuts;

    @OneToMany(mappedBy="ewpMoneyPool")
    private List<EwpMpChangeReq> ewpMpChangeReqs;

    @OneToMany(mappedBy="ewpMoneyPool")
    private List<RequestApproval> requestApprovals;

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

    public String getGeaMoneyPoolRefId() {
        return geaMoneyPoolRefId;
    }

    public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
        this.geaMoneyPoolRefId = geaMoneyPoolRefId;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getAlertLine() {
        return alertLine;
    }

    public void setAlertLine(BigDecimal alertLine) {
        this.alertLine = alertLine;
    }

    public MoneyPoolStatus getStatus() {
        return status;
    }

    public void setStatus(MoneyPoolStatus status) {
        this.status = status;
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

    public List<EwpPoolAdjustment> getEwpPoolAdjustments() {
        return ewpPoolAdjustments;
    }

    public void setEwpPoolAdjustments(List<EwpPoolAdjustment> ewpPoolAdjustments) {
        this.ewpPoolAdjustments = ewpPoolAdjustments;
    }

    public List<EwpPoolDepositCashOut> getEwpPoolDepositCashOuts() {
        return ewpPoolDepositCashOuts;
    }

    public void setEwpPoolDepositCashOuts(List<EwpPoolDepositCashOut> ewpPoolDepositCashOuts) {
        this.ewpPoolDepositCashOuts = ewpPoolDepositCashOuts;
    }

    public List<EwpMpChangeReq> getEwpMpChangeReqs() {
        return ewpMpChangeReqs;
    }

    public void setEwpMpChangeReqs(List<EwpMpChangeReq> ewpMpChangeReqs) {
        this.ewpMpChangeReqs = ewpMpChangeReqs;
    }

    public List<RequestApproval> getRequestApprovals() {
        return requestApprovals;
    }

    public void setRequestApprovals(List<RequestApproval> requestApprovals) {
        this.requestApprovals = requestApprovals;
    }
}
