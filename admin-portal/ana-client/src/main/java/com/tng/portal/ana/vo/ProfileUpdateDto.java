package com.tng.portal.ana.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/8.
 */
public class ProfileUpdateDto implements Serializable{
	@ApiModelProperty(value="ANA login account id, ref. ANA_ACCOUNT.ID")
    private String id;
    @ApiModelProperty(value="ANA account firstName")
    private String firstName;
    @ApiModelProperty(value="ANA account lastName")
    private String lastName;
    @ApiModelProperty(value="8 digits, eg. 666888")
    private String mobile;
    private String email;
    @ApiModelProperty(value="en,zh-cn,zh-tw")
    private String language;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
}
