package com.gea.portal.tre.dto;

import java.io.Serializable;
import java.util.List;

public class RateErrorData implements Serializable {

    private String exchRateFileId;
    private List<RateDetails> details;
    private List<String> currencyCodes;

    public List<RateDetails> getDetails() {
        return details;
    }

    public void setDetails(List<RateDetails> details) {
        this.details = details;
    }

    public String getExchRateFileId() {
        return exchRateFileId;
    }

    public void setExchRateFileId(String exchRateFileId) {
        this.exchRateFileId = exchRateFileId;
    }

    public List<String> getCurrencyCodes() {
        return currencyCodes;
    }

    public void setCurrencyCodes(List<String> currencyCodes) {
        this.currencyCodes = currencyCodes;
    }
}
