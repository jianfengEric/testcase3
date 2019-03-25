package com.tng.portal.ana.entity;

import javax.persistence.*;

import com.tng.portal.common.entity.AnaApplication;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Owen on 2017/8/9.
 */
@Table(name = "ANA_ACCOUNT_APPLICATION")
@Entity
//@IdClass(AnaAccountApplicationPK.class)
public class AnaAccountApplication implements Serializable{

    @Id
    @Column(name = "ID")
//    @GeneratedValue(generator="SEQ_ACCOUNT_APPLICATION_ID",strategy= GenerationType.SEQUENCE)
//    @SequenceGenerator(name="SEQ_ACCOUNT_APPLICATION_ID",sequenceName="SEQ_ACCOUNT_APPLICATION_ID", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "BINDING_ACCOUNT_ID")
    private String bindingAccountId;
    @Column(name = "STATUS")
//    @Enumerated(EnumType.STRING)
    private String status;
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    @Column(name = "UPDATE_BY")
    private String updateBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id")
    private AnaAccount anaAccount;

    @OneToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "APPLICATION_CODE", referencedColumnName = "code")
    private AnaApplication anaApplication;

    public String getBindingAccountId() {
        return bindingAccountId;
    }

    public void setBindingAccountId(String bindingAccountId) {
        this.bindingAccountId = bindingAccountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

   public AnaAccount getAnaAccount() {
        return anaAccount;
    }

    public void setAnaAccount(AnaAccount anaAccount) {
        this.anaAccount = anaAccount;
    }

     public AnaApplication getAnaApplication() {
        return anaApplication;
    }

    public void setAnaApplication(AnaApplication anaApplication) {
        this.anaApplication = anaApplication;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
