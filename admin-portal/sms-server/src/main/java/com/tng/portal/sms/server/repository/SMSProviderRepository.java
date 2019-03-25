package com.tng.portal.sms.server.repository;

import com.tng.portal.sms.server.entity.SMSProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSProviderRepository extends JpaRepository<SMSProvider,String> {
	
}
