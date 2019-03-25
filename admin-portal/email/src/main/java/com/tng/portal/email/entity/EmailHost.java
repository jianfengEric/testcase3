package com.tng.portal.email.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Owen on 2016/11/18.
 */
@Entity
@Table(name = "EMAIL_HOST")
public class EmailHost {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "provider")
    private String provider;

    @Column(name = "host1")
    private String host1;

    @Column(name = "host2")
    private String host2;

    @Column(name = "require_auth")
    private Long require_auth;

    @Column(name = "secure_type")
    private String secure_type;


    @Column(name = "port")
    private Long port;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "status")
    private String status;

    @Column(name = "EMAIL_SIZE_LIMIT")
    private Long emailSizeLimit;

    @OneToMany(mappedBy = "emailHost",fetch = FetchType.EAGER)
    @Where(clause = " status = 'ACT' ")
    @OrderBy(" priority asc ")
    private List<EmailAccount> emailAccounts;

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

    public Long getRequire_auth() {
        return require_auth;
    }

    public void setRequire_auth(Long require_auth) {
        this.require_auth = require_auth;
    }

    public String getSecure_type() {
        return secure_type;
    }

    public void setSecure_type(String secure_type) {
        this.secure_type = secure_type;
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

    public List<EmailAccount> getEmailAccounts() {
        return emailAccounts;
    }

    public void setEmailAccounts(List<EmailAccount> emailAccounts) {
        this.emailAccounts = emailAccounts;
    }

    public Long getEmailSizeLimit() { return emailSizeLimit; }

    public void setEmailSizeLimit(Long emailSizeLimit) { this.emailSizeLimit = emailSizeLimit; }
}
