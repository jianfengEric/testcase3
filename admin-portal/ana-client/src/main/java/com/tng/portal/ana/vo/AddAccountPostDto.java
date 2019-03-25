package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import com.tng.portal.ana.constant.AccountType;
import com.tng.portal.ana.constant.DepartmentEnum;
import com.tng.portal.ana.constant.EmailSendTo;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/8.
 */
public class AddAccountPostDto  implements Serializable{
	@ApiModelProperty(value="ANA login account, ref. ANA_ACCOUNT.ACCOUNT")
    private String account;
    @ApiModelProperty(value="ANA account firstName")
    private String firstName;
    @ApiModelProperty(value="ANA account lastName")
    private String lastName;
    @ApiModelProperty(value="8 digits, eg. 666888")
    private String mobile;
    private String email;
    @ApiModelProperty(value="email send to")
    private EmailSendTo emailSendTo;

    private String externalGroupId;

    private String merchantId;

    private AccountType userType;

    private String password;

    private List<Long> roles;

    private DepartmentEnum department;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMerchantId() { return merchantId; }

    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    public DepartmentEnum getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEnum department) {
        this.department = department;
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
}
