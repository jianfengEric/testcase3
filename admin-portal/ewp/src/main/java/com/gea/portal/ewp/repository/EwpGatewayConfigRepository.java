package com.gea.portal.ewp.repository;

import java.util.List;

import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.gea.portal.ewp.entity.EwpGatewayConfig;
import com.tng.portal.common.enumeration.ParticipantStatus;

/**
 * Created by Owen on 2018/8/27.
 */
@Repository
public interface EwpGatewayConfigRepository extends JpaRepository<EwpGatewayConfig, Long>{

	@Query("from EwpGatewayConfig d where d.ewalletParticipant =:participantId and d.status = :status Order By d.createDate Desc")
	List<EwpGatewayConfig> findEwpGatewayConfig(@Param("status")ParticipantStatus status,@Param("participantId")EwalletParticipant participantId);

	List<EwpGatewayConfig> findByStatus(ParticipantStatus status);

	List<EwpGatewayConfig> findByStatusAndCurrentEnvir(ParticipantStatus status, Instance instance);

	@Query("from EwpGatewayConfig where serverApiKey =?1 and status='ACTIVE'")
	public EwpGatewayConfig findByServerApiKey(String serverApiKey);
	
	@Query("from EwpGatewayConfig where serverSecretKey =?1 and status='ACTIVE'")
	public EwpGatewayConfig findByServerSecretKey(String serverSecretKey);
	
}
