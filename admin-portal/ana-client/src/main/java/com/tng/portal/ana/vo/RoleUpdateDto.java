package com.tng.portal.ana.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/10.
 */
public class RoleUpdateDto extends  RolePostDto implements Serializable {
    @ApiModelProperty(value="ANA ROLE id, ref. ANA_ROLE.ID")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
