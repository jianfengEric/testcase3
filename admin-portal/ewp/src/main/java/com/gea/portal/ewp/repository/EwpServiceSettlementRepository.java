package com.gea.portal.ewp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwpServiceSettlement;

/**
 * 
 */
@Repository
public interface EwpServiceSettlementRepository extends JpaRepository<EwpServiceSettlement, Long>{
	
	/**
	 * find By ParticipantId
	 */
	@Query("from EwpServiceSettlement where participantId = ?1 ")
	public List<EwpServiceSettlement> findByParticipantId(Long participantId);
	
	/**
	 * find By EwpServiceId
	 */
	@Query("from EwpServiceSettlement where ewpServiceId = ?1 ")
	public List<EwpServiceSettlement> findByEwpServiceId(Long ewpServiceId);
	
}
