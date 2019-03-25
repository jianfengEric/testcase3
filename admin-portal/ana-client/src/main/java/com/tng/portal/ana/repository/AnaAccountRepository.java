package com.tng.portal.ana.repository;


import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/4.
 * modified by Kaster 20170214  Implement interface JpaSpecificationExecutor<AnaAccount>
 */
@Repository
public interface AnaAccountRepository extends JpaRepository<AnaAccount,String>,JpaSpecificationExecutor<AnaAccount>{
    AnaAccount findById(String id);
    AnaAccount findByAccountAndStatusIn(String account, List<String> status);
    
    @Query(" from AnaAccount where account=?1 order by createdTime desc")
    List<AnaAccount> findByAccount(String account);
    AnaAccount findByAccountAndStatusNot(String account, String status);
    AnaAccount findByAccountIgnoreCaseAndStatusNot(String account, String status);
    List<AnaAccount> findByAccountLikeAndStatusNot(String account, String status);
    List<AnaAccount> findByAnaRoles(AnaRole anaRole);
    List<AnaAccount> findDistinctByAnaRolesIn(List<AnaRole> rList);
    List<AnaAccount> findByExternalGroupId(String externalGroupId);
    @Modifying
    @Query("update AnaAccount a set a.status = 'NACT'  where a.externalGroupId = ?2")
    int updateByMid(String mid);

}
