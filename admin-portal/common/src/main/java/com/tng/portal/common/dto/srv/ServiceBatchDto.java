package com.tng.portal.common.dto.srv;

import java.io.Serializable;

/**
 * Created by Eric on 2018/11/27.
 */

public class ServiceBatchDto implements Serializable{
    private Long serviceId;
    private String markup;
    private String serverCode;
    private String serverName;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }


    public String getMarkup() {
        return markup;
    }

    public void setMarkup(String markup) {
        this.markup = markup;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }


}
