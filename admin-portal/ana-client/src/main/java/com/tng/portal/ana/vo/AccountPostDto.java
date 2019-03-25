package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/8.
 */
public class AccountPostDto implements Serializable {
	@ApiModelProperty(value="ANA login account, ref. ANA_ACCOUNT.ACCOUNT")
    private String account;
    @ApiModelProperty(value="ANA account firstName")
    private String firstName;
    @ApiModelProperty(value="ANA account lastName")
    private String lastName;
	@ApiModelProperty(value="Password require at least one digit, one letter, length from 8 to 20. (Valid characters 0-9, a-z.).")
    private String password;
    @ApiModelProperty(value="the same as password")
    private String rePassword;
    @ApiModelProperty(value="8 digits, eg. 666888")
    private String mobile;
    private String email;
    @ApiModelProperty(value="en,zh-cn,zh-tw")
    private String language;

    private List<Long> roles;

    @ApiModelProperty(value="1-internal, 0-not internal")
    private Boolean internal;

    @ApiModelProperty(value="external user belong to which company(MID)")
    private String externalGroupId;

    @ApiModelProperty(value="external user belong to which loginAccount id")
    private String loginAccountid;

    @ApiModelProperty(value="System Code")
    private String systemCode;

    @ApiModelProperty(value="binding application")
    private List<AnaAccountApplicationDto> bindingData;

    @ApiModelProperty(value="email send to")
    private String emailSendTo;

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

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    public String getLoginAccountid() {
        return loginAccountid;
    }

    public void setLoginAccountid(String loginAccountid) {
        this.loginAccountid = loginAccountid;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public List<AnaAccountApplicationDto> getBindingData() {
        return bindingData;
    }

    public void setBindingData(List<AnaAccountApplicationDto> bindingData) {
        this.bindingData = bindingData;
    }

    public String getEmailSendTo() {
        return emailSendTo;
    }

    public void setEmailSendTo(String emailSendTo) {
        this.emailSendTo = emailSendTo;
    }
}
