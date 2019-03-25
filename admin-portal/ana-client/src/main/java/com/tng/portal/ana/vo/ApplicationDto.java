package com.tng.portal.ana.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Zero on 2016/11/4.
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApplicationDto  implements Serializable{
    private String code;
    private String name;
    private String description;
    private Boolean isDisplay;
    private String urlEnpoin;

    public ApplicationDto() {

    }
    public ApplicationDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
    public ApplicationDto(String code, String name,String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public ApplicationDto(String code, String name,String description, String urlEnpoin) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.urlEnpoin = urlEnpoin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getUrlEnpoin() {
        return urlEnpoin;
    }

    public void setUrlEnpoin(String urlEnpoin) {
        this.urlEnpoin = urlEnpoin;
    }
}
