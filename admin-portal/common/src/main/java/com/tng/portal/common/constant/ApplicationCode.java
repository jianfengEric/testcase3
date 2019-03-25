package com.tng.portal.common.constant;

/**
 * Created by Owen on 2017/9/4.
 */
public enum ApplicationCode {
    ANA("ana"),
    BTU("btu"),
    JIR("jir"),
    MAM("mam"),
    SMS("sms"),
    SMM("sms-server");

    private String code;

    ApplicationCode(String code) {
        this.code = code;
    }
    @Override
    public String toString() {
        return code;
    }

    public String getCode() {
        return code;
    }

}
