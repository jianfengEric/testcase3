package com.gea.portal.ewp.repository;

import java.util.List;

import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.gea.portal.ewp.entity.EwpCompanyForm;
import com.tng.portal.common.enumeration.ParticipantStatus;

/**
 * Created by dong on 2018/8/27.
 */
@Repository
public interface EwpCompanyFormRepository extends JpaRepository<EwpCompanyForm, Long>{

	@Query("from EwpCompanyForm d where d.ewalletParticipant =:participantId and d.status = :status Order By d.createDate Desc")
	List<EwpCompanyForm> findEwpCompanyForm(@Param("status")ParticipantStatus status,@Param("participantId")EwalletParticipant participantId);

	List<EwpCompanyForm> findByStatus(ParticipantStatus status);

	List<EwpCompanyForm> findByStatusAndCurrentEnvir(ParticipantStatus status, Instance instance);
}
