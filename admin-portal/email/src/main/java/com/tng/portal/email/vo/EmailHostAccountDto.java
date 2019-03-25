package com.tng.portal.email.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Owen on 2017/10/31.
 */
public class EmailHostAccountDto  implements Serializable{
    private Long id;
    @ApiModelProperty(value="email host code")
    private String code;
    private String account;
    private String password;
    private Long priority;
    private String defaultSenderEmail;
    private String status;
    @ApiModelProperty(value="email account quota type, D- daily, M-month")
    private String quotaType;
    private Long quotaCount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getDefaultSenderEmail() {
        return defaultSenderEmail;
    }

    public void setDefaultSenderEmail(String defaultSenderEmail) {
        this.defaultSenderEmail = defaultSenderEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public Long getQuotaCount() {
        return quotaCount;
    }

    public void setQuotaCount(Long quotaCount) {
        this.quotaCount = quotaCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

