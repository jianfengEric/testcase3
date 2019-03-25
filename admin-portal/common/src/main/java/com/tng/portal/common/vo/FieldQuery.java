package com.tng.portal.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tng.portal.common.util.DateUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Roger on 2017/10/25.
 */
//FieldQuery
public class FieldQuery implements Serializable {
    private String fieldName;
    private String queryType;   //contains equal isbank isnotbank in notin startswith startsnotwith endswith endsnotwith
    private String fieldValue;
    private String fieldType;
    private String fieldPattern;
    private List<String> fieldSelect;
    public FieldQuery(){}
    public FieldQuery(String fieldName, String queryType, String fieldValue, String fieldType, String fieldPattern,List<String> fieldSelect) {
        this.fieldName = fieldName;
        this.queryType = queryType;
        this.fieldValue = fieldValue;
        this.fieldType = fieldType;
        this.fieldPattern = fieldPattern;
        this.fieldSelect = fieldSelect;
    }

    public String getFieldName() { return fieldName; }

    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getQueryType() { return queryType; }

    public void setQueryType(String queryType) { this.queryType = queryType; }

    public String getFieldValue() { return fieldValue; }

    public void setFieldValue(String fieldValue) { this.fieldValue = fieldValue; }

    public String getFieldType() { return fieldType; }

    public void setFieldType(String fieldType) { this.fieldType = fieldType; }

    public String getFieldPattern() { return fieldPattern; }

    public void setFieldPattern(String fieldPattern) { this.fieldPattern = fieldPattern; }
    @JsonIgnore
    public Object getFieldValueObject() {
        if(PageQuery.FieldType.DATE.equalsIgnoreCase(fieldType)){
            return DateUtils.parseDate(fieldValue,fieldPattern);
        }else if(PageQuery.FieldType.INT.equalsIgnoreCase(fieldType)){
            return Integer.valueOf(fieldValue);
        }else if(PageQuery.FieldType.LONG.equalsIgnoreCase(fieldType)){
            return Long.valueOf(fieldValue);
        }else if(PageQuery.FieldType.DOUBLE.equalsIgnoreCase(fieldType)){
            return Double.valueOf(fieldValue);
        }else if(PageQuery.FieldType.FLOAT.equalsIgnoreCase(fieldType)){
            return Float.valueOf(fieldValue);
        }else if(PageQuery.FieldType.STRING.equalsIgnoreCase(fieldType)){
            return fieldValue;
        }
        return fieldValue;
    }
    @JsonIgnore
    public List<Object> getFieldValueObjects() {
        List<Object> values = new LinkedList<>();
        for(String value : fieldValue.split(",")) {
            if (PageQuery.FieldType.DATE.equalsIgnoreCase(fieldType)) {
                values.add(DateUtils.parseDate(fieldValue, fieldPattern));
            } else if (PageQuery.FieldType.INT.equalsIgnoreCase(fieldType)) {
                values.add(Integer.valueOf(fieldValue));
            } else if (PageQuery.FieldType.LONG.equalsIgnoreCase(fieldType)) {
                values.add(Long.valueOf(fieldValue));
            } else if (PageQuery.FieldType.DOUBLE.equalsIgnoreCase(fieldType)) {
                values.add(Double.valueOf(fieldValue));
            } else if (PageQuery.FieldType.FLOAT.equalsIgnoreCase(fieldType)) {
                values.add(Float.valueOf(fieldValue));
            } else if (PageQuery.FieldType.STRING.equalsIgnoreCase(fieldType)) {
                values.add(fieldValue);
            }else{
                values.add(fieldValue);
            }
        }
        return values;
    }

    public List<String> getFieldSelect() {
        return fieldSelect;
    }

    public void setFieldSelect(List<String> fieldSelect) {
        this.fieldSelect = fieldSelect;
    }
}
