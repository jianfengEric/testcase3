package com.tng.portal.ana.vo;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/4.
 */
public class FunctionDto extends BaseDto implements Serializable{

    private String code;
    private String description;
    private int permissionSum;

    private ApplicationDto application;

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

    public ApplicationDto getApplication() {
        return application;
    }

    public void setApplication(ApplicationDto application) {
        this.application = application;
    }
}
