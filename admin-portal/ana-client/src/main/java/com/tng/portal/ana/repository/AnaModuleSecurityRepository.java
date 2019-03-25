package com.tng.portal.ana.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tng.portal.ana.entity.AnaModuleSecurity;

/**
 */
@Repository
public interface AnaModuleSecurityRepository extends JpaRepository<AnaModuleSecurity,Long>{

    /**
     * find By consumers
     */
    @Query("from AnaModuleSecurity where consumer = ?1")
    public AnaModuleSecurity findByConsumer(String consumer);

}
