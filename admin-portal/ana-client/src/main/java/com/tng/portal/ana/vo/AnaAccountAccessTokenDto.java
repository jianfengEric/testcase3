package com.tng.portal.ana.vo;

import java.io.Serializable;

public class AnaAccountAccessTokenDto implements Serializable{

    private String token;

    private String expriedtime;

    private String accountid;

    private String remoteaddr;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpriedtime() {
        return expriedtime;
    }

    public void setExpriedtime(String expriedtime) {
        this.expriedtime = expriedtime;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getRemoteaddr() {
        return remoteaddr;
    }

    public void setRemoteaddr(String remoteaddr) {
        this.remoteaddr = remoteaddr;
    }

}
