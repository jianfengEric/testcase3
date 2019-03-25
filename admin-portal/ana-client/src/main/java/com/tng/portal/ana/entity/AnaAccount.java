package com.tng.portal.ana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tng.portal.common.entity.BaseAuditEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Zero on 2016/11/4.
 */
@Entity
@Table(name = "ANA_ACCOUNT")
public class AnaAccount extends BaseAuditEntity{
    @Id
    @Column(name = "id")
    @GenericGenerator(name="pk_uuid",strategy = "uuid")
    @GeneratedValue(generator = "pk_uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @GeneratedValue(generator ="system-uuid")
    private String id;

    @ApiModelProperty(value="ANA login account, ref. ANA_ACCOUNT.ACCOUNT")
    @Column(name = "account")
    private String account;
    @Column(name = "email")
    private String email;
    @ApiModelProperty(value="ANA account firstName")
    @Column(name = "first_name")
    private String firstName;
    @ApiModelProperty(value="ANA account lastName")
    @Column(name = "last_name")
    private String lastName;
    @ApiModelProperty(value="Password require at least one digit, one letter, length from 8 to 20. (Valid characters 0-9, a-z.).")
    @Column(name = "password")
    private String password;
    @ApiModelProperty(value="the account created time ")
    @Column(name = "created_time")
    private Date createdTime;
    @ApiModelProperty(value="the account created by which user")
    @Column(name = "created_by")
    private String createdBy;
    @ApiModelProperty(value="the account mobile number, 8 digits, eg. 666888")
    @Column(name = "mobile")
    private String mobile;
    @ApiModelProperty(value="en,zh-cn,zh-tw")
    @Column(name = "language")
    private String language;

    @ApiModelProperty(value="1-internal, 0-external")
    @Column(name = "internal")
    private Boolean internal;

    @Column(name = "MERCHANT_ID")
    private Long merchantId;

    @ApiModelProperty(value="external user belong to which merchant(MID)")
    @Column(name = "external_group_id")
    private String externalGroupId;

    @OneToOne(mappedBy = "anaAccount")
    private AnaAccountAccessToken accountToken;

    @ManyToMany(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinTable(name = "ana_account_role_map",
            joinColumns = {@JoinColumn(name = "account_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")})
    private List<AnaRole> anaRoles;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "anaAccount")
    private List<AnaAccountApplication> anaAccountApplications;

    @Column(name = "user_type")
//    @Enumerated(EnumType.STRING)
    private String userType;

    @Column(name = "VERIFY_EMAIL_TYPE")
    private String verifyEmailType;

    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;
    
    @Column(name = "reset_pwd_time")
    private Date resetPwdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = StringUtils.trimToEmpty(account);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<AnaRole> getAnaRoles() {
        return anaRoles;
    }

    public void setAnaRoles(List<AnaRole> anaRoles) {
        this.anaRoles = anaRoles;
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

    public AnaAccountAccessToken getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(AnaAccountAccessToken accountToken) {
        this.accountToken = accountToken;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }

    public List<AnaAccountApplication> getAnaAccountApplications() {
        return anaAccountApplications;
    }

    public void setAnaAccountApplications(List<AnaAccountApplication> anaAccountApplications) {
        this.anaAccountApplications = anaAccountApplications;
    }

    public String getVerifyEmailType() {
        return verifyEmailType;
    }

    public void setVerifyEmailType(String verifyEmailType) {
        this.verifyEmailType = verifyEmailType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonIgnore
    public String getFullName(){
        String name = "";
        if(StringUtils.isNotBlank(firstName)){
            name = firstName;
        }
        if(StringUtils.isNotBlank(lastName)){
            name += " " + lastName;
        }
        return name;
    }

	public Date getResetPwdTime() {
		return resetPwdTime;
	}

	public void setResetPwdTime(Date resetPwdTime) {
		this.resetPwdTime = resetPwdTime;
	}

}
