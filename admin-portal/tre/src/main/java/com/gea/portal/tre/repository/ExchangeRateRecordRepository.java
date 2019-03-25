package com.gea.portal.tre.repository;

import com.gea.portal.tre.entity.ExchangeRateRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2018/9/20.
 */
@Repository
public interface ExchangeRateRecordRepository extends JpaRepository<ExchangeRateRecord, Long>{
    @Query(" from ExchangeRateRecord e order by e.createDate desc")
    List<ExchangeRateRecord> findAllRecord();
    Page<ExchangeRateRecord> findByExchRateFileId(Long fileId, Pageable pageable);

    @Query(" from ExchangeRateRecord e where e.exchRateFileId =:fileId order by e.currFrom asc ")
    List<ExchangeRateRecord> findExchangeRateRecord(@Param("fileId") Long fileId);


}
