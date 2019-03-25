package com.tng.portal.sms.server.repository;

import com.tng.portal.sms.server.entity.SystemParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter,String> {

	List<SystemParameter> findByParamCat(String paramCat);
	
}
