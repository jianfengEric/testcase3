package com.gea.portal.eny.entity.pre;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "MONEY_POOL")
public class MoneyPool {
    private int id;
    private String refId;
    private int ewalletParticipantId;
    private String description;
    private String currencyType;
    private String status;
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
    @Column(name = "REF_ID", nullable = true, length = 75)
    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Basic
    @Column(name = "EWALLET_PARTICIPANT_ID", nullable = false)
    public int getEwalletParticipantId() {
        return ewalletParticipantId;
    }

    public void setEwalletParticipantId(int ewalletParticipantId) {
        this.ewalletParticipantId = ewalletParticipantId;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 400)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "CURRENCY_TYPE", nullable = true, length = 5)
    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Basic
    @Column(name = "STATUS", nullable = false, length = 20)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        MoneyPool moneyPool = (MoneyPool) o;
        return id == moneyPool.id &&
                ewalletParticipantId == moneyPool.ewalletParticipantId &&
                Objects.equals(refId, moneyPool.refId) &&
                Objects.equals(description, moneyPool.description) &&
                Objects.equals(currencyType, moneyPool.currencyType) &&
                Objects.equals(status, moneyPool.status) &&
                Objects.equals(createDatetime, moneyPool.createDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, refId, ewalletParticipantId, description, currencyType, status, createDatetime);
    }
}
