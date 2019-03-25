package com.gea.portal.dpy.enumeration;

/**
 * Created by Eric on 2018/11/1.
 */
public enum DeploySyncType {

    ADD("add"),
    DELETE("delete"),
    NONE("none"),
    UPDATE("update")
    ;

    private DeploySyncType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
