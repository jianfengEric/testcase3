package com.tng.portal.ana.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Chazz on 2016/11/4.
 */
public class PermissionPostDto implements Serializable {
    @ApiModelProperty(value="ANA permission id, ref. ANA_PERMISSION.ID")
    private int id;
    @ApiModelProperty(value="ANA function name, ref. ANA_FUNCTION.NAME")
    private String name;
    @ApiModelProperty(value="ANA function description, ref. ANA_FUNCTION.DESCRIPTION")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
