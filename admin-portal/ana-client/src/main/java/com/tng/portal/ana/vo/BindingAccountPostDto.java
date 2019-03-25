package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2017/8/9.
 */
public class BindingAccountPostDto  implements Serializable{

    private String id;

    private List<AnaAccountApplicationDto> bindingData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<AnaAccountApplicationDto> getBindingData() {
        return bindingData;
    }

    public void setBindingData(List<AnaAccountApplicationDto> bindingData) {
        this.bindingData = bindingData;
    }
}
