package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaFunction;
import com.tng.portal.ana.entity.AnaRole;
import com.tng.portal.ana.entity.AnaRoleFunctionPermission;
import com.tng.portal.ana.entity.AnaRoleFunctionPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaRoleFunctionPermissionRepository extends JpaRepository<AnaRoleFunctionPermission,AnaRoleFunctionPk>{

    List<AnaRoleFunctionPermission> findByAnaRole(AnaRole anaRole);
    List<AnaRoleFunctionPermission> findByAnaFunction(AnaFunction anaFunction);


    @Modifying
    @Query(value = "delete from ana_role_function_permission  where role_id=?1",nativeQuery = true)
    void delectByRoleId(Long roleId);

    @Modifying
    @Query(value = "delete from ana_role_function_permission  where function_code=?1",nativeQuery = true)
    void delectByFunctionCode(String code);


}
