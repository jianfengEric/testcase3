package com.tng.portal.ana.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chazz on 2016/11/4.
 */
@Entity
@Table(name = "ana_login_session")
public class AnaLoginSession {
    @Id
    @Column(name = "log_id")
//    @GeneratedValue(generator = "SEQ_ANA_LOGIN_SESSION_ID", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "SEQ_ANA_LOGIN_SESSION_ID", sequenceName = "SEQ_ANA_LOGIN_SESSION_ID",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long logId;
    @Column(name = "account")
    private String account;
    @Column(name = "name")
    private String name;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    @Column(name = "language")
    private String language;
    @Column(name = "ip")
    private String ip;
    @Column(name = "session_date_time")
    private Date sessionDateTime;
    @Column(name = "is_login")
    private Boolean isLogin;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(Date sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public Boolean getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }
}
