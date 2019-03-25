package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Owen on 2017/10/17.
 */
public interface EmailContentRepository extends JpaRepository<EmailContent, Long> {
}
