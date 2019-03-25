package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import com.tng.portal.ana.constant.AccountType;
import com.tng.portal.ana.constant.EmailSendTo;

/**
 * Created by Roger on 2017/12/6.
 */
public class RemoteAccountPostDto implements Serializable {

    private String account;
    private String name;
    private String mobile;
    private String email;
    private EmailSendTo emailSendTo;
    private String externalGroupId;
    private String merchantId;
    private AccountType userType;
    private String password;
    private String userId;
    private String mamUserId;
    private Long departmentId;
    private String applicationCode;
    private List<AnaAccountApplicationDto> bindingData;

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

    public EmailSendTo getEmailSendTo() {
        return emailSendTo;
    }

    public void setEmailSendTo(EmailSendTo emailSendTo) {
        this.emailSendTo = emailSendTo;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMamUserId() {
        return mamUserId;
    }

    public void setMamUserId(String mamUserId) {
        this.mamUserId = mamUserId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public List<AnaAccountApplicationDto> getBindingData() {
        return bindingData;
    }

    public void setBindingData(List<AnaAccountApplicationDto> bindingData) {
        this.bindingData = bindingData;
    }
}
