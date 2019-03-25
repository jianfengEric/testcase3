/*
package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailAccount;
import org.apache.commons.lang.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by Roger on 2017/10/25.
 *//*

public class EmailAccountRepositoryImpl{
    @PersistenceContext
    private EntityManager entityManager;


    public EmailAccount getEmailAccount(String hostCode, Long hostSizeLimit) {
        StringBuilder sb = new StringBuilder();
        sb.append("select a from EmailAccount a where a.status = 'ACT' and a.emailHost.status = 'ACT' ");
        Map<String, Object> parameters = new HashMap<>();
        if(StringUtils.isNotBlank(hostCode)){
            sb.append(" and a.emailHost.code = :hostCode ");
            parameters.put("hostCode", hostCode);
        }
        if(hostSizeLimit!=null && hostSizeLimit.longValue()>0){
            sb.append(" and a.emailHost.emailSizeLimit >= :hostSizeLimit ");
            parameters.put("hostSizeLimit", hostSizeLimit);
        }
        sb.append(" order by a.emailHost.priority asc,(coalesce(a.emailAccountQuota.send_quota,0)-coalesce(a.emailAccountQuota.send_counter,0)) desc,a.priority asc");
        Query query = entityManager.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.setFirstResult(0);
        query.setMaxResults(1);
        List<EmailAccount> emailAccounts = query.getResultList();
        if(emailAccounts == null|| emailAccounts.isEmpty()){
            return null;
        }else{
            return emailAccounts.get(0);
        }
    }


    public void updateEmailAccountQuota(String quotaPeriod){//sonar modify
        String sql = null;
        if("D".equals(quotaPeriod)){
            sql = "update EMAIL_ACCOUNT_QUOTA set SEND_COUNTER=0,UPDATE_DATE=sysdate where (to_char(UPDATE_DATE,'yyyy-mm-dd')<> to_char(sysdate,'yyyy-mm-dd') or UPDATE_DATE is null) and QUOTA_PERIOD=?";
        }else if("M".equals(quotaPeriod)){
            sql = "update EMAIL_ACCOUNT_QUOTA set SEND_COUNTER=0,UPDATE_DATE=sysdate where (to_char(UPDATE_DATE,'mm')<> to_char(sysdate,'mm') or UPDATE_DATE is null) and QUOTA_PERIOD=?";
        }

        Query query = entityManager.createNativeQuery(sql);
        query = query.setParameter(1,quotaPeriod);
        query.executeUpdate();
    }
}

*/
