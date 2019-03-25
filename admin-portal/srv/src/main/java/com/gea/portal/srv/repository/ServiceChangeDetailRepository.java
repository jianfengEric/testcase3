package com.gea.portal.srv.repository;

import com.gea.portal.srv.entity.ServiceBatchChgReq;
import com.gea.portal.srv.entity.ServiceChangeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceChangeDetailRepository extends JpaRepository<ServiceChangeDetail, Long> {

    @Query("from ServiceChangeDetail where serviceBatchChgReq =?1")
    public List<ServiceChangeDetail> findByServiceBatchChgReq(@Param("serviceBatchChgReq") ServiceBatchChgReq serviceBatchChgReq);
}
