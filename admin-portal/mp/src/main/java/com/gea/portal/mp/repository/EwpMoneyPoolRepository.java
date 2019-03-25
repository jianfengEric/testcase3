package com.gea.portal.mp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gea.portal.mp.entity.EwpMoneyPool;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.RequestApprovalStatus;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpMoneyPoolRepository extends JpaRepository<EwpMoneyPool, Long>, JpaSpecificationExecutor<EwpMoneyPool> {

    EwpMoneyPool findTopByOrderByCreateDateDesc();
    
    @Query(" from EwpMoneyPool where geaMoneyPoolRefId=?1 and currentEnvir=?2 ")
    public EwpMoneyPool findByGeaMoneyPoolRefId(String geaMoneyPoolRefId, Instance currentEnvir);
    
    @Query("from EwpMoneyPool where geaParticipantRefId =?1 and currentEnvir=?2 ")
    public List<EwpMoneyPool> findByGeaParticipantRefId(String geaParticipantRefId, Instance instance);
    
    
    List<EwpMoneyPool> findByStatusAndCurrentEnvir(MoneyPoolStatus status, Instance instance);

    @Query(value = "select e from EwpMoneyPool e where e.currentEnvir=:instance and (e.id=:moneyPoolId or e.geaParticipantRefId in (:participantId) or e.baseCurrency =:currency or e.status = :moneyPoolStatus)")
    Page<EwpMoneyPool> listMoneyPool(@Param("moneyPoolId")long moneyPoolId, @Param("participantId") ArrayList<String> participantId, @Param("currency") String currency,  @Param("moneyPoolStatus") String moneyPoolStatus, @Param("instance") Instance instance, Pageable parameter);

    @Query(value = "select e from EwpMoneyPool e where  e.status=:status and e.currentEnvir=:instance")
    Page<EwpMoneyPool> listMoneyPoolForProcessingStatus(@Param("status") RequestApprovalStatus status, @Param("instance") Instance instance, Pageable parameter);

    @Query(value = "select e from EwpMoneyPool e where e.currentEnvir=:instance and (e.id=:moneyPoolId or e.baseCurrency =:currency or e.createBy = :portalUserId)")
    Page<EwpMoneyPool> listMoneyPoolDetailPage(@Param("moneyPoolId")long moneyPoolId, @Param("currency") String currency,  @Param("instance") Instance instance, Pageable parameter);

    @Query(value = "select e from EwpMoneyPool e where  e.status in :status and e.currentEnvir=:instance and e.geaParticipantRefId=:geaParticipantRefId")
    List<EwpMoneyPool> getAllMoneyPool( @Param("geaParticipantRefId") String geaParticipantRefId, @Param("status") List<MoneyPoolStatus> status, @Param("instance") Instance instance);

	@Query(" select max(geaMoneyPoolRefId) from EwpMoneyPool where geaMoneyPoolRefId like ?1 and currentEnvir=?2 ")
	public String findMaxGeaRefId(String prefix,Instance currentEnvir);
	
	@Query(value = "select geaParticipantRefId as geaParticipantRefId,count(id) as total from EwpMoneyPool where  status in ?2 and currentEnvir=?3 and geaParticipantRefId in ?1 group by geaParticipantRefId")
	public List<Map<String,Object>> findMoneyPoolCount(List<String> geaParticipantRefId, List<MoneyPoolStatus> status,Instance instance);
	
}
