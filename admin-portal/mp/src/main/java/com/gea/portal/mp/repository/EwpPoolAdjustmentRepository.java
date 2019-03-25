package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.EwpMoneyPool;
import com.gea.portal.mp.entity.EwpPoolAdjustment;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpPoolAdjustmentRepository extends JpaRepository<EwpPoolAdjustment, Long> {

    List<EwpPoolAdjustment> findByEwpMoneyPoolAndCurrentEnvir(EwpMoneyPool ewpMoneyPool, Instance instance);
    List<EwpPoolAdjustment> findByEwpMoneyPool(EwpMoneyPool ewpMoneyPool);
    List<EwpPoolAdjustment> findByStatusAndCurrentEnvir(MoneyPoolStatus status, Instance instance);
    List<EwpPoolAdjustment> findByStatusAndCurrentEnvirAndEwpMoneyPool(MoneyPoolStatus status, Instance instance, EwpMoneyPool ewpMoneyPool);
    EwpPoolAdjustment findByGeaTxRefNoAndCurrentEnvir(String geaTxRefNo, Instance instance);
}
