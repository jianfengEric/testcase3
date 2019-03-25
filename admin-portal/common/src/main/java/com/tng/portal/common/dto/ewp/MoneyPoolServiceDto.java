package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dell on 2018/11/16.
 */
public class MoneyPoolServiceDto implements Serializable {
    private List<Long> serviceIds;
    private String geaMoneyPoolRefId;
    private String moneyPoolServices;


    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }

    public String getGeaMoneyPoolRefId() {
        return geaMoneyPoolRefId;
    }

    public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
        this.geaMoneyPoolRefId = geaMoneyPoolRefId;
    }

    public String getMoneyPoolServices() {
        return moneyPoolServices;
    }

    public void setMoneyPoolServices(String moneyPoolServices) {
        this.moneyPoolServices = moneyPoolServices;
    }
}
