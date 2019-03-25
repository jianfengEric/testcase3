package com.gea.portal.order.repository.production.moneyPool;

import com.gea.portal.order.entity.production.moneyPool.ProMoneyPoolCashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProMoneyPoolCashFlowRepository extends JpaRepository<ProMoneyPoolCashFlow, Long>,JpaSpecificationExecutor<ProMoneyPoolCashFlow> {

    @Query(value = "select moneyPool.refId from ProMoneyPoolCashFlow where geaRefId=:geaRefId")
    String getMoneyPoolRefIdByGeaRefId(@Param("geaRefId") String geaRefId);

}
