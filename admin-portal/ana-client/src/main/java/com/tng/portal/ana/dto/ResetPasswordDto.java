package com.tng.portal.ana.dto;

import java.io.Serializable;

/**
 * Created by Jimmy on 2018/6/25.
 */
public class ResetPasswordDto implements Serializable {

    private String code;
    private String newPwd;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
    
}
