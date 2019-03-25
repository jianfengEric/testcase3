package com.tng.portal.ana.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Zero on 2016/11/8.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AccountDto implements Serializable{
    private String id;
    private String account;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;
    private Date createdTime;
    private Date updatedTime;
    private List<Long> roles;
    private Boolean internal;
    private String externalGroupId;
    private String createdBy;
    private String updatedBy;
    private String systemUrl;
    private String status;
    private String applications;
    private String email;
    private String ssoLoginId;
    private List<AnaAccountApplicationViewDto> bindingAccounts;
    private String emailSendTo;
    private String userType;
    private Long department;
    private List<AccountDto> clientAccountList;
    private String participantName;

    public String getSsoLoginId() {
		return ssoLoginId;
	}
	public void setSsoLoginId(String ssoLoginId) {
		this.ssoLoginId = ssoLoginId;
	}
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
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<Long> getRoles() {
        return roles;
    }
    public void setRoles(List<Long> roles) {
        this.roles = roles;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
	public String getSystemUrl() {
		return systemUrl;
	}
	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplications() {
        return applications;
    }

    public void setApplications(String applications) {
        this.applications = applications;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AnaAccountApplicationViewDto> getBindingAccounts() {
        return bindingAccounts;
    }

    public void setBindingAccounts(List<AnaAccountApplicationViewDto> bindingAccounts) {
        this.bindingAccounts = bindingAccounts;
    }

    public String getEmailSendTo() {
        return emailSendTo;
    }

    public void setEmailSendTo(String emailSendTo) {
        this.emailSendTo = emailSendTo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public List<AccountDto> getClientAccountList() {
        return clientAccountList;
    }

    public void setClientAccountList(List<AccountDto> clientAccountList) {
        this.clientAccountList = clientAccountList;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
}
