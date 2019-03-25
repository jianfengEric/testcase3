package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailAccountQuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAccountQuotaRepository extends JpaRepository<EmailAccountQuota, Long> {

    @Modifying
    @Query(value ="update EmailAccountQuota emai set emai.quota_period=:quota_period")
    void updateEmailAccountQuota(@Param("quota_period") String quotaPeriod);
}
