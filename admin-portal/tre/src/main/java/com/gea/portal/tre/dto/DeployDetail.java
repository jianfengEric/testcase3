package com.gea.portal.tre.dto;

import java.io.Serializable;
import java.util.List;

public class DeployDetail implements Serializable {

    private String serviceName;
    private List<RateDetails> details;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<RateDetails> getDetails() {
        return details;
    }

    public void setDetails(List<RateDetails> details) {
        this.details = details;
    }
}
