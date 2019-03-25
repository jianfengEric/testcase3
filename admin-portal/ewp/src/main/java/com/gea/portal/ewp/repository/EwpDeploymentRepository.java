package com.gea.portal.ewp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gea.portal.ewp.entity.EwpDeployment;

/**
 * Created by Owen on 2018/8/31.
 */
@Repository
public interface EwpDeploymentRepository extends JpaRepository<EwpDeployment, Long>{

}
