package com.gea.portal.ewp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwpServiceConfig;

/**
 * 
 */
@Repository
public interface EwpServiceConfigRepository extends JpaRepository<EwpServiceConfig, Long>{
	
	
}
