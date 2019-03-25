package com.gea.portal.tre.repository;

import com.gea.portal.tre.entity.RequestApproval;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2018/8/31.
 */
@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApproval, Long> {

    Page<RequestApproval> findByStatus(RequestApprovalStatus status, Pageable pageable);
    List<RequestApproval> findByStatusAndCurrentEnvir(RequestApprovalStatus status, Instance instance);

    @Query(" from RequestApproval r where r.status='APPROVED' and r.currentEnvir=:instance order by r.updateDate desc")
    List<RequestApproval> findAllApprovalRecord(@Param("instance")Instance instance);

}
