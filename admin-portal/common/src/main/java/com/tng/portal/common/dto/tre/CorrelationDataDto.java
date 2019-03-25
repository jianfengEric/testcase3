package com.tng.portal.common.dto.tre;

import java.io.Serializable;

public class CorrelationDataDto  implements Serializable{
    private String currFrom;
    private String currTo;
    private String currencyExchangeRate;
    private String newExchangeRate;
    private String percentageChange;

    public String getCurrFrom() {
        return currFrom;
    }

    public void setCurrFrom(String currFrom) {
        this.currFrom = currFrom;
    }

    public String getCurrTo() {
        return currTo;
    }

    public void setCurrTo(String currTo) {
        this.currTo = currTo;
    }

    public String getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    public void setCurrencyExchangeRate(String currencyExchangeRate) {
        this.currencyExchangeRate = currencyExchangeRate;
    }

    public String getNewExchangeRate() {
        return newExchangeRate;
    }

    public void setNewExchangeRate(String newExchangeRate) {
        this.newExchangeRate = newExchangeRate;
    }

    public String getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(String percentageChange) {
        this.percentageChange = percentageChange;
    }
}
