package com.gea.portal.mp.repository;

import com.gea.portal.mp.entity.EwpPoolDeployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Repository
public interface EwpPoolDeploymentRepository extends JpaRepository<EwpPoolDeployment, Long> {
}
