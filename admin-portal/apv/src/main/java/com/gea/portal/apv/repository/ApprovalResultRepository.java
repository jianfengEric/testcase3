package com.gea.portal.apv.repository;

import com.gea.portal.apv.entity.ApprovalResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by Owen on 2018/9/5.
 */
@Repository
public interface ApprovalResultRepository extends JpaRepository<ApprovalResult, Long>, JpaSpecificationExecutor<ApprovalResult>{
}
