package com.gea.portal.ewp.repository;

import com.gea.portal.ewp.entity.EwpMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/30.
 */
@Repository
public interface EwpMaterialRepository extends JpaRepository<EwpMaterial, Long> {

    List<EwpMaterial> findByEwpFormId(Long ewpFormId);
    List<EwpMaterial> findByEwpOwnerId(Long ewpOwnerId);
    List<EwpMaterial> findByEwpKeyPersonId(Long ewpKeyPersonId);
    List<EwpMaterial> findByEwpDisputeContactId(Long ewpDisputeContactId);

}
