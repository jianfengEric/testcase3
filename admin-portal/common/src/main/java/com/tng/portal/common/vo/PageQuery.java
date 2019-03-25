package com.tng.portal.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Roger on 2017/10/24.
 */
public class PageQuery<T> implements Serializable{

    private int pageNo;

    private int pageSize;

    private List<FieldQuery> fieldQueries;
    @JsonIgnore
    private List<FieldQuery> mustQueries;

    private List<SortQuery> sortQueries;
    @JsonIgnore
    private Map<String,String> fieldNames;

    private String criteria;    //and&or

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<FieldQuery> getFieldQueries() {
        return fieldQueries;
    }

    public void setFieldQueries(List<FieldQuery> fieldQueries) {
        this.fieldQueries = fieldQueries;
    }

    public List<SortQuery> getSortQueries() {
        return sortQueries;
    }

    public void setSortQueries(List<SortQuery> sortQueries) {
        this.sortQueries = sortQueries;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public Map<String, String> getFieldNames() { return fieldNames; }

    public void setFieldNames(Map<String, String> fieldNames) { this.fieldNames = fieldNames; }

    public Pageable getPageable(){
        if(null==sortQueries||sortQueries.isEmpty()){
            return new PageRequest(pageNo - 1, pageSize);
        }else{
            List<Sort.Order> orders = new ArrayList<>();
            for(SortQuery sortQuery : sortQueries){
                if(Sort.Direction.ASC.name().equalsIgnoreCase(sortQuery.getSortName())){
                    Sort.Order order = new Sort.Order(Sort.Direction.ASC, sortQuery.getFieldName());
                    orders.add(order);
                }else if(Sort.Direction.DESC.name().equalsIgnoreCase(sortQuery.getSortName())){
                    Sort.Order order = new Sort.Order(Sort.Direction.DESC, sortQuery.getFieldName());
                    orders.add(order);
                }
            }
            Sort sort = new Sort(orders);
            return new PageRequest(pageNo - 1, pageSize,sort);
        }
    }

    class Condition{
        static final String CONTAINS = "contains";
        static final String EQUAL = "equal";
        static final String ISBANK = "isbank";
        static final String ISNOTBANK = "isnotbank";
        static final String IN = "in";
        static final String NOTIN = "notin";
        static final String STARTSWITH = "startswith";
        static final String STARTSNOTWITH = "startsnotwith";
        static final String ENDSWITH = "endswith";
        static final String ENDSNOTWITH = "endsnotwith";
    }
    class Criteria{
        static final String OR = "or";
        static final String AND = "and";
    }

    class FieldType{
        static final String INT = "INT";
        static final String LONG = "LONG";
        static final String STRING = "STRING";
        static final String DATE = "DATE";
        static final String DOUBLE = "DOUBLE";
        static final String FLOAT = "FLOAT";
        static final String LIST = "LIST";
    }
    @JsonIgnore
    private String queryHql;
    @JsonIgnore
    private Map<String, Object> parameters;

    public void getResult(Class<T> clazz){
        StringBuffer hql = new StringBuffer("from ").append(clazz.getSimpleName()).append(" a where 1=1");
        parameters = new HashMap<>();
        if(getMustQueries()!=null && !getMustQueries().isEmpty()){
            hql.append(" and ( ");
            hql.append(builderHQL(getMustQueries(),Criteria.AND));
            hql.append(" ) ");
        }
        if(getFieldQueries()!=null && !getFieldQueries().isEmpty()){
            hql.append(" and ( ");
            hql.append(builderHQL(getFieldQueries(),criteria));
            hql.append(" ) ");
        }
        if(getSortQueries()!=null && !getSortQueries().isEmpty()){
            hql.append(" order by ");
            int i = 0;
            for(SortQuery sortQuery : getSortQueries()){
                if(i == 0){
                    hql.append(" a.").append(sortQuery.getFieldName()).append(" ").append(sortQuery.getSortName());
                }else{
                    hql.append(",a.").append(sortQuery.getFieldName()).append(" ").append(sortQuery.getSortName());
                }
                i++;
            }
        }
        setQueryHql(hql.toString());
    }

    public List<FieldQuery> getMustQueries() { return mustQueries; }

    public void setMustQueries(List<FieldQuery> mustQueries) { this.mustQueries = mustQueries; }

    public String getQueryHql() { return queryHql; }

    public void setQueryHql(String queryHql) { this.queryHql = queryHql; }

    public Map<String, Object> getParameters() { return parameters; }

    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }


