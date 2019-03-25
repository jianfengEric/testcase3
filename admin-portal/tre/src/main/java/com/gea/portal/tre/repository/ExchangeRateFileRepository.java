package com.gea.portal.tre.repository;

import com.gea.portal.tre.entity.ExchangeRateFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Owen on 2018/9/20.
 */
@Repository
public interface ExchangeRateFileRepository extends JpaRepository<ExchangeRateFile, Long>{
    @Query(" from ExchangeRateFile e where e.status='' order by e.id desc")
    List<ExchangeRateFile> findAllRecord();
}
