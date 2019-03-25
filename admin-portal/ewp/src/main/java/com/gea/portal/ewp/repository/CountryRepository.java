package com.gea.portal.ewp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.Country;

/**
 * 
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long>{
	
	@Query("from Country order by nameEn")
	public List<Country> findAll();
}
