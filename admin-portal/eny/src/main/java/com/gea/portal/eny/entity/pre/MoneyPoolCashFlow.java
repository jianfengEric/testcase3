package com.gea.portal.eny.entity.pre;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "MONEY_POOL_CASH_FLOW")
public class MoneyPoolCashFlow {
    private int id;
    private int moneyPoolId;
    private String action;
    private BigDecimal previousTotalReserved;
    private BigDecimal previousTotalAvailable;
    private BigDecimal previousTotalBalance;
    private BigDecimal changeReservation;
    private BigDecimal changeBalance;
    private BigDecimal lastTotalReserved;
    private BigDecimal lastTotalAvailable;
    private BigDecimal lastTotalBalance;
    private String refNo;
    private String topupChannel;
    private String description;
    private String remark;
    private String moduleCode;
    private String serviceId;
    private String mpCfRefNo;
    private Timestamp createDatetime;
    private String type;
    private Integer ewalletParticipantId;
    private Integer parentId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "MONEY_POOL_ID", nullable = false)
    public int getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(int moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
    }

    @Basic
    @Column(name = "ACTION", nullable = false, length = 100)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Basic
    @Column(name = "PREVIOUS_TOTAL_RESERVED", nullable = false, precision = 4)
    public BigDecimal getPreviousTotalReserved() {
        return previousTotalReserved;
    }

    public void setPreviousTotalReserved(BigDecimal previousTotalReserved) {
        this.previousTotalReserved = previousTotalReserved;
    }

    @Basic
    @Column(name = "PREVIOUS_TOTAL_AVAILABLE", nullable = false, precision = 4)
    public BigDecimal getPreviousTotalAvailable() {
        return previousTotalAvailable;
    }

    public void setPreviousTotalAvailable(BigDecimal previousTotalAvailable) {
        this.previousTotalAvailable = previousTotalAvailable;
    }

    @Basic
    @Column(name = "PREVIOUS_TOTAL_BALANCE", nullable = false, precision = 4)
    public BigDecimal getPreviousTotalBalance() {
        return previousTotalBalance;
    }

    public void setPreviousTotalBalance(BigDecimal previousTotalBalance) {
        this.previousTotalBalance = previousTotalBalance;
    }

    @Basic
    @Column(name = "CHANGE_RESERVATION", nullable = false, precision = 4)
    public BigDecimal getChangeReservation() {
        return changeReservation;
    }

    public void setChangeReservation(BigDecimal changeReservation) {
        this.changeReservation = changeReservation;
    }

    @Basic
    @Column(name = "CHANGE_BALANCE", nullable = false, precision = 4)
    public BigDecimal getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(BigDecimal changeBalance) {
        this.changeBalance = changeBalance;
    }

    @Basic
    @Column(name = "LAST_TOTAL_RESERVED", nullable = false, precision = 4)
    public BigDecimal getLastTotalReserved() {
        return lastTotalReserved;
    }

    public void setLastTotalReserved(BigDecimal lastTotalReserved) {
        this.lastTotalReserved = lastTotalReserved;
    }

    @Basic
    @Column(name = "LAST_TOTAL_AVAILABLE", nullable = false, precision = 4)
    public BigDecimal getLastTotalAvailable() {
        return lastTotalAvailable;
    }

    public void setLastTotalAvailable(BigDecimal lastTotalAvailable) {
        this.lastTotalAvailable = lastTotalAvailable;
    }

    @Basic
    @Column(name = "LAST_TOTAL_BALANCE", nullable = false, precision = 4)
    public BigDecimal getLastTotalBalance() {
        return lastTotalBalance;
    }

    public void setLastTotalBalance(BigDecimal lastTotalBalance) {
        this.lastTotalBalance = lastTotalBalance;
    }

    @Basic
    @Column(name = "REF_NO", nullable = true, length = 100)
    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    @Basic
    @Column(name = "TOPUP_CHANNEL", nullable = true, length = 100)
    public String getTopupChannel() {
        return topupChannel;
    }

    public void setTopupChannel(String topupChannel) {
        this.topupChannel = topupChannel;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "REMARK", nullable = true, length = 255)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "MODULE_CODE", nullable = true, length = 100)
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    @Basic
    @Column(name = "SERVICE_ID", nullable = true, length = 100)
    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "MP_CF_REF_NO", nullable = true, length = 100)
    public String getMpCfRefNo() {
        return mpCfRefNo;
    }

    public void setMpCfRefNo(String mpCfRefNo) {
        this.mpCfRefNo = mpCfRefNo;
    }

    @Basic
    @Column(name = "CREATE_DATETIME", nullable = true)
    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Basic
    @Column(name = "TYPE", nullable = true, length = 45)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "PARENT_ID", nullable = true)
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Basic
    @Column(name = "EWALLET_PARTICIPANT_ID", nullable = true)
    public Integer getEwalletParticipantId() {
        return ewalletParticipantId;
    }

    public void setEwalletParticipantId(Integer ewalletParticipantId) {
        this.ewalletParticipantId = ewalletParticipantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyPoolCashFlow that = (MoneyPoolCashFlow) o;
        return id == that.id &&
                moneyPoolId == that.moneyPoolId &&
                Objects.equals(action, that.action) &&
                Objects.equals(previousTotalReserved, that.previousTotalReserved) &&
                Objects.equals(previousTotalAvailable, that.previousTotalAvailable) &&
                Objects.equals(previousTotalBalance, that.previousTotalBalance) &&
                Objects.equals(changeReservation, that.changeReservation) &&
                Objects.equals(changeBalance, that.changeBalance) &&
                Objects.equals(lastTotalReserved, that.lastTotalReserved) &&
                Objects.equals(lastTotalAvailable, that.lastTotalAvailable) &&
                Objects.equals(lastTotalBalance, that.lastTotalBalance) &&
                Objects.equals(refNo, that.refNo) &&
                Objects.equals(topupChannel, that.topupChannel) &&
                Objects.equals(description, that.description) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(moduleCode, that.moduleCode) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(mpCfRefNo, that.mpCfRefNo) &&
                Objects.equals(createDatetime, that.createDatetime) &&
                Objects.equals(type, that.type) &&
                Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moneyPoolId, action, previousTotalReserved, previousTotalAvailable, previousTotalBalance, changeReservation, changeBalance, lastTotalReserved, lastTotalAvailable, lastTotalBalance, refNo, topupChannel, description, remark, moduleCode, serviceId, mpCfRefNo, createDatetime, type, parentId);
    }
}
