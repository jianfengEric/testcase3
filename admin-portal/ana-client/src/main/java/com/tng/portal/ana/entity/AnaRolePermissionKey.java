package com.tng.portal.ana.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Roger on 2017/8/28.
 */
@Embeddable
public class AnaRolePermissionKey implements Serializable {

    private Long roleId;

    private Integer permissionId;

    private String functionCode;

    public AnaRolePermissionKey() {
    }
    public AnaRolePermissionKey(Long roleId, Integer permissionId,String functionCode) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.functionCode = functionCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(! (obj instanceof AnaRolePermissionKey)) {
            return false;
        }
        AnaRolePermissionKey key = (AnaRolePermissionKey)obj;
        if(!roleId.equals(key.getRoleId())) {
            return false;
        }
        if(!permissionId.equals(key.getPermissionId())) {
            return false;
        }
        return functionCode.equals(key.getFunctionCode());
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = roleId == null ? 0 : roleId.hashCode();
        result = (functionCode == null ? 0 : functionCode.hashCode())+result;
        result = 29 * (permissionId == null ? 0 : permissionId.hashCode()) + result;
        return result;
    }
}
