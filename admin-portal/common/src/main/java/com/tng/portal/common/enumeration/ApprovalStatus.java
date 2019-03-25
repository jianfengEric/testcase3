package com.tng.portal.common.enumeration;

/**
 * Created by Owen on 2018/9/3.
 */
public enum ApprovalStatus {
    PENDING_APPROVAL("Pending Approval"),
    EDITABLE("Editable");


    ApprovalStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;

}
