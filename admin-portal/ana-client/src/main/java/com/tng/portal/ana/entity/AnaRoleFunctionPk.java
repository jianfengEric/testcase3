package com.tng.portal.ana.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Zero on 2016/11/4.
 */
@Embeddable
public class AnaRoleFunctionPk implements Serializable{
    @Basic(optional = false)
    @Column(name = "role_id")
    private Long roleId;
    @Basic(optional = false)
    @Column(name = "function_code")
    private String functionCode;

    public AnaRoleFunctionPk() {

    }
    public AnaRoleFunctionPk(Long roleId, String functionCode) {
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
        if (this == obj){
            return true;
        }
        if (obj != null && obj.getClass()==AnaRoleFunctionPk.class){
            AnaRoleFunctionPk pk = (AnaRoleFunctionPk) obj;
            return pk.getRoleId()==getRoleId()
                    && pk.getFunctionCode() == getFunctionCode();

        }
        return false;
    }

    @Override
    public int hashCode() {
        return getRoleId().hashCode()*31 + getFunctionCode().hashCode();
    }
}
