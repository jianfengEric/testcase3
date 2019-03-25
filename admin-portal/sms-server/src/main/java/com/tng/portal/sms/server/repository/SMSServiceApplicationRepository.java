package com.tng.portal.sms.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.sms.server.entity.SMSProvider;
import com.tng.portal.sms.server.entity.SMSServiceApplication;

@Repository
public interface SMSServiceApplicationRepository extends JpaRepository<SMSServiceApplication,String>, JpaSpecificationExecutor<SMSServiceApplication> {

	List<SMSServiceApplication> findByAnaApplication(AnaApplication anaApplication);

	SMSServiceApplication findByAnaApplicationAndSmsProvider(AnaApplication anaApplication, SMSProvider smsProvider);
	
	SMSServiceApplication findBySmsProvider(SMSProvider smsProvider);

	List<SMSServiceApplication> findByStatus(String status);

	@Query("select a from SMSServiceApplication a where a.status = :status and a.anaApplication.code = :code")
	List<SMSServiceApplication> findByStatusAndCode(@Param("status")String status, @Param("code")String code);
	
}
