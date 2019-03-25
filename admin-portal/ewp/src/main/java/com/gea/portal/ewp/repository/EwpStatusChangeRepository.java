package com.gea.portal.ewp.repository;

import com.gea.portal.ewp.entity.EwpStatusChange;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpStatusChangeRepository extends JpaRepository<EwpStatusChange, Long> {

    List<EwpStatusChange> findByStatusAndCurrentEnvir(ParticipantStatus status, Instance instance);

}
