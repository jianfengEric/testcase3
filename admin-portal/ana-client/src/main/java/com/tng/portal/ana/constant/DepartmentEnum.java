package com.tng.portal.ana.constant;

/**
 * Created by Owen on 2018/10/10.
 */
public enum DepartmentEnum {
    Finance,
    Compliance,
    Operator,
    Portal;

    public static String getName(int ordinal){
        if(ordinal > DepartmentEnum.values().length-1){
            return "";
        }
        return DepartmentEnum.values()[ordinal].name();
    }

}
