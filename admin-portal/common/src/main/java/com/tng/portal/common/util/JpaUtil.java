package com.tng.portal.common.util;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional(readOnly=true)
@Repository
public class JpaUtil {
    @Autowired
    private EntityManager em;
    
    public void refresh(Object obj){
    	em.refresh(obj);
    }
    
    public <T> List<T> list(String sql, Map<String, Object> params) {
        Query query = em.createQuery(sql);
        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }

    /**
     *  Get paging data 
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Page<T> page(String sql, Map<String, Object> params, Pageable pageable) {
        Query query = em.createQuery(sql);
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
        TypedQuery<Long> cQuery = (TypedQuery<Long>) em.createQuery(QueryUtils.createCountQueryFor(sql));
        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                cQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return new PageImpl<>(query.getResultList(), pageable, executeCountQuery(cQuery));
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
    
    
    /** 
     *  Get paging data  By Native Sql
     * @return
     */
    public Page<Map<String,Object>> pageBySql(String sql, Pageable pageable, Object... params) {
    	if (params != null) {
            for (int i=0;i<params.length;i++) {
            	sql = sql.replaceFirst("\\? ", "?"+i+" ");
            }
        }
        Query query = em.createNativeQuery(sql);
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
        Query cQuery = em.createNativeQuery(cSql);
        if (params != null) {
            for (int i=0;i<params.length;i++) {
            	cQuery.setParameter(i, params[i]);
            }
        }
        Long resCount = ((BigInteger)cQuery.getResultList().get(0)).longValue();
        return new PageImpl<>(resList, pageable, resCount);
    }
    
    public static String genLikeStr(String str){
    	return "%"+str.replaceAll("%", "\\\\%")+"%";
    }
    
    
}