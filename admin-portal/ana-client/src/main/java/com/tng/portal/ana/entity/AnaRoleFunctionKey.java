package com.tng.portal.ana.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Roger on 2017/8/28.
 */
@Embeddable
public class AnaRoleFunctionKey implements Serializable {

    private Long roleId;

    private String functionCode;
    public AnaRoleFunctionKey() {
    }
    public AnaRoleFunctionKey(Long roleId, String functionCode) {
        this.roleId = roleId;
        this.functionCode = functionCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
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
        if(! (obj instanceof AnaRoleFunctionKey)) {
            return false;
        }
        AnaRoleFunctionKey key = (AnaRoleFunctionKey)obj;
        if(!roleId.equals(key.getRoleId())) {
            return false;
        }
        return functionCode.equals(key.getFunctionCode());
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = roleId == null ? 0 : roleId.hashCode();
        result = 29 * (functionCode == null ? 0 : functionCode.hashCode()) + result;
        return result;
    }
}
