package com.tng.portal.sms.util;

import java.util.List;

import com.tng.portal.ana.bean.Account;

public class EmailParameterUtil {
	
    public static String generateReceivers(List<Account> roleAccs){
        StringBuilder result = new StringBuilder();
        if (roleAccs == null || roleAccs.isEmpty()){
            return result.toString();
        }
        int size = roleAccs.size();
        for (int i = 0; i < size; i++) {
            Account account = roleAccs.get(i);
            if (i < size - 1){
                result.append(account.getEmail() + ",");
            }else {
                result.append(account.getEmail());
            }
        }
        return result.toString();
    }
}
