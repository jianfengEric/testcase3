package com.gea.portal.eny.rest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zero on 2016/11/8.
 */
public class AccountRegisteredDto implements Serializable {
    private String account;
    private String name;
    private String password;
    private String rePassword;
    private String mobile;
    private String email;
    private String language;
    private List<String> roleNames;

    private Boolean internal;
    private String externalGroupId;
    private String loginAccountid;

    private String systemCode;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }
    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

    public String getLoginAccountid() {
        return loginAccountid;
    }

    public void setLoginAccountid(String loginAccountid) {
        this.loginAccountid = loginAccountid;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
