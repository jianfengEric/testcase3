package com.tng.portal.ana.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Zero on 2016/11/4.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnaPermissionDto  implements Serializable{
    private int id;
    private String name;
    private String description;
    private String accessRight;
    private String before;
    private String after;
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

    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public String getBefore() { return before; }

    public void setBefore(String before) { this.before = before; }

    public String getAfter() { return after; }

    public void setAfter(String after) { this.after = after; }
}
