package com.gea.portal.order.repository.production.order;

import com.gea.portal.order.entity.production.order.ProGeaTxQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProGeaTxQueueRepository extends JpaRepository<ProGeaTxQueue, Integer>,JpaSpecificationExecutor<ProGeaTxQueue> {

}
