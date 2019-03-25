package com.gea.portal.mp.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by dong on 2018/9/10.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoneyPoolDetailDto implements Serializable {

    private BigDecimal alertLine;
    private String moneyPoolId;
    private String geaMoneyPoolRefId;
    private String baseCurrency;
    private String geaParticipantRefId;
    private String participantId;
    private String participantName;
    private String serviceNo;
    private String status;
    private BigDecimal balance;

    public BigDecimal getAlertLine() {
        return alertLine;
    }

    public void setAlertLine(BigDecimal alertLine) {
        this.alertLine = alertLine;
    }

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
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

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
