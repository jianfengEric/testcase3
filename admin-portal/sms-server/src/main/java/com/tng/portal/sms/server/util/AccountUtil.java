package com.tng.portal.sms.server.util;

import com.google.common.base.Joiner;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.Role;
import com.tng.portal.sms.server.vo.AccountDto;

import java.util.ArrayList;
import java.util.List;

public class AccountUtil {
	
	public static AccountDto extractAccountInfo(Account account){
		if(account == null){
			return null;
		}
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountId(account.getAccountId());
		accountDto.setAccountNo(account.getUsername());
		accountDto.setName(account.getFullName());
		List<String> roleNameList = new ArrayList<>();
		if(account.getRoles() != null){
			for(Role role : account.getRoles()){
				roleNameList.add(role.getName());
			}
			accountDto.setRoleNameStr(Joiner.on(",").join(roleNameList));
		}
		return accountDto;
	}

}
