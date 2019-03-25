package com.tng.portal.common.vo.wfl.request;

import java.io.Serializable;

/**
 * Created by Owen on 2016/11/9.
 */
public class RequestFormDataVo implements Serializable {
    private String fieldName;
    private String fieldValue;


    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "RequestFormDataVo{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldValue='" + fieldValue + '\'' +
                '}';
    }
}
