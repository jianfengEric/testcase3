package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.EwpMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpMaterialRepository extends JpaRepository<EwpMaterial, Long> {

    List<EwpMaterial> findByEwpPoolAdjustmentId(Long poolAdjustmentId);
    List<EwpMaterial> findByEwpPoolDepositCashOutId(Long poolDepositCashOutId);

}
