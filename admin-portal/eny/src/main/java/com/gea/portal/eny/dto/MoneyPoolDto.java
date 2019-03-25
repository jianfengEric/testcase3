package com.gea.portal.eny.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by dong on 2018/9/10.
 */
public class MoneyPoolDto implements Serializable {

	private String moneyPoolRefId;
    private String moneyPoolRefIds;
    private String moneyPoolRefIdLike;
    private String participantId;
    private String geaMoneyTransferId;
    private String balanceUpdateTime;
    private BigDecimal beforeTotalBalance;
    private BigDecimal afterTotalBalance;
    private String transactionType;
    private String transactionDateTime;
    
    private String beginTransactionDateTime;
    private String endTransactionDateTime;
    private BigDecimal minimumAmount;
    private BigDecimal largestAmount;
    private BigDecimal minimumBalanceAfterTransaction;
    private BigDecimal largestBalanceAfterTransaction;
    private BigDecimal minimumBalanceBeforeTransaction;
    private BigDecimal largestBalanceBeforeTransaction;
    private String currency;
    private BigDecimal amount;
    private String serviceId;
    private String remark;
    private String instance;
    
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private Boolean isAscending;

    private String refNo;

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getGeaMoneyTransferId() {
        return geaMoneyTransferId;
    }

    public void setGeaMoneyTransferId(String geaMoneyTransferId) {
        this.geaMoneyTransferId = geaMoneyTransferId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getBalanceUpdateTime() {
        return balanceUpdateTime;
    }

    public void setBalanceUpdateTime(String balanceUpdateTime) {
        this.balanceUpdateTime = balanceUpdateTime;
    }

    public BigDecimal getBeforeTotalBalance() {
        return beforeTotalBalance;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(String transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public void setBeforeTotalBalance(BigDecimal beforeTotalBalance) {
        this.beforeTotalBalance = beforeTotalBalance;
    }

    public BigDecimal getAfterTotalBalance() {
        return afterTotalBalance;
    }

    public void setAfterTotalBalance(BigDecimal afterTotalBalance) {
        this.afterTotalBalance = afterTotalBalance;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public Boolean getIsAscending() {
		return isAscending;
	}

	public void setIsAscending(Boolean isAscending) {
		this.isAscending = isAscending;
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

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public BigDecimal getLargestAmount() {
        return largestAmount;
    }

    public void setLargestAmount(BigDecimal largestAmount) {
        this.largestAmount = largestAmount;
    }

    public Boolean getAscending() {
        return isAscending;
    }

    public void setAscending(Boolean ascending) {
        isAscending = ascending;
    }

    public BigDecimal getMinimumBalanceAfterTransaction() {
        return minimumBalanceAfterTransaction;
    }

    public void setMinimumBalanceAfterTransaction(BigDecimal minimumBalanceAfterTransaction) {
        this.minimumBalanceAfterTransaction = minimumBalanceAfterTransaction;
    }

    public BigDecimal getLargestBalanceAfterTransaction() {
        return largestBalanceAfterTransaction;
    }

    public void setLargestBalanceAfterTransaction(BigDecimal largestBalanceAfterTransaction) {
        this.largestBalanceAfterTransaction = largestBalanceAfterTransaction;
    }

    public BigDecimal getMinimumBalanceBeforeTransaction() {
        return minimumBalanceBeforeTransaction;
    }

    public void setMinimumBalanceBeforeTransaction(BigDecimal minimumBalanceBeforeTransaction) {
        this.minimumBalanceBeforeTransaction = minimumBalanceBeforeTransaction;
    }

    public BigDecimal getLargestBalanceBeforeTransaction() {
        return largestBalanceBeforeTransaction;
    }

    public void setLargestBalanceBeforeTransaction(BigDecimal largestBalanceBeforeTransaction) {
        this.largestBalanceBeforeTransaction = largestBalanceBeforeTransaction;
    }

	public String getMoneyPoolRefId() {
		return moneyPoolRefId;
	}

	public void setMoneyPoolRefId(String moneyPoolRefId) {
		this.moneyPoolRefId = moneyPoolRefId;
	}

	public String getMoneyPoolRefIds() {
		return moneyPoolRefIds;
	}

	public void setMoneyPoolRefIds(String moneyPoolRefIds) {
		this.moneyPoolRefIds = moneyPoolRefIds;
	}

	public String getMoneyPoolRefIdLike() {
		return moneyPoolRefIdLike;
	}

	public void setMoneyPoolRefIdLike(String moneyPoolRefIdLike) {
		this.moneyPoolRefIdLike = moneyPoolRefIdLike;
	}
}
