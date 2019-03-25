package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaFunctionRepository extends JpaRepository<AnaFunction,String>, JpaSpecificationExecutor<AnaFunction>{

    List<AnaFunction> findByCode(String code);

    @Modifying
    @Query(value = "update AnaFunction f set f.code=?1 where f.code=?2")
    void updateCode(String newCode, String oldCode);

}
