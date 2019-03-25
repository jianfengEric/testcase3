package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAccountRepository extends JpaRepository<EmailAccount, Long>, JpaSpecificationExecutor<EmailAccount> {
//    EmailAccount getEmailAccount(String hostCode,Long hostSizeLimit);

    @Query(value ="update EmailAccountQuota set quota_period='D'")
    void updateEmailAccountQuota(String quotaPeriod);
}
