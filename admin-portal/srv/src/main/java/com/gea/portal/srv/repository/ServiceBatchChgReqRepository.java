package com.gea.portal.srv.repository;

import com.gea.portal.srv.entity.ServiceBatchChgReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceBatchChgReqRepository extends JpaRepository<ServiceBatchChgReq, Long> {

}
