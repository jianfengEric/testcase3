package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Owen on 2016/11/18.
 */
@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long>{

    EmailTemplate findTopByJob(String job);
}
