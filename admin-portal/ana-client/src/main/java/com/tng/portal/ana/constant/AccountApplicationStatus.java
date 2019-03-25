package com.tng.portal.ana.constant;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Owen on 2017/8/10.
 */
public enum AccountApplicationStatus {
    Temporary("TEMP"),
    Connected("LINK");

    private String code;

    AccountApplicationStatus(String code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public static AccountApplicationStatus parse(String code){
        Optional<AccountApplicationStatus> optional = Arrays.asList(AccountApplicationStatus.values()).stream().filter(item->item.getCode().equals(code)).findFirst();
        return optional.isPresent()?optional.get():null;
    }
    public static String getName(String code){
        AccountApplicationStatus em  = parse(code);
        return null==em?"":em.name();
    }
}
