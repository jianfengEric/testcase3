package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailAttachment;
import com.tng.portal.email.entity.EmailContent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Owen on 2017/10/17.
 */
public interface EmailAttachmentRepository extends JpaRepository<EmailAttachment, Long> {
	List<EmailAttachment> findByEmailContent(EmailContent emailContent);
}
