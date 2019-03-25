package com.gea.portal.order.entity.preProduction.order;


import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "QUOTATION")
public class PreQuotation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private Long id;


    @Column(name = "QUO_REF_ID")
    private String quoRefId;


    @Column(name = "ISSUER_ID")
    private Long issuerId;


    @Column(name = "REF_ID")
    private String refId;


    @Column(name = "AMOUNT")
    private BigDecimal amount;


    @Column(name = "AMOUNT_TYPE")
    private String amountType;


    @Column(name = "EXCHANGE_RATE")
    private BigDecimal exchangeRate;


    @Column(name = "EXCHANGE_RATE_ID")
    private Long exchangeRateId;


    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;


    @Column(name = "FROM_AMOUNT")
    private BigDecimal fromAmount;


    @Column(name = "TO_AMOUNT")
    private BigDecimal toAmount;


    @Column(name = "FROM_CURRENCY")
    private String fromCurrency;


    @Column(name = "TO_CURRENCY")
    private String toCurrency;


    @Column(name = "CREATE_DATETIME")
    private Date createDatetime;


    @Column(name = "UPDATE_DATETIME")
    private Date updateDatetime;


    @Column(name = "PARTICIPANT_ID")
    private Long participantId;


    @Column(name = "COUNTRY_ID")
    private Long countryId;


    @Column(name = "STATUS")
    private String status;

    @NotFound(action= NotFoundAction.IGNORE)
    @ManyToOne
    @JoinColumn(name = "ISSUER_ID", insertable=false, updatable=false)
    private PreTngIssuer tngIssuer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getQuoRefId() {
        return quoRefId;
    }

    public void setQuoRefId(String quoRefId) {
        this.quoRefId = quoRefId;
    }


    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }


    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }


    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }


    public Long getExchangeRateId() {
        return exchangeRateId;
    }

    public void setExchangeRateId(Long exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }


    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }


    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }


    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }


    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }


    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }


    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }


    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }


    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }


    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PreTngIssuer getTngIssuer() {
        return tngIssuer;
    }

    public void setTngIssuer(PreTngIssuer tngIssuer) {
        this.tngIssuer = tngIssuer;
    }
}
