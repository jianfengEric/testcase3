package com.gea.portal.eny.rest;

import java.io.Serializable;

/**
 * Created by Owen on 2017/8/3.
 */
public class BaseRestfulResponse implements Serializable {
    private String status;
    private String errorCode;
    private String messageEN;
    private String messageZhHK;
    private String messageZhCN;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessageEN() {
        return messageEN;
    }

    public void setMessageEN(String messageEN) {
        this.messageEN = messageEN;
    }

    public String getMessageZhHK() {
        return messageZhHK;
    }

    public void setMessageZhHK(String messageZhHK) {
        this.messageZhHK = messageZhHK;
    }

    public String getMessageZhCN() {
        return messageZhCN;
    }

    public void setMessageZhCN(String messageZhCN) {
        this.messageZhCN = messageZhCN;
    }
}
