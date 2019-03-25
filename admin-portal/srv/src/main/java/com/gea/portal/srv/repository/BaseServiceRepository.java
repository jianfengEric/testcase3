package com.gea.portal.srv.repository;

import com.gea.portal.srv.entity.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BaseServiceRepository extends JpaRepository<BaseService, Long> {

    /**
     *  Find only available
     */
    @Query(" from BaseService where status='ACTIVE'")
    List<BaseService> findActive();
}
