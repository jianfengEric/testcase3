package com.tng.portal.ana.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by Zero on 2016/12/7.
 */
public class AccountUsernameDto implements Serializable{
	private String accountId;
	
	@ApiModelProperty(value="ANA account username")
    private String username;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
        return username;
    }

	public void setUsername(String username) {
        this.username = username;
    }
}
