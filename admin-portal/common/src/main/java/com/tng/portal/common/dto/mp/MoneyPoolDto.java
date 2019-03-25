package com.tng.portal.common.dto.mp;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jimmy on 2018/9/3.
 */
public class MoneyPoolDto implements Serializable {

    private String geaParticipantRefId;
    private String geaMoneyPoolRefId;
    private String baseCurrency;
    private BigDecimal balance;
    private BigDecimal alertLine;
    private String status;
    private String currentEnvir;
    private String requestRemark;

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentEnvir() {
        return currentEnvir;
    }

    public void setCurrentEnvir(String currentEnvir) {
        this.currentEnvir = currentEnvir;
    }

    public String getGeaMoneyPoolRefId() {
        return geaMoneyPoolRefId;
    }

    public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
        this.geaMoneyPoolRefId = geaMoneyPoolRefId;
    }

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}

}
