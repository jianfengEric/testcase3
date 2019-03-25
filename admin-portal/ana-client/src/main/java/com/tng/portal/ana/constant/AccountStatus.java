package com.tng.portal.ana.constant;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Owen on 2017/8/10.
 */
public enum AccountStatus {
    Active("ACT"),
    Inactive("NACT"),
    NotVerified("NVF"),
    Terminated("DEL");

    private String code;

    AccountStatus(String code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public static AccountStatus parse(String code){
        Optional<AccountStatus> optional = Arrays.asList(AccountStatus.values()).stream().filter(item->item.getCode().equals(code)).findFirst();
        return optional.isPresent()?optional.get():null;
    }
    public static String getName(String code){
        AccountStatus em  = parse(code);
        return null==em?"":em.name();
    }
}
