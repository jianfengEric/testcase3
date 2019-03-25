package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2018/8/27.
 */
public class ServiceFromCurrencyDto implements Serializable{
    private String currency;
    private Boolean enable;
    private List<ServiceToCurrencyDto> toCurrencyDto;

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

    public List<ServiceToCurrencyDto> getToCurrencyDto() {
        return toCurrencyDto;
    }

    public void setToCurrencyDto(List<ServiceToCurrencyDto> toCurrencyDto) {
        this.toCurrencyDto = toCurrencyDto;
    }
}
