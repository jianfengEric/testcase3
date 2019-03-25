package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.RequestApproval;
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
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApproval, Long> {

    Page<RequestApproval> findByStatus(RequestApprovalStatus status, Pageable pageable);
    @Query(value = "select ra FROM RequestApproval ra  where ra.status = :status and ra.currentEnvir=:instance")
    List<RequestApproval> findRequestApproval(@Param("status") RequestApprovalStatus status, @Param("instance") Instance instance);
    
    @Query(value = "from RequestApproval where geaParticipantRefId = :geaParticipantRefId")
    List<RequestApproval> findByGeaParticipantRefId(@Param("geaParticipantRefId")String geaParticipantRefId);
}
