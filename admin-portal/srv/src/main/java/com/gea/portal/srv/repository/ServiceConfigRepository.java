package com.gea.portal.srv.repository;

import com.gea.portal.srv.entity.BaseService;
import com.gea.portal.srv.entity.ServiceConfig;
import com.tng.portal.common.enumeration.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ServiceConfigRepository extends JpaRepository<ServiceConfig, Long>,JpaSpecificationExecutor<ServiceConfig> {
    @Query("from ServiceConfig where baseService =?1 and currentEnvir =?2")
    ServiceConfig findByBaseService(@Param("baseService") BaseService baseService,@Param("instance") Instance instance);

    List<ServiceConfig> findByCurrentEnvir(Instance currentEnvir);
}
