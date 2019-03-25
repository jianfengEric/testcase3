package com.gea.portal.eny.page;

import java.io.Serializable;

/**
 * Created by Roger on 2017/10/25.
 */
// SortQuery
public class SortQuery implements Serializable {
    private String fieldName;
    private String sortName; //asc&desc

    public String getFieldName() { return fieldName; }

    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getSortName() { return sortName; }

    public void setSortName(String sortName) { this.sortName = sortName; }
}