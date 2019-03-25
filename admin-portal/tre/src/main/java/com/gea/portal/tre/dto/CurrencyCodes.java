package com.gea.portal.tre.dto;

import java.io.Serializable;
import java.util.List;

public class CurrencyCodes implements Serializable {
    private String status;
    private List<String> currencyCodes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCurrencyCodes() {
        return currencyCodes;
    }

    public void setCurrencyCodes(List<String> currencyCodes) {
        this.currencyCodes = currencyCodes;
    }
}
