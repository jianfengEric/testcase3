package com.gea.portal.ewp.repository;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.gea.portal.ewp.entity.EwpServiceMoneyPool;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2018/8/24.
 */
@Repository
public interface EwpServiceMoneyPoolRepository extends JpaRepository<EwpServiceMoneyPool, Long>{

    @Query("from EwpServiceMoneyPool d where d.ewalletParticipant =:participant and d.status = :status Order By d.createDate Desc")
    List<EwpServiceMoneyPool> findByEwalletParticipant(@Param("status")ParticipantStatus status,@Param("participant")EwalletParticipant participant);

    List<EwpServiceMoneyPool> findByStatus(ParticipantStatus status);

    List<EwpServiceMoneyPool> findByStatusAndCurrentEnvir(ParticipantStatus status, Instance instance);
    
    @Query("from EwpServiceMoneyPool where ewpService.id = ?1")
    public List<EwpServiceMoneyPool> findByEwpServiceId(Long ewpServiceId);
    
    @Query("from EwpServiceMoneyPool where geaMoneyPoolRefId = ?1 and currentEnvir=?2")
    public List<EwpServiceMoneyPool> findByGeaMoneypoolRefId(String geaMoneypoolRefId, Instance instance);

}
