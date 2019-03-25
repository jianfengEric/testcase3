package com.tng.portal.sms.server.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tng.portal.sms.server.entity.SMSJobDetail;

@Repository
public interface JobDetailRepository extends JpaRepository<SMSJobDetail,Long> {

    @Query("select a from SMSJobDetail a where a.status = ?1 or a.status = ?2 ")
    List<SMSJobDetail> findByStatusAndCount(String status, String status1);
    
    @Query("select max(d.sentTimestamp) from SMSJobDetail d where d.smsProvider.id = ?1")
    Date findLastUsedBySmsProvider(String smsProviderId);

	List<SMSJobDetail> findByJobIdAndStatusAndMobileNumber(Long id, String status, String mobile);
	
}
