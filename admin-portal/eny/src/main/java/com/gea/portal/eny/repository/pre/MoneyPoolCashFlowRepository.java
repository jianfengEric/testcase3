package com.gea.portal.eny.repository.pre;

import com.gea.portal.eny.entity.pre.MoneyPoolCashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dong on 2018/9/10.
 */
@Repository
public interface MoneyPoolCashFlowRepository extends  JpaRepository<MoneyPoolCashFlow, Integer> {

    @Query(value = "select m from MoneyPoolCashFlow m where m.moneyPoolId=:moneyPoolId and m.refNo=:geaMoneyTransferId and m.createDatetime =:transactionDateTime and m.serviceId = :serviceType and m.type = :transactionType and m.changeBalance = :amount and m.previousTotalBalance = :beforeTotalBalance and m.lastTotalBalance = :afterTotalBalance")
    List<MoneyPoolCashFlow> searchMoneyPoolCashFlow(@Param("moneyPoolId")int moneyPoolId,@Param("geaMoneyTransferId")String geaMoneyTransferId,@Param("transactionDateTime")String transactionDateTime,@Param("serviceType")String serviceType,@Param("transactionType")String transactionType,@Param("amount")BigDecimal amount,@Param("beforeTotalBalance")BigDecimal beforeTotalBalance,@Param("afterTotalBalance") BigDecimal afterTotalBalance);

    List<MoneyPoolCashFlow> findByMoneyPoolId(int moneyPoolId);
}
