package com.tng.portal.ana.bean;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/15.
 */
public class Function implements Serializable{
    private String code;
    private int permissionSum;

    public int getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(int permissionSum) {
        this.permissionSum = permissionSum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
