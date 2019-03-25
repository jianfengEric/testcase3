package com.tng.portal.ana.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/4.
 */
public class FunctionPostDto implements Serializable{
    @ApiModelProperty(value="ANA function code, ref. ANA_FUNCTION.CODE")
    private String code;
    @ApiModelProperty(value="function description")
    private String description;
    @ApiModelProperty(value="permission sum, such as 2,4,8...")
    private int permissionSum;

    private String applicationCode;

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


    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }


    public int getPermissionSum() {
        return permissionSum;
    }

    public void setPermissionSum(int permissionSum) {
        this.permissionSum = permissionSum;
    }
}
