package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.EwpMoneyPool;
import com.gea.portal.mp.entity.EwpPoolDepositCashOut;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpPoolDepositCashOutRepository extends JpaRepository<EwpPoolDepositCashOut, Long> {

    List<EwpPoolDepositCashOut> findByEwpMoneyPoolAndCurrentEnvir(EwpMoneyPool ewpMoneyPool, Instance instance);
    List<EwpPoolDepositCashOut> findByEwpMoneyPool(EwpMoneyPool ewpMoneyPool);
    List<EwpPoolDepositCashOut> findByStatusAndCurrentEnvir(MoneyPoolStatus status, Instance instance);
    List<EwpPoolDepositCashOut> findByStatusAndCurrentEnvirAndEwpMoneyPool(MoneyPoolStatus status, Instance instance, EwpMoneyPool ewpMoneyPool);
    EwpPoolDepositCashOut findByGeaTxRefNoAndCurrentEnvir(String geaTxRefNo, Instance instance);

}