    private String builderHQL(List<FieldQuery> fieldQueries,String criteria){
        StringBuffer buffer = new StringBuffer();
        if(Criteria.AND.equalsIgnoreCase(criteria)){
            buffer.append(" 1=1 ");
        }else if(Criteria.OR.equalsIgnoreCase(criteria)){
            buffer.append(" 1=0 ");
        }else{
            buffer.append(" 1=1 ");
        }

        int len = 0;
        for(FieldQuery fieldQuery : fieldQueries){
            buffer.append(" ").append(criteria);
            len ++;
            if(Condition.CONTAINS.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 0<(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,"%"+fieldQuery.getFieldValueObject()+"%");
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,"%"+fieldQuery.getFieldValueObject()+"%");
                }
            }if(Condition.EQUAL.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 0<(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" = :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,fieldQuery.getFieldValueObject());
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" = :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,fieldQuery.getFieldValueObject());
                }

            }else if(Condition.STARTSWITH.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 0<(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,fieldQuery.getFieldValueObject()+"%");
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,fieldQuery.getFieldValueObject()+"%");
                }
            }else if(Condition.ENDSWITH.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 0<(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,"%"+fieldQuery.getFieldValueObject());
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,"%"+fieldQuery.getFieldValueObject());
                }
            }else if(Condition.STARTSNOTWITH.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 1>(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" not like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,fieldQuery.getFieldValueObject()+"%");
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" not like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,fieldQuery.getFieldValueObject()+"%");
                }
            }else if(Condition.ENDSNOTWITH.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 1>(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" not like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,"%"+fieldQuery.getFieldValueObject());
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" not like :").append(fieldQuery.getFieldName()+len);
                    parameters.put(fieldQuery.getFieldName()+len,"%"+fieldQuery.getFieldValueObject());
                }

            }else if(Condition.IN.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 1>(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" in (");
                    int k = 0;
                    for(Object object :fieldQuery.getFieldValueObjects()){
                        len ++ ;
                        if(k == 0){
                            buffer.append(" :").append(fieldQuery.getFieldName()+len);
                        }else{
                            buffer.append(",:").append(fieldQuery.getFieldName()+len);
                        }
                        parameters.put(fieldQuery.getFieldName()+len,object);
                        k++;
                    }
                    buffer.append(" ) ");
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" in (");
                    int k = 0;
                    for(Object object :fieldQuery.getFieldValueObjects()){
                        len ++ ;
                        if(k == 0){
                            buffer.append(" :").append(fieldQuery.getFieldName()+len);
                        }else{
                            buffer.append(",:").append(fieldQuery.getFieldName()+len);
                        }
                        parameters.put(fieldQuery.getFieldName()+len,object);
                        k++;
                    }
                    buffer.append(" ) ");
                }

            }else if(Condition.NOTIN.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 1>(").append(fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))).append(" where "+fieldNames.get(fieldQuery.getFieldName())+" not in (");
                    int k = 0;
                    for(Object object :fieldQuery.getFieldValueObjects()){
                        len ++ ;
                        if(k == 0){
                            buffer.append(" :").append(fieldQuery.getFieldName()+len);
                        }else{
                            buffer.append(",:").append(fieldQuery.getFieldName()+len);
                        }
                        parameters.put(fieldQuery.getFieldName()+len,object);
                        k++;
                    }
                    buffer.append(" ) ");
                    buffer.append(" ) "); //end
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" not in (");
                    int k = 0;
                    for(Object object :fieldQuery.getFieldValueObjects()){
                        len ++;
                        if(k == 0){
                            buffer.append(" :").append(fieldQuery.getFieldName()+len);
                        }else{
                            buffer.append(",:").append(fieldQuery.getFieldName()+len);
                        }
                        parameters.put(fieldQuery.getFieldName()+len,object);
                        k++;
                    }
                    buffer.append(" ) ");
                }
            }else if(Condition.ISBANK.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 1> ("+fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))+")  ");
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" is null");
                }
            }else if(Condition.ISNOTBANK.equalsIgnoreCase(fieldQuery.getQueryType())){
                if(FieldType.LIST.equalsIgnoreCase(fieldQuery.getFieldType())){
                    buffer.append(" 0 < ("+fieldNames.get(fieldNames.get(fieldQuery.getFieldName()))+")  ");
                }else{
                    buffer.append(" a.").append(fieldNames.get(fieldQuery.getFieldName())).append(" is not null ");
                }

            }

        }
        return buffer.toString();
    }
}
