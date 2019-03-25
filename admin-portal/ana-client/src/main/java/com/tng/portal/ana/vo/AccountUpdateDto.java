package com.tng.portal.ana.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/8.
 */
public class AccountUpdateDto extends AccountPostDto implements Serializable{
	@ApiModelProperty(value="ANA login account id, ref. ANA_ACCOUNT.ID")
    private String id;
    private String userType;
    private Long departmentId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
