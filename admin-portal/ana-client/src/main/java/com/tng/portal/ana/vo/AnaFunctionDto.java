package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Zero on 2016/11/4.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnaFunctionDto extends BaseDto implements Serializable{

    private String code;
    private String description;
    private int permissionSum;
    private String accessRight;
    private String before;
    private String after;
    private List<AnaPermissionDto> permissions;

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

    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public List<AnaPermissionDto> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<AnaPermissionDto> permissions) {
        this.permissions = permissions;
    }

    public String getBefore() { return before; }

    public void setBefore(String before) { this.before = before; }

    public String getAfter() { return after; }

    public void setAfter(String after) { this.after = after; }
}
