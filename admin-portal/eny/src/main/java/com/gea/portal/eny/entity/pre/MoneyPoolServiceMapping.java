package com.gea.portal.eny.entity.pre;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "MONEY_POOL_SERVICE_MAPPING")
public class MoneyPoolServiceMapping {
    private int id;
    private Integer ewalletParticipantId;
    private Integer moneyPoolId;
    private Integer serviceId;
    private String serviceCode;
    private Timestamp createDatetime;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "EWALLET_PARTICIPANT_ID", nullable = true)
    public Integer getEwalletParticipantId() {
        return ewalletParticipantId;
    }

    public void setEwalletParticipantId(Integer ewalletParticipantId) {
        this.ewalletParticipantId = ewalletParticipantId;
    }

    @Basic
    @Column(name = "MONEY_POOL_ID", nullable = true)
    public Integer getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(Integer moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
    }

    @Basic
    @Column(name = "SERVICE_ID", nullable = true)
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    @Basic
    @Column(name = "SERVICE_CODE", nullable = true, length = 75)
    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Basic
    @Column(name = "CREATE_DATETIME", nullable = false)
    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyPoolServiceMapping that = (MoneyPoolServiceMapping) o;
        return id == that.id &&
                Objects.equals(ewalletParticipantId, that.ewalletParticipantId) &&
                Objects.equals(moneyPoolId, that.moneyPoolId) &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(serviceCode, that.serviceCode) &&
                Objects.equals(createDatetime, that.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ewalletParticipantId, moneyPoolId, serviceId, serviceCode, createDatetime);
    }
}
