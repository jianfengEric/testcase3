package com.tng.portal.ana.vo;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/10.
 */
public class RoleFunctionPermissionDto implements Serializable{
    private Long roleId;
    private String code;

    private int permissionSum;

    public RoleFunctionPermissionDto() {

    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(int permissionSum) {
        this.permissionSum = permissionSum;
    }
}
