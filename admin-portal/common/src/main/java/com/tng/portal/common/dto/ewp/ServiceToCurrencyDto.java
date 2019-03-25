package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tng.portal.common.serialize.BigDecimalSerializer;

/**
 * Created by Owen on 2018/8/27.
 */
public class ServiceToCurrencyDto implements Serializable{
    private String currency;
    private Boolean enable;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal adminFee;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal changeNameAdminFee;
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal cancelAdminFee;

    public BigDecimal getChangeNameAdminFee() {
        return changeNameAdminFee;
    }

    public void setChangeNameAdminFee(BigDecimal changeNameAdminFee) {
        this.changeNameAdminFee = changeNameAdminFee;
    }

    public BigDecimal getCancelAdminFee() {
        return cancelAdminFee;
    }

    public void setCancelAdminFee(BigDecimal cancelAdminFee) {
        this.cancelAdminFee = cancelAdminFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public BigDecimal getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(BigDecimal adminFee) {
        this.adminFee = adminFee;
    }
}
