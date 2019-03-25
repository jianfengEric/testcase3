package com.gea.portal.mp.dto;

import java.io.Serializable;

/**
 * Created by dong on 2018/9/5.
 */
public class MoneyPoolDetailPageDto implements Serializable {

    private String moneyPoolId;
    private String moneyPoolRefId;
    private String portalUserId;
    private String geaMoneyTransferId;
    //private String transactionDateTime;
    private String beginTransactionDateTime;
	private String endTransactionDateTime;
    
    private String currency;
    private String serviceType;
    private String transactionType;
    //private String amount;
    private String minimumAmount;
    private String largestAmount;
    //private String balanceAfterTransaction;
    //private String balanceBeforeTransaction;
    private String minimumBalanceAfterTransaction;
    private String largestBalanceAfterTransaction;
    private String minimumBalanceBeforeTransaction;
    private String largestBalanceBeforeTransaction;
    private String remark;

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
    }

    public String getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(String portalUserId) {
        this.portalUserId = portalUserId;
    }

    public String getGeaMoneyTransferId() {
        return geaMoneyTransferId;
    }

    public void setGeaMoneyTransferId(String geaMoneyTransferId) {
        this.geaMoneyTransferId = geaMoneyTransferId;
    }

    /*public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }*/

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBeginTransactionDateTime() {
		return beginTransactionDateTime;
	}

	public void setBeginTransactionDateTime(String beginTransactionDateTime) {
		this.beginTransactionDateTime = beginTransactionDateTime;
	}

	public String getEndTransactionDateTime() {
		return endTransactionDateTime;
	}

	public void setEndTransactionDateTime(String endTransactionDateTime) {
		this.endTransactionDateTime = endTransactionDateTime;
	}

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getLargestAmount() {
        return largestAmount;
    }

    public void setLargestAmount(String largestAmount) {
        this.largestAmount = largestAmount;
    }

    public String getMinimumBalanceAfterTransaction() {
        return minimumBalanceAfterTransaction;
    }

    public void setMinimumBalanceAfterTransaction(String minimumBalanceAfterTransaction) {
        this.minimumBalanceAfterTransaction = minimumBalanceAfterTransaction;
    }

    public String getLargestBalanceAfterTransaction() {
        return largestBalanceAfterTransaction;
    }

    public void setLargestBalanceAfterTransaction(String largestBalanceAfterTransaction) {
        this.largestBalanceAfterTransaction = largestBalanceAfterTransaction;
    }

    public String getMinimumBalanceBeforeTransaction() {
        return minimumBalanceBeforeTransaction;
    }

    public void setMinimumBalanceBeforeTransaction(String minimumBalanceBeforeTransaction) {
        this.minimumBalanceBeforeTransaction = minimumBalanceBeforeTransaction;
    }

    public String getLargestBalanceBeforeTransaction() {
        return largestBalanceBeforeTransaction;
    }

    public void setLargestBalanceBeforeTransaction(String largestBalanceBeforeTransaction) {
        this.largestBalanceBeforeTransaction = largestBalanceBeforeTransaction;
    }

	public String getMoneyPoolRefId() {
		return moneyPoolRefId;
	}

	public void setMoneyPoolRefId(String moneyPoolRefId) {
		this.moneyPoolRefId = moneyPoolRefId;
	}
}
