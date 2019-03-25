package com.tng.portal.common.enumeration;

import java.util.stream.Stream;

/**
 * Created by Owen on 2018/8/27.
 */
public enum Instance {

    PRE_PROD("PRE_PROD"),
    PROD("PROD");

    private Instance(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;

    public static Instance stringToInstance(String value){
        return Stream.of(Instance.values()).filter(item -> item.getValue().equals(value)).findFirst().orElse(null);
    }


}
