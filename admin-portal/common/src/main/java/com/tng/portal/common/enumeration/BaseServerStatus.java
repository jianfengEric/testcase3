package com.tng.portal.common.enumeration;


public enum BaseServerStatus {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    ;

    private BaseServerStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
