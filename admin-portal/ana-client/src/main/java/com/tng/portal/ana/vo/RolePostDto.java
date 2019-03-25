package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/10.
 */
public class RolePostDto implements Serializable {
    @ApiModelProperty(value="ANA ROLE name, ref. ANA_ROLE.NAME")
    private String name;
    @ApiModelProperty(value="ANA ROLE description, ref. ANA_ROLE.DESCRIPTION")
    private String description;
    @ApiModelProperty(value="ANA ROLE applicationCode, ref. ANA_ROLE.APPLICATION_CODE")
    private String applicationCode;
    @ApiModelProperty(value="ANA Function list, ref. ANA_FUNCTION.CODE")
    List<Function> functionList;
    @ApiModelProperty(value="ANA ROLE id, ref. ANA_ROLE.TYPE")
    private String type;

    public RolePostDto() {

    }

    public RolePostDto(Long id, String name, String applicationCode, String description) {
        this.name = name;
        this.description = description;
        this.applicationCode = applicationCode;
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

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public List<Function> getFunctionList() {
        return functionList;
    }

    public void setFunctionList(List<Function> functionList) {
        this.functionList = functionList;
    }

    /**
     * Created by Zero on 2016/11/4.
     */
    public static  class Function{
        private String code;
        private int permissionSum;

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }

        public int getPermissionSum() {
            return permissionSum;
        }

        public void setPermissionSum(int permissionSum) {
            this.permissionSum = permissionSum;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
