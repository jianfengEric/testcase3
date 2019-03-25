package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2018/8/28.
 */
public class MoneyPoolDto implements Serializable{
    private String geaMoneyPoolRefId;
    private String currency;
    private String balance;
    private String alertLevel;
    private String status;
    private String group;
    private List<ServiceSettingDto> serviceSettingDtoList;

    public String getGeaMoneyPoolRefId() {
        return geaMoneyPoolRefId;
    }

    public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
        this.geaMoneyPoolRefId = geaMoneyPoolRefId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ServiceSettingDto> getServiceSettingDtoList() {
        return serviceSettingDtoList;
    }

    public void setServiceSettingDtoList(List<ServiceSettingDto> serviceSettingDtoList) {
        this.serviceSettingDtoList = serviceSettingDtoList;
    }
}
