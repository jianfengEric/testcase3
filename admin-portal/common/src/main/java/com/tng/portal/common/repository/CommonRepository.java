package com.tng.portal.common.repository;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.PageQuery;

/**
 * Created by Roger on 2017/10/25.
 */
@Repository
public class CommonRepository <T> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @PersistenceContext
    private EntityManager entityManager;

    public PageDatas<T> query(PageQuery pageQuery, Class<T> clazz){
        PageDatas<T> pageDatas = new PageDatas<>(pageQuery.getPageNo() ,pageQuery.getPageSize());
        pageQuery.getResult(clazz);
        logger.info(pageQuery.getQueryHql());
        Query query = entityManager.createQuery(pageQuery.getQueryHql());
        Map<String, Object> parameters = pageQuery.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setFirstResult((pageQuery.getPageNo() - 1) * pageQuery.getPageSize());
        query.setMaxResults(pageQuery.getPageSize());
        pageDatas.setList(query.getResultList());
        query = entityManager.createQuery("select count(*) " + pageQuery.getQueryHql());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        pageDatas.setTotal((Long) query.getSingleResult());
        return pageDatas;
    }
    //FTV100A
}
