package com.tng.portal.ana.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Zero on 2016/11/4.
 */
public class FunctionUpdateDto extends FunctionPostDto implements Serializable {
    @ApiModelProperty(value="ANA function code, ref. ANA_FUNCTION.CODE")
    private String oldCode;
    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }
}
