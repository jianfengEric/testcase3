package com.tng.portal.ana.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Zero on 2016/11/10.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto extends  BaseDto implements Serializable{
    private String name;
    private String anaApplication;
    private String description;
    @JsonIgnore
    private Date createDate;
    @JsonIgnore
    private Date lastModifyDate;

    private String type;

    private Boolean internal;

    private String externalGroupId;

    private Boolean isDefault;

    private String roleType;

    public RoleDto() {

    }

    public RoleDto(Long id, String name, String appCode, String description) {
        super.setId(id);
        this.name = name;
        this.anaApplication = appCode;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAnaApplication() {
        return anaApplication;
    }

    public void setAnaApplication(String anaApplication) {
        this.anaApplication = anaApplication;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
}
