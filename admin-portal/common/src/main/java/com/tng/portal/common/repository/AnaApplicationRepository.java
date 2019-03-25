package com.tng.portal.common.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tng.portal.common.entity.AnaApplication;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaApplicationRepository extends JpaRepository<AnaApplication,String>{
    //AnaApplication findByAnaFunctions(AnaFunction anaFunction);
    //AnaApplication findByAnaRoles(AnaRole anaRoles);
    AnaApplication findByCode(String code);
}
