package com.gea.portal.mp.entity;

import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "ewp_mp_change_req")
public class EwpMpChangeReq {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "money_pool_id")
    private EwpMoneyPool ewpMoneyPool;

    @Column(name = "to_alert_line")
    private BigDecimal toAlertLine;

    @Column(name = "to_status")
    private String toStatus;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EwpMoneyPool getEwpMoneyPool() {
        return ewpMoneyPool;
    }

    public void setEwpMoneyPool(EwpMoneyPool ewpMoneyPool) {
        this.ewpMoneyPool = ewpMoneyPool;
    }

    public BigDecimal getToAlertLine() {
        return toAlertLine;
    }

    public void setToAlertLine(BigDecimal toAlertLine) {
        this.toAlertLine = toAlertLine;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
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
}
