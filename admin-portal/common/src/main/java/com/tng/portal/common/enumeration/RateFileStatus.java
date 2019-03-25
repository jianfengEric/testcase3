package com.tng.portal.common.enumeration;

import java.util.stream.Stream;

public enum RateFileStatus {

    PENDING_FOR_PROCESS("PENDING_FOR_PROCESS"),
    PROCESSED("PROCESSED");

    private RateFileStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;

    public static RateFileStatus stringToInstance(String value){
        return Stream.of(RateFileStatus.values()).filter(item -> item.getValue().equals(value)).findFirst().orElse(null);
    }


}
