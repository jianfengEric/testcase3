package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaPermissionRepository extends JpaRepository<AnaPermission,Integer> {

    List<AnaPermission> findById(int id);

    List<AnaPermission> findByName(String name);

    @Query("select p from AnaPermission p where p.id = (select max(pp.id) from AnaPermission pp)")
    AnaPermission findMaxIdAnaPermission();


}
