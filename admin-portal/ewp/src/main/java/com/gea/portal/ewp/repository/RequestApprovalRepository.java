package com.gea.portal.ewp.repository;

import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.RequestApproval;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/29.
 */
@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApproval, Long>, JpaSpecificationExecutor<RequestApproval> {
	@Query(value = "select ra FROM RequestApproval ra where ra.status = :status and ra.currentEnvir=:instance")
	List<RequestApproval> findRequestApproval(@Param("status") RequestApprovalStatus status, @Param("instance") Instance instance);

	@Query(value = "FROM RequestApproval where ewalletParticipant.id=?1 order by createDate desc")
	public List<RequestApproval> findByParticipantId(Long participantId);
}
