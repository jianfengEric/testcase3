package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaRolePermission;
import com.tng.portal.ana.entity.AnaRolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaRolePermissionRepository extends JpaRepository<AnaRolePermission,AnaRolePermissionKey>{

    @Query("select rp from AnaRolePermission rp where rp.roleId = ?1 ")
    List<AnaRolePermission> findByRoleId(Long roleId);

}
