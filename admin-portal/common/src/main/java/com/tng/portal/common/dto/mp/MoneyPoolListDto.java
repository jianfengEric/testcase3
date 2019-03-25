package com.tng.portal.common.dto.mp;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dong on 2018/9/5.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoneyPoolListDto implements Serializable{

    private String moneyPoolId;
    private String geaMoneyPoolRefId;
    private String participantId;
    private String geaParticipantRefId;
    private String participantName;
    private String currency;
    private BigDecimal balance;
    private BigDecimal alertLine;
    private String moneyPoolStatus;
    private String relatedServices;
    private String group;
    private String approvalStatus;
    private String lastUpdateTime;


    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMoneyPoolStatus() {
        return moneyPoolStatus;
    }

    public void setMoneyPoolStatus(String moneyPoolStatus) {
        this.moneyPoolStatus = moneyPoolStatus;
    }

    public String getRelatedServices() {
        return relatedServices;
    }

    public void setRelatedServices(String relatedServices) {
        this.relatedServices = relatedServices;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGeaMoneyPoolRefId() {
        return geaMoneyPoolRefId;
    }

    public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
        this.geaMoneyPoolRefId = geaMoneyPoolRefId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAlertLine() {
        return alertLine;
    }

    public void setAlertLine(BigDecimal alertLine) {
        this.alertLine = alertLine;
    }

}
