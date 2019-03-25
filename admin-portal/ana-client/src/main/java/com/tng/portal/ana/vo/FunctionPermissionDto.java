package com.tng.portal.ana.vo;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/15.
 */
public class FunctionPermissionDto  implements Serializable{
    private String functionCode;
    private int permissionSum;

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public int getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(int permissionSum) {
        this.permissionSum = permissionSum;
    }
}
