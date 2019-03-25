package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/8.
 */
public class AccountDetailDto implements Serializable {
    private String id;
	@ApiModelProperty(value="ANA login account, ref. ANA_ACCOUNT.ACCOUNT")
    private String account;
	@ApiModelProperty(value="ANA account username")
    private String name;
	@ApiModelProperty(value="Password require at least one digit, one letter, length from 8 to 20. (Valid characters 0-9, a-z.).")
    private String password;
    @ApiModelProperty(value="the same as password")
    private String rePassword;
    @ApiModelProperty(value="8 digits, eg. 666888")
    private String mobile;
    private String email;
    
    @ApiModelProperty(value="external user belong to which company(MID)")
    private String externalGroupId;
    @ApiModelProperty(value="1-internal, 0-not internal")
    private Boolean internal;

    private List<RoleDetailDto> roles;

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

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    public List<RoleDetailDto> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleDetailDto> roles) {
        this.roles = roles;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
}
