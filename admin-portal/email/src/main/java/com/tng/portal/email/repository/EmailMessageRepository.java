package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Owen on 2016/12/1.
 */
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long>{

    List<EmailMessage> findByJob(String job);
}
