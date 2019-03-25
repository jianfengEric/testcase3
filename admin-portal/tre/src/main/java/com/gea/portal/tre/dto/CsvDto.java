package com.gea.portal.tre.dto;

import java.io.Serializable;

/**
 * Created by Owen on 2018/9/20.
 */
public class CsvDto  implements Serializable{
    private String currFrom;
    private String currTo;
    private String offerRate;

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

    public String getOfferRate() {
        return offerRate;
    }

    public void setOfferRate(String offerRate) {
        this.offerRate = offerRate;
    }

}
