package com.gea.portal.srv.repository;

import com.gea.portal.srv.entity.ServiceDeployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceDeploymentRepository extends JpaRepository<ServiceDeployment, Long> {

}
