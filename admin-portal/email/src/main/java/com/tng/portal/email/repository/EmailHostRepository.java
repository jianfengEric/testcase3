package com.tng.portal.email.repository;

import com.tng.portal.email.entity.EmailHost;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailHostRepository extends JpaRepository<EmailHost, String>, JpaSpecificationExecutor<EmailHost> {
    List<EmailHost> findByStatus(String status, Sort sort);
    List<EmailHost> findByStatusAndCode(String status,String code, Sort sort);
}
