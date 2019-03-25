package com.gea.portal.ewp.repository;

import java.util.List;

import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.gea.portal.ewp.entity.EwpService;
import com.tng.portal.common.enumeration.ParticipantStatus;

/**
 * Created by Owen on 2018/8/27.
 */
@Repository
public interface EwpServiceRepository extends JpaRepository<EwpService, Long>{
	@Query("from EwpService d where d.ewalletParticipant =:participantId and d.status = :status Order By d.createDate Desc")
	List<EwpService> findByEwalletParticipant(@Param("status")ParticipantStatus status,@Param("participantId")EwalletParticipant participantId);

	List<EwpService> findByStatus(ParticipantStatus status);

	List<EwpService> findByStatusAndCurrentEnvir(ParticipantStatus status, Instance instance);
	
	@Query("from EwpService where id in ?1")
	public List<EwpService> findByIds(List<Long> ids);
	
}
