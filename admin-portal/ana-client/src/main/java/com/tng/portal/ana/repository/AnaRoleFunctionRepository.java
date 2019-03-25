package com.tng.portal.ana.repository;

import com.tng.portal.ana.entity.AnaRoleFunction;
import com.tng.portal.ana.entity.AnaRoleFunctionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
@Repository
public interface AnaRoleFunctionRepository extends JpaRepository<AnaRoleFunction,AnaRoleFunctionKey>{

    @Query("select rf from AnaRoleFunction rf where rf.roleId = ?1 ")
    List<AnaRoleFunction> findByRoleId(Long roleId);

}
