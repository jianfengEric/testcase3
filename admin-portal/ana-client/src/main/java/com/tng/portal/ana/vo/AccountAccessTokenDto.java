package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.Date;


public class AccountAccessTokenDto implements Serializable {


    private String token;

    private Date expriedTime;

    private String tokenComponent;

    private String account;

    private String remoteAddr;

    private boolean updateStatus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpriedTime() {
        return expriedTime;
    }

    public void setExpriedTime(Date expriedTime) {
        this.expriedTime = expriedTime;
    }

    public String getTokenComponent() {
        return tokenComponent;
    }

    public void setTokenComponent(String tokenComponent) {
        this.tokenComponent = tokenComponent;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public boolean isUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(boolean updateStatus) {
        this.updateStatus = updateStatus;
    }
}
