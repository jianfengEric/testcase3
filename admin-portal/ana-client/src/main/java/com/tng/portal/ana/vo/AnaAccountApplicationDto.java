package com.tng.portal.ana.vo;

import java.io.Serializable;

import com.tng.portal.ana.constant.AccountType;

/**
 * Created by Owen on 2017/8/9.
 */
public class AnaAccountApplicationDto  implements Serializable{
    private String applicationCode;
    private String account;
    private AccountType userType;
    private String actionType;// add/connect

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

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
