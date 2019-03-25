package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaLoginSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Zero on 2016/11/4.
 */
@Repository
public interface AnaLoginSessionRepository extends JpaRepository<AnaLoginSession,Long>{
    List<AnaLoginSession> findByAccount(String account);
    long countByAccount(String account);
    long countByAccountAndSessionDateTimeAfter(String account, Date sessionDateTime);
}
