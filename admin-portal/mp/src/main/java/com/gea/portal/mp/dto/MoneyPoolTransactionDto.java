package com.gea.portal.mp.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by dong on 2018/9/10.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoneyPoolTransactionDto implements Serializable {

    private String geaUserId;
    private String moneyPoolRefId;
    private String participantId;
    private String geaMoneyTransferId;
    private String balanceUpdateTime;
    private String beforeTotalBalance;
    private String afterTotalBalance;
    private String transactionType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transactionDateTime;
    private String currency;
    private String amount;
    private String serviceId;
    private String remark;
    private String refNo;

    public String getGeaUserId() {
        return geaUserId;
    }

    public void setGeaUserId(String geaUserId) {
        this.geaUserId = geaUserId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getGeaMoneyTransferId() {
        return geaMoneyTransferId;
    }

    public void setGeaMoneyTransferId(String geaMoneyTransferId) {
        this.geaMoneyTransferId = geaMoneyTransferId;
    }

    public String getBalanceUpdateTime() {
        return balanceUpdateTime;
    }

    public void setBalanceUpdateTime(String balanceUpdateTime) {
        this.balanceUpdateTime = balanceUpdateTime;
    }

    public String getBeforeTotalBalance() {
        return beforeTotalBalance;
    }

    public void setBeforeTotalBalance(String beforeTotalBalance) {
        this.beforeTotalBalance = beforeTotalBalance;
    }

    public String getAfterTotalBalance() {
        return afterTotalBalance;
    }

    public void setAfterTotalBalance(String afterTotalBalance) {
        this.afterTotalBalance = afterTotalBalance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getMoneyPoolRefId() {
		return moneyPoolRefId;
	}

	public void setMoneyPoolRefId(String moneyPoolRefId) {
		this.moneyPoolRefId = moneyPoolRefId;
	}

}
