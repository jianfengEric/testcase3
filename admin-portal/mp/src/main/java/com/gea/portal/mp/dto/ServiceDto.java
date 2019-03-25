package com.gea.portal.mp.dto;

import java.io.Serializable;

/**
 * Created by dong on 2018/9/10.
 */
public class ServiceDto  implements Serializable{

    private int id;
    private String code;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
