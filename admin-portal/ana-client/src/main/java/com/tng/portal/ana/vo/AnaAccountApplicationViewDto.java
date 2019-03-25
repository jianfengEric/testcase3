package com.tng.portal.ana.vo;

import java.io.Serializable;

/**
 * Created by Owen on 2017/8/9.
 */
public class AnaAccountApplicationViewDto  implements Serializable{
    private String applicationCode;
    private String account;
    private String mid;
    private String status;
    private String userType;

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
