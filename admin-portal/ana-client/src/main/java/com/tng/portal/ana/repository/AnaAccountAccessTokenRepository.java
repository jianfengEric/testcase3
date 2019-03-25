package com.tng.portal.ana.repository;


import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaAccountAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Zero on 2016/11/14.
 */
@Repository
public interface AnaAccountAccessTokenRepository extends JpaRepository<AnaAccountAccessToken,Long>{

    AnaAccountAccessToken findByToken(String token);
    AnaAccountAccessToken findByAnaAccount(AnaAccount anaAccount);
    
    @Modifying
    @Transactional
    @Query("delete from AnaAccountAccessToken a where a.expriedTime < :expriedTime")
    void deleteByExpriedTime(@Param("expriedTime") Date expriedTime);

    @Modifying
    @Transactional
    @Query("delete from AnaAccountAccessToken where anaAccount.id=?1 ")
    public void deleteByAccount(String accountId);
}
