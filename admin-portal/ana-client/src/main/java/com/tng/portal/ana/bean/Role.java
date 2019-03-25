package com.tng.portal.ana.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public class Role implements Serializable{
    private String name;
    private int permissionSum;
    private List<Function> functions;
    private String appCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(int permissionSum) {
        this.permissionSum = permissionSum;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

}
