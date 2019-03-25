package com.gea.portal.ewp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwpSysConfig;

/**
 * 
 */
@Repository
public interface EwpSysConfigRepository extends JpaRepository<EwpSysConfig, Long>{
	
	@Query(" from EwpSysConfig where category=?1")
	public List<EwpSysConfig> findByCategory(String category);
}
