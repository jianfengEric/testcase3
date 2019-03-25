package com.tng.portal.ana.vo;

import java.io.Serializable;

/**
 * Created by Zero on 2017/1/19.
 */
public class ExternalAccountDto implements Serializable{
    private String id;
    private String account;
    private String name;
    private String externalGroupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }
}
