package com.tng.portal.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tng.portal.sms.entity.SMSServiceProvider;

@Repository
public interface SMSServiceProviderRepository extends JpaRepository<SMSServiceProvider,String> {
	
}
