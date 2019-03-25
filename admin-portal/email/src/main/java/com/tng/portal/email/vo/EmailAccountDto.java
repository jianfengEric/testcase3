package com.tng.portal.email.vo;

import java.io.Serializable;


public class EmailAccountDto implements Serializable{

    private Long accountId;
    private String account;

    private String password;

    private String defaultSenderEmail;

    private String host1;

    private String host2;

    private Long requireAuth;

    private String secureType;

    private Long port;

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

    public String getDefaultSenderEmail() {
        return defaultSenderEmail;
    }

    public void setDefaultSenderEmail(String defaultSenderEmail) {
        this.defaultSenderEmail = defaultSenderEmail;
    }

    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public String getHost2() {
        return host2;
    }

    public void setHost2(String host2) {
        this.host2 = host2;
    }

    public Long getRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(Long requireAuth) {
        this.requireAuth = requireAuth;
    }

    public String getSecureType() {
        return secureType;
    }

    public void setSecureType(String secureType) {
        this.secureType = secureType;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
