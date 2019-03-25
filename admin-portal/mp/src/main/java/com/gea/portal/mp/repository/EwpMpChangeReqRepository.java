package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.EwpMoneyPool;
import com.gea.portal.mp.entity.EwpMpChangeReq;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpMpChangeReqRepository extends JpaRepository<EwpMpChangeReq, Long> {

    List<EwpMpChangeReq> findByStatusAndCurrentEnvir(MoneyPoolStatus status, Instance instance);
    List<EwpMpChangeReq> findByStatusAndCurrentEnvirAndEwpMoneyPool(MoneyPoolStatus status, Instance instance, EwpMoneyPool ewpMoneyPool);

    @Query(value = "select * from (select r.* from ewp_mp_change_req r left join ewp_money_pool p on r.money_pool_id = p.id where r.money_pool_id = :moneyPoolId) t order by t.create_date desc limit 1;", nativeQuery = true)
    EwpMpChangeReq findByMoneyPoolId(@Param("moneyPoolId") Long moneyPoolId);
}
