package com.tng.portal.ana.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/8.
 */
public class AccountUpPassDto implements Serializable{
	@ApiModelProperty(value="Password require at least one digit, one letter, length from 8 to 20. (Valid characters 0-9, a-z.).")
    private String originalPassword;
    @ApiModelProperty(value="Password require at least one digit, one letter, length from 8 to 20. (Valid characters 0-9, a-z.).")
    private String newPassword;
    @ApiModelProperty(value="the same as password")
    private String reNewPassword;

    public String getOriginalPassword() {
        return originalPassword;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getReNewPassword() {
        return reNewPassword;
    }

    public void setReNewPassword(String reNewPassword) {
        this.reNewPassword = reNewPassword;
    }
}
