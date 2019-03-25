package com.tng.portal.ana.entity;

import javax.persistence.*;

/**
 * Created by Zero on 2016/11/4.
 */
@Entity
@Table(name = "ana_role_function_permission")
public class AnaRoleFunctionPermission {
    @EmbeddedId
    private AnaRoleFunctionPk anaRoleFunctionPk;


    @Column(name = "permissions_sum")
    private int permissionsSum;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id",referencedColumnName = "id",insertable = false, updatable = false)
    private AnaRole anaRole;

    @ManyToOne(optional = false)
    @JoinColumn(name = "function_code",referencedColumnName = "code",insertable = false, updatable = false)
    private AnaFunction anaFunction;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException

    public int getPermissionsSum() {
        return permissionsSum;
    }

    public void setPermissionsSum(int permissionsSum) {
        this.permissionsSum = permissionsSum;
    }

    public AnaRoleFunctionPk getAnaRoleFunctionPk() {
        return anaRoleFunctionPk;
    }

    public void setAnaRoleFunctionPk(AnaRoleFunctionPk anaRoleFunctionPk) {
        this.anaRoleFunctionPk = anaRoleFunctionPk;
    }

    public AnaRole getAnaRole() {
        return anaRole;
    }

    public void setAnaRole(AnaRole anaRole) {
        this.anaRole = anaRole;
    }

    public AnaFunction getAnaFunction() {
        return anaFunction;
    }

    public void setAnaFunction(AnaFunction anaFunction) {
        this.anaFunction = anaFunction;
    }

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }
}
