package com.tng.portal.ana.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Zero on 2016/11/4.
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseDto  implements Serializable{
    private Long id;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
