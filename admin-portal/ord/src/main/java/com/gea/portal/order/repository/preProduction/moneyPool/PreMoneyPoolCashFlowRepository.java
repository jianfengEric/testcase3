package com.gea.portal.order.repository.preProduction.moneyPool;

import com.gea.portal.order.entity.preProduction.moneyPool.PreMoneyPoolCashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreMoneyPoolCashFlowRepository extends JpaRepository<PreMoneyPoolCashFlow, Long>,JpaSpecificationExecutor<PreMoneyPoolCashFlow> {

    @Query(value = "select moneyPool.refId from PreMoneyPoolCashFlow where geaRefId=:geaRefId")
    String getMoneyPoolRefIdByGeaRefId(@Param("geaRefId") String geaRefId);

}
