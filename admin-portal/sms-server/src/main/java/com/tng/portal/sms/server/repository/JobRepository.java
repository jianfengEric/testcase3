package com.tng.portal.sms.server.repository;

import com.tng.portal.sms.server.entity.SMSJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<SMSJob,Long>, JpaSpecificationExecutor<SMSJob> {
	
	@Query("select max(job.referenceId) from SMSJob job")
	String findMaxMAMId();
	
	@Query("select job.referenceId from SMSJob job where ROWNUM <= 1 ORDER BY job.id DESC") 
	String findMaxRefId();

	@Query("select distinct a from SMSJob a left join fetch a.jobDetails b where b.status = ?1 or b.status = ?2")
	List<SMSJob> findByStatusAndCount(String status, String status1);

	SMSJob findTopByReferenceId(String key);

	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and j.anaApplication.code = ?3")
	List<SMSJob> findByDateAndCode(Date start, Date end, String code);


	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and d.status = ?1 and j.referenceId = ?2 order by j.createDate desc")
	List<SMSJob> findJobJobId(String status, String jobId);
	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and d.status = ?1 and j.referenceId = ?2 and d.mobileNumber = ?3 order by j.createDate desc")
	List<SMSJob> findJobJobIdNumber(String status, String jobId, String number);

	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and d.status = ?3 order by j.createDate desc")
	List<SMSJob> findJobDate(Date start, Date end, String status);
	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and d.status = ?3 and d.mobileNumber = ?4 order by j.createDate desc")
	List<SMSJob> findJobDateNumber(Date start, Date end, String status, String number);

	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and d.status = ?3 and j.senderAccountId = ?4 order by j.createDate desc")
	List<SMSJob> findJobSender(Date start, Date end, String status, String sender);
	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and d.status = ?3 and j.senderAccountId = ?4 and d.mobileNumber = ?5 order by j.createDate desc")
	List<SMSJob> findJobSenderNumber(Date start, Date end, String status, String sender, String number);

	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and d.status = ?3 and d.smsProvider.providerName = ?4 and j.anaApplication.code = ?5 order by j.createDate desc")
	List<SMSJob> findJobProvider(Date start, Date end, String status, String provider, String appCode);
	@Query("select distinct j from SMSJob j left join fetch j.jobDetails d where j.id is not null and j.createDate between ?1 and ?2 and d.status = ?3 and d.smsProvider.providerName = ?4 and j.anaApplication.code = ?5 and d.mobileNumber = ?6 order by j.createDate desc")
	List<SMSJob> findJobProviderNumber(Date start, Date end, String status, String provider, String appCode, String number);


}
