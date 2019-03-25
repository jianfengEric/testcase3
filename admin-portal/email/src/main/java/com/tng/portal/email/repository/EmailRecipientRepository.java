package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Owen on 2017/10/17.
 */
public interface EmailRecipientRepository extends JpaRepository<EmailRecipient, Long> {

    @Query(" from EmailRecipient a where a.status in :status and a.resendCount <:resendCount order by a.sentTime asc")
    Page<EmailRecipient> find( @Param("status")List<String> status,@Param("resendCount")int resendCount,Pageable pageable);
}
