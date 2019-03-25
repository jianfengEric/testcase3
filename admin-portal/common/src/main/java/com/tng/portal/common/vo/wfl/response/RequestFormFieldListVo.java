package com.tng.portal.common.vo.wfl.response;

import java.io.Serializable;

/**
 * Created by Owen on 2016/11/9.
 */
public class RequestFormFieldListVo implements Serializable {
    private Long id;
    private String name;
    private String dateType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }
}
