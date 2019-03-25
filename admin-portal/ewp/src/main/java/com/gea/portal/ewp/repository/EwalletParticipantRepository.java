package com.gea.portal.ewp.repository;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2018/8/24.
 */
@Repository
public interface EwalletParticipantRepository extends JpaRepository<EwalletParticipant, Long>, JpaSpecificationExecutor<EwalletParticipant> {

	@Query(value = "select e FROM EwalletParticipant e left join e.ewpCompanyForms com left join e.ewpServices s left join s.ewpMoneyPool m where s.status='ACTIVE' and com.currentEnvir=:instance and com.status='ACTIVE'")
	Page<EwalletParticipant> findAll(@Param("instance") Instance instance, Pageable parameter);// OR e.preStatus LIKE :keyWords

	@Query(value = "select e FROM EwalletParticipant e left join e.ewpCompanyForms com left join e.ewpServices s left join s.ewpMoneyPool m where s.status='ACTIVE' and com.currentEnvir=:instance and com.status='ACTIVE' and (com.fullCompanyName LIKE :participantName or e.id LIKE :participantId OR s.status = :status)")
	Page<EwalletParticipant> searchPreEwalletParticipant(@Param("status")ParticipantStatus status, @Param("participantId") long participantId, @Param("participantName") String participantName, @Param("instance") Instance instance, Pageable parameter);// OR e.preStatus LIKE :keyWords

	EwalletParticipant findByGeaRefIdAndPreStatus(String refId, ParticipantStatus status);
	EwalletParticipant findByGeaRefIdAndProdStatus(String refId, ParticipantStatus status);
	EwalletParticipant findByGeaRefId(String refId);

	@Query(value = "select e FROM EwalletParticipant e left join e.ewpCompanyForms com where com.currentEnvir=:instance and (com.fullCompanyName LIKE CONCAT('%',:participantName,'%')  or e.geaRefId =:geaParticipantRefId) group by e.id ")
	List<EwalletParticipant> findAllByIdOrEwpCompanyForms(@Param("geaParticipantRefId") String geaParticipantRefId, @Param("participantName") String participantName, @Param("instance") Instance instance);
	@Query(value = "select e FROM EwalletParticipant e left join e.ewpCompanyForms com where com.currentEnvir=:instance group by e.id ")
	List<EwalletParticipant> findAllByInstance(@Param("instance") Instance instance);

	@Query(" select max(geaRefId) from EwalletParticipant where geaRefId like ?1 ")
	public String findMaxGeaRefId(String area);
	
	@Query(" select max(geaPrePid) from EwalletParticipant ")
	public Long findMaxGeaPrePid();
	
	@Query(" select max(geaPrdPid) from EwalletParticipant ")
	public Long findMaxGeaPrdPid();
	
	@Query(" from EwalletParticipant where geaRefId in ?1 ")
	public List<EwalletParticipant> findByGeaRefId(List<String> geaRefId);
}

