package com.tng.portal.common.enumeration;

/**
 * Created by Owen on 2018/8/31.
 */
public enum DeployStatus {

	PENDING_FOR_DEPLOY("PENDING_FOR_DEPLOY"),
	DEPLOYED("DEPLOYED"),
	FAIL("FAIL"),
    CANCEL("CANCEL"),
    ;

    private DeployStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
