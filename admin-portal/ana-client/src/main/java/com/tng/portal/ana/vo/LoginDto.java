package com.tng.portal.ana.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/28.
 */
public class LoginDto implements Serializable{
	@ApiModelProperty(value="ANA account username")
    private String username;
	@ApiModelProperty(value="Password require at least one digit, one letter, length from 8 to 20. (Valid characters 0-9, a-z.).")
    private String password;
    private String applicationCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }
}
