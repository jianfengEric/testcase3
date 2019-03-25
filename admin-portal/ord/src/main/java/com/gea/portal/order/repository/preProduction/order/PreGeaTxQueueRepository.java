package com.gea.portal.order.repository.preProduction.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gea.portal.order.entity.preProduction.order.PreGeaTxQueue;

@Repository
public interface PreGeaTxQueueRepository extends JpaRepository<PreGeaTxQueue, Integer>,JpaSpecificationExecutor<PreGeaTxQueue> {

}
