package com.tng.portal.ana.constant;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Owen on 2017/8/10.
 */
public enum AccountType {

    ROOT("RT"),
    Admin("ADM"),
    Operator("OPR"),
    Manager("MGR");

    private String code;

    AccountType(String code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public static AccountType parse(String code){
        Optional<AccountType> optional = Arrays.asList(AccountType.values()).stream().filter(item->item.getCode().equals(code)).findFirst();
        return optional.isPresent()?optional.get():null;
    }

    public static String getName(String code){
        AccountType em  = parse(code);
        return null==em?"":em.name();
    }

    public static boolean equals(AccountType accountType, String type){
        if(Objects.isNull(accountType) || StringUtils.isBlank(type)){
            return false;
        }
        return accountType.getCode().equalsIgnoreCase(type) || accountType.name().equalsIgnoreCase(type);
    }

}
