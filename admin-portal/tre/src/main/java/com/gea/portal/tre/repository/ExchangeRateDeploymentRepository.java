package com.gea.portal.tre.repository;

import com.gea.portal.tre.entity.ExchangeRateDeployment;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2018/9/20.
 */
@Repository
public interface ExchangeRateDeploymentRepository extends JpaRepository<ExchangeRateDeployment, Long>{
	
    @Query(" from ExchangeRateDeployment e where e.status='DEPLOYED' and e.deployEnvir=:instance order by e.updateDate desc,e.createDate desc")
    List<ExchangeRateDeployment> findAllExchangeRateDeployment(@Param("instance")Instance instance);

}
