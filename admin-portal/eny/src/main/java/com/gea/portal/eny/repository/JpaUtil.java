package com.gea.portal.eny.repository;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Transactional(readOnly=true)
@Repository
public class JpaUtil {
	
	public static final String PRE_PROD="PRE_PROD";
	public static final String PROD="PROD";
	
    @Autowired
    @Qualifier("entityManagerFactoryProduction")
    private EntityManager prdEm;
    
    @Autowired
    @Qualifier("entityManagerFactoryPreProduction")
    private EntityManager preEm;
    
    
    private Query createQuery(String sql,String instance){
    	if(instance.equals(PRE_PROD)){
    		return preEm.createQuery(sql);
    	}else{
    		return prdEm.createQuery(sql);
    	}
    }
    
    private Query createNativeQuery(String sql,String instance){
    	if(instance.equals(PRE_PROD)){
    		return preEm.createNativeQuery(sql);
    	}else{
    		return prdEm.createNativeQuery(sql);
    	}
    }


    public <T> List<T> list(String instance,String sql, Map<String, Object> params) {
        Query query = createQuery(sql,instance);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.getResultList();
    }

    /**
     *  Get paging data 
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Page<T> page(String instance,String sql, Map<String, Object> params, Pageable pageable) {
        Query query = createQuery(sql,instance);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        /**
         *  Generating total number sql
         */
        TypedQuery<Long> cQuery = (TypedQuery<Long>) createQuery(QueryUtils.createCountQueryFor(sql),instance);
        if (params != null) {
            for (String key : params.keySet()) {
                cQuery.setParameter(key, params.get(key));
            }
        }
        return new PageImpl<>(query.getResultList(), pageable, executeCountQuery(cQuery));
    }

    /**
     *  Get paging data  By Native Sql
     * @return
     */
    public Page<Map<String,Object>> pageBySql(String instance,String sql, Pageable pageable, Object... params) {
        if (params != null) {
            for (int i=0;i<params.length;i++) {
                sql = sql.replaceFirst("\\? ", "?"+i+" ");
            }
        }
        Query query = createNativeQuery(sql,instance);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (params != null) {
            for (int i=0;i<params.length;i++) {
                query.setParameter(i, params[i]);
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Map<String,Object>> resList = query.getResultList();
        // Generating total number sql
        String cSql = "select count(1) from (" + sql + ") temp";
        Query cQuery = createNativeQuery(cSql,instance);
        if (params != null) {
            for (int i=0;i<params.length;i++) {
                cQuery.setParameter(i, params[i]);
            }
        }
        Long resCount = ((BigInteger)cQuery.getResultList().get(0)).longValue();
        return new PageImpl<>(resList, pageable, resCount);
    }

    /**
     * Executes a count query and transparently sums up all values returned.
     * @return
     */
    private static Long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        Long total = 0L;
        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }
}