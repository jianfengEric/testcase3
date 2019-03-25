package com.tng.portal.sms.server.vo;

import java.io.Serializable;

public class AccountDto implements Serializable {

	private String accountId;
	private String accountNo;
	private String roleNameStr;
	private String name;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getRoleNameStr() {
		return roleNameStr;
	}
	public void setRoleNameStr(String roleNameStr) {
		this.roleNameStr = roleNameStr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
