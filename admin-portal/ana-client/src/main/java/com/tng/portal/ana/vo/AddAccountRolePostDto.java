package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2016/11/8.
 */
public class AddAccountRolePostDto  implements Serializable{

    private String id;

    private List<Long> roles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }
}
