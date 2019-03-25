package com.tng.portal.email.vo;

import java.io.Serializable;

/**
 * Created by Owen on 2017/10/31.
 */
public class EmailHostDto  implements Serializable{
    private String code;
    private String provider;
    private String host1;
    private String host2;
    private Long requireAuth;
    private String secureType;
    private Long port;
    private Long priority;
    private String status;
    private Long emailSizeLimit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEmailSizeLimit() {
        return emailSizeLimit;
    }

    public void setEmailSizeLimit(Long emailSizeLimit) {
        this.emailSizeLimit = emailSizeLimit;
    }
}
