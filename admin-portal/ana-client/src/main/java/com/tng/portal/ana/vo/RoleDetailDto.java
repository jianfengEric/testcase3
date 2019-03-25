package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public class RoleDetailDto extends  BaseDto implements Serializable{
    private String name;
    private String description;

    private Date createDate;
    private Date lastModifyDate;

    private ApplicationDto application;

    private List<RoleFunctionPermissionDto> functionList;

    private String type;


    public RoleDetailDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ApplicationDto getApplication() {
        return application;
    }

    public void setApplication(ApplicationDto application) {
        this.application = application;
    }

    public List<RoleFunctionPermissionDto> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<RoleFunctionPermissionDto> functionList) {
        this.functionList = functionList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
