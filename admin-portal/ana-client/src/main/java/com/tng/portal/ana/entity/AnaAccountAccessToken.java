package com.tng.portal.ana.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Zero on 2016/11/14.
 */
@Entity
@Table(name = "ana_account_access_token")
public class AnaAccountAccessToken{
    @Id
    @Column(name = "id")
//    @GeneratedValue(generator = "SEQ_ANA_ACCESS_TOKEN_ID", strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name = "SEQ_ANA_ACCESS_TOKEN_ID", sequenceName = "SEQ_ANA_ACCESS_TOKEN_ID",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private AnaAccount anaAccount;
    @Column(name = "token")
    private String token;
    @Column(name = "expiration_time")
    private Date expriedTime;
    @Column(name = "token_component")
    private String tokenComponent;

    @Transient
    private String account;

    @Transient
    private String remoteAddr;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnaAccount getAnaAccount() {
        return anaAccount;
    }

    public void setAnaAccount(AnaAccount anaAccount) {
        this.anaAccount = anaAccount;
    }

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

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }
}
