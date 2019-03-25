package com.tng.portal.ana.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import com.tng.portal.common.entity.AnaApplication;

import java.util.List;

/**
 * Created by Zero on 2016/11/4.
 */
@Entity
@Table(name = "ana_function")
public class AnaFunction {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;
    @Column(name = "permission_sum")
    private int permissionSum;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "application_code")
    private AnaApplication anaApplication;

    @OneToMany(cascade=CascadeType.MERGE,mappedBy = "anaFunction")
    private List<AnaRoleFunctionPermission> anaFunctionRoles;

    @Version
    @Column(name = "optimisticLockVersion")
    private long optimisticLockVersion; //throws OptimisticLockException

    @ApiModelProperty(value="1-external, 0-internal")
    @Column(name = "external")
    private Boolean external;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(int permissionSum) {
        this.permissionSum = permissionSum;
    }

    public AnaApplication getAnaApplication() {
        return anaApplication;
    }

    public void setAnaApplication(AnaApplication anaApplication) {
        this.anaApplication = anaApplication;
    }

    public List<AnaRoleFunctionPermission> getAnaFunctionRoles() {
        return anaFunctionRoles;
    }

    public void setAnaFunctionRoles(List<AnaRoleFunctionPermission> anaFunctionRoles) {
        this.anaFunctionRoles = anaFunctionRoles;
    }

    public long getOptimisticLockVersion() {
        return optimisticLockVersion;
    }

    public void setOptimisticLockVersion(long optimisticLockVersion) {
        this.optimisticLockVersion = optimisticLockVersion;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }
}
