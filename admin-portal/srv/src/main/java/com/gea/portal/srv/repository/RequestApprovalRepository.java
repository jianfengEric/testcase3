package com.gea.portal.srv.repository;

import com.gea.portal.srv.entity.RequestApproval;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApproval, Long> {

    @Query(value = "select ra FROM RequestApproval ra  where ra.status = :status and ra.currentEnvir=:instance")
    RequestApproval findRequestApproval(@Param("status") RequestApprovalStatus status, @Param("instance") Instance instance);

    @Query(value = "select ra FROM RequestApproval ra  where ra.currentEnvir=:instance order by createDate desc ")
    List<RequestApproval> findByCurrentEnvir(@Param("instance")Instance instance);
}
