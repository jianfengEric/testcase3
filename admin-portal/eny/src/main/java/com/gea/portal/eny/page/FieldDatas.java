package com.gea.portal.eny.page;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roger on 2017/10/26.
 */
public class FieldDatas implements Serializable{

    private List<FieldQuery> fieldQueries;  // Fields to query 
    @JsonIgnore
    private List<FieldQuery> mustQueries;   // Fields to be filled out ( Initialization in background )
    @JsonIgnore
    private Map<String,String> fieldNames;  // Mapping of front-end fields and back-end fields ( Front ^ after )


    public List<FieldQuery> getMustQueries() { return mustQueries; }

    public void setMustQueries(List<FieldQuery> mustQueries) { this.mustQueries = mustQueries; }

    public List<FieldQuery> getFieldQueries() {
        return fieldQueries;
    }

    public void setFieldQueries(List<FieldQuery> fieldQueries) {
        this.fieldQueries = fieldQueries;
    }

    public Map<String, String> getFieldNames() { return fieldNames; }

    public void setFieldNames(Map<String, String> fieldNames) { this.fieldNames = fieldNames; }
    @JsonIgnore
    public static FieldDatas getInstance(String functionCode,Map<String,List<String>> value){
        FieldDatas fieldDatas = new FieldDatas();
        Map<String,List<String>> values = value;
        if(values == null){
            values = new HashMap<>();
        }
        if("USER_ACCOUNT".equalsIgnoreCase(functionCode)){
            String status = "status";
            List<FieldQuery> mustQueries = new ArrayList<>();
            mustQueries.add(new FieldQuery(
                    status,
                    "notin",
                    "DEL",
                    "string",
                    "",values.get(status)));
            fieldDatas.setMustQueries(mustQueries);
            List<FieldQuery> fieldQueries = new ArrayList<>();
            fieldQueries.add(new FieldQuery(
                    status,
                    "",
                    "",
                    "string",
                    "",values.get("account")));

            fieldQueries.add(new FieldQuery(
                    "applications",
                    "",
                    "",
                    "list",
                    "",values.get("applications")));
            fieldQueries.add(new FieldQuery(
                    "roles",
                    "",
                    "",
                    "list",
                    "",values.get("roles")));
            fieldQueries.add(new FieldQuery(
                    status,
                    "",
                    "",
                    "string",
                    "",values.get(status)));

            fieldQueries.add(new FieldQuery(
                    "createdTime",
                    "",
                    "",
                    "date",
                    "yyyy-MM-dd",values.get("createdTime")));
            fieldDatas.setFieldQueries(fieldQueries);
            Map<String,String> fieldNames = new HashMap<>();
            fieldNames.put("account","account");
            fieldNames.put("applications","app.anaApplication.code");
            fieldNames.put("app.anaApplication.code"," select count(app) from a.anaAccountApplications app ");
            fieldNames.put("roles","role.name");
            fieldNames.put("role.name"," select count(role) from a.anaRoles role ");
            fieldNames.put("status",status);
            fieldNames.put("createdTime","createdTime");
            fieldDatas.setFieldNames(fieldNames);
        }
        return fieldDatas;
    }
}
