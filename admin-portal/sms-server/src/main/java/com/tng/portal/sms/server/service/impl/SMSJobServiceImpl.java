package com.tng.portal.sms.server.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.vo.SMSJobQueryVo;
import com.tng.portal.ana.vo.SMSQueryParamVo;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.util.JsonUtils;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.sms.SmsSendDetail;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;
import com.tng.portal.sms.server.constant.SMSStatus;
import com.tng.portal.sms.server.entity.SMSJob;
import com.tng.portal.sms.server.entity.SMSJobDetail;
import com.tng.portal.sms.server.entity.SMSServiceApplication;
import com.tng.portal.sms.server.entity.SystemParameter;
import com.tng.portal.sms.server.repository.JobDetailRepository;
import com.tng.portal.sms.server.repository.JobRepository;
import com.tng.portal.sms.server.repository.SMSServiceApplicationRepository;
import com.tng.portal.sms.server.service.ReportService;
import com.tng.portal.sms.server.service.SMSJobService;
import com.tng.portal.sms.server.service.SMSSendService;
import com.tng.portal.sms.server.service.SystemParameterService;
import com.tng.portal.sms.server.util.GenerateUtil;
import com.tng.portal.sms.server.util.StringUtil;

@Service
public class SMSJobServiceImpl implements SMSJobService{
    @Autowired
	private JobRepository jobRepository;
    
    @Autowired
	private JobDetailRepository jobDetailRepository;
    
    @Autowired
    private AnaAccountRepository anaAccountRepository;
    
    @Autowired
    private AnaApplicationRepository anaApplicationRepository;
    
    @Qualifier("anaUserService")
	@Autowired
	private UserService userService;

	@Autowired
	private SMSServiceApplicationRepository smsServiceApplicationRepository;

	@Autowired
	private SMSSendService smsSendService;
	
	@Autowired
	private SystemParameterService systemParameterService;
	
	@Autowired
	private ReportService reportService;

	private static final Logger logger = LoggerFactory.getLogger(SMSJobServiceImpl.class);
    
    /**
	 * Query sms job list 
	 * 
	 * @param vo
	 * 			sms query param vo
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public PageDatas<SMSJobQueryVo> getJobsByPage(SMSQueryParamVo vo){
		PageDatas<SMSJobQueryVo> pageDatas = new PageDatas<>(vo.getPageNo(), vo.getPageSize());

		Sort sort = new Sort(Direction.DESC, "referenceId");
		if(vo.getSortBy() != null)
			sort=new Sort(!vo.isAscending() ? Direction.DESC : Direction.ASC, getSort(vo.getSortBy()));

        Specification<SMSJob> specifi = new Specification<SMSJob>() {  
            @Override  
            public Predicate toPredicate(Root<SMSJob> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {  
                List<Predicate> predicates = new ArrayList<>(); 
                if (StringUtils.isNotBlank(vo.getApplicationCode())){
                	Join<SMSJob, AnaApplication> join = root.join("anaApplication", JoinType.LEFT);
                	predicates.add(cb.like(join.get("code").as(String.class), "%" + vo.getApplicationCode() + "%"));
                }
                if (StringUtils.isNotBlank(vo.getStartDate())) {  
                    try {
						predicates.add(cb.greaterThanOrEqualTo(root.get("createDate").as(Date.class), new SimpleDateFormat("yyyyMMddHHmm").parse(vo.getStartDate())));
					} catch (ParseException e) {
						logger.error(e.getMessage(),e);
					}
                }  
                if (StringUtils.isNotBlank(vo.getEndDate())) {  
                	try {
						predicates.add(cb.lessThanOrEqualTo(root.get("createDate").as(Date.class), new SimpleDateFormat("yyyyMMddHHmm").parse(vo.getEndDate())));
                	} catch (ParseException e) {
						logger.error(e.getMessage(),e);
					}
                }  
                if (StringUtils.isNotBlank(vo.getContentSearch())){
                	predicates.add(cb.like(root.get("content").as(String.class), "%" + vo.getContentSearch() + "%"));
                }
                if (StringUtils.isNotBlank(vo.getSenderIdSearch())){
                	predicates.add(cb.equal(root.get("senderAccountId").as(String.class), vo.getSenderIdSearch()));
                }
                if (StringUtils.isNotBlank(vo.getReferenceIdSearch())){
                	predicates.add(cb.like(root.get("referenceId").as(String.class), "%" + vo.getReferenceIdSearch() + "%"));
                }
                if (StringUtils.isNotBlank(vo.getMobileNumberSearch())){
                	Join<SMSJob, SMSJobDetail> join = root.join("jobDetails", JoinType.LEFT);
                	predicates.add(cb.like(join.get("mobileNumber").as(String.class), "%" + vo.getMobileNumberSearch() + "%"));
                }
                if (vo.getAccountIds() != null && !vo.getAccountIds().isEmpty()){
                	predicates.add(root.<String>get("senderAccountId").in(vo.getAccountIds()));
                }
                criteriaQuery.distinct(true);
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));  
            }  
        };  
        
        Page<SMSJob> page = jobRepository.findAll(specifi, pageDatas.pageRequest(sort));  
		List<SMSJob> smsJobs = page.getContent();
		List<SMSJobQueryVo> dtos = smsJobs.stream().map(item -> {
			SMSJobQueryVo dto = new SMSJobQueryVo();
			dto.setId(item.getId());
			dto.setReferenceId(item.getReferenceId());
			dto.setContent(item.getContent());
			dto.setSenderId(item.getSenderAccountId());
			dto.setCreateDate(item.getCreateDate());
			dto.setApplicationCode(item.getAnaApplication().getCode());
			
			String mobileStr = "";
			List<String> failMobiles = new ArrayList<>();
			int successCount = 0;
			int failCount = 0;
			int resendCount = 0;
			int newCount = 0;
			int totalCount = 0;
			List<SMSJobDetail> details = item.getJobDetails();
			if(details != null && !details.isEmpty()){
				totalCount = details.size();
				for(SMSJobDetail detail : details){
					String mobile = detail.getMobileNumber();
					if(mobile.length() >= 7)
						mobile = new StringBuffer(mobile).replace(3, 7, "XXXX").toString();
					
					boolean b = false;
					switch(SMSStatus.valueOf(detail.getStatus())){
					case NEW : 
						newCount ++; break;
					case SUCCESS : 
						successCount++;
						break;
					case RESENT : 
						resendCount++; break;
					case FAIL : 
						failMobiles.add(mobile);
						failCount++; 
						break;
					default:
						break;
					}
					mobileStr += mobile + ",";
				}
				if(StringUtils.isNotBlank(mobileStr)) 
					mobileStr = mobileStr.substring(0, mobileStr.length()-1);
			}
			dto.setTotalCount(totalCount);
			dto.setSuccessCount(successCount);
			dto.setFailCount(failCount);
			dto.setMobileNumbers(mobileStr);
			dto.setFailMobiles(failMobiles);
			
			if("TRMT".equals(item.getStatus())){
				dto.setStatus("Terminate");
			}
			else if(newCount == totalCount){
				dto.setStatus("Pending");
			}
			else if(successCount + failCount + resendCount < totalCount){
				dto.setStatus("In-Progress");
			}
			else if(successCount + failCount + resendCount == totalCount){
				dto.setStatus("Completed");
			}
			
			if(item.getOriginalJob() != null){
				dto.setOriginalId(item.getOriginalJob().getId());
				dto.setStatus("Resend " + dto.getStatus());
			}
			
			return dto;
		}).collect(Collectors.toList());

    	pageDatas.initDatas(dtos, page.getTotalElements(), page.getTotalPages());
        return pageDatas;
	}
	
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	@Transactional
	public void addJob(SMSJobInputVo vo) {
		SMSJob job = new SMSJob();
		job.setSenderAccountId(vo.getCreateBy());
		job.setReferenceId(GenerateUtil.generateJobId(jobRepository.findMaxRefId()));
		job.setAnaApplication(anaApplicationRepository.findByCode(vo.getApplicationCode()));
		job.setContent(vo.getContent());
		job.setCreateDate(new Date());
		job.setStatus(SMSStatus.NEW.getDesc());
		jobRepository.save(job);

		String [] numbers = vo.getMobileNumbers().split(",");
		for (int i=0 ; i<numbers.length ; i++) {
			createJobDetail(job.getId(), numbers[i], SMSStatus.NEW.getDesc());
		}
	}
	
	@Async
	@Override
	@Transactional
	public void resend(String type, String key, String senderId) {
		com.tng.portal.common.vo.PageDatas<SmsSendDetail> pageData = reportService.getReportItemData(type, null, key, "FAIL" ,"SMS" ,null ,null ,null ,null);
		List<SmsSendDetail> sendDetails = pageData.getList();
		if(sendDetails == null || sendDetails.isEmpty()){
			return;
		}
		Map<String, List<SmsSendDetail>> map = sendDetails.stream().collect(Collectors.groupingBy(SmsSendDetail::getJobId));
		
		for (Map.Entry<String, List<SmsSendDetail>> entry : map.entrySet()) {
			SMSJob oriJob = jobRepository.findTopByReferenceId(entry.getKey());
			if(oriJob == null){
				return ;
			}
			
			List<SMSJobDetail> failDetails = oriJob.getJobDetails().stream().filter(item -> SMSStatus.FAIL.getDesc().equals(item.getStatus())).collect(Collectors.toList());
			if(failDetails == null || failDetails.isEmpty()){
				continue;
			}
			failDetails.forEach(item -> item.setStatus(SMSStatus.RESENT.getDesc()));
			
			SMSJob resendJob = new SMSJob();
			resendJob.setReferenceId(GenerateUtil.generateJobId(jobRepository.findMaxRefId()));
			resendJob.setAnaApplication(oriJob.getAnaApplication());
			resendJob.setContent(oriJob.getContent());
			resendJob.setCreateDate(new Date());
			resendJob.setSenderAccountId(senderId);
			resendJob.setStatus(SMSStatus.NEW.getDesc());
			resendJob.setOriginalJob(oriJob);
			jobRepository.save(resendJob);
	
			for (SMSJobDetail detail : failDetails) {
				createJobDetail(resendJob.getId(), detail.getMobileNumber(), SMSStatus.NEW.getDesc());
			}
		}
	}

	private void createJobDetail(Long jobId, String mobileNumber, String desc) {
		String insertSql = "INSERT INTO SMS_JOB_DETAIL (ID, SMS_JOB_ID, MOBILE_NUMBER, STATUS) VALUES (SEQ_SMS_JOB_DETAIL.nextval, ?, ?, ?)";
        Query query = entityManager.createNativeQuery(insertSql);
        query.setParameter(1, jobId);
        query.setParameter(2, mobileNumber);
        query.setParameter(3, desc);
        query.executeUpdate();
	}

	private void sendSMS(SMSJob smsJob, List<SMSJobDetail> list, List<SMSServiceApplication> appList){
		boolean isLong = false;
		boolean isSpecial = false;
		boolean isForeignCountry = false;
		if (smsJob.getContent().length() > 160) {
			isLong = true;
		}
		if (StringUtil.checkStrSpecial(smsJob.getContent())) {
			isSpecial = true;
		}
		for (SMSJobDetail item : list) {
			if (!item.getMobileNumber().startsWith("852")) {
				isForeignCountry = true;
			}
			List<SMSServiceApplication> providerList = getProviders(appList, isLong, isSpecial, isForeignCountry);
			if (null == providerList || providerList.isEmpty()) {
				break;
			}
			Thread sendThread = new Thread(new SendSMSThread(jobDetailRepository, smsSendService, providerList, item, smsJob.getContent()));
			sendThread.start();
		}
	}

	public static List<SMSServiceApplication> getProviders(List<SMSServiceApplication>  providerList, boolean isLong, boolean isSpecial, boolean isForeignCountry){
		return providerList.stream().sorted(Comparator.comparing(SMSServiceApplication::getPriority)).filter(item -> {
			boolean l = true;
			if (isLong) {
				l = item.getSmsProvider().isLongSMS() == isLong;
			}
			boolean s = true;
			if (isSpecial) {
				s = item.getSmsProvider().isSpecialCharacter() == isSpecial;
			}
			boolean f = true;
			if (isForeignCountry) {
				f = item.getSmsProvider().isSendForeignCountry() == isForeignCountry;
			}
			return l&&s&&f;
		}).collect(Collectors.toList());
	}

	private String getSort(String sortBy){
		Map<String,String> orderMap = orderMap();
		return StringUtils.isBlank(orderMap.get(sortBy))?orderMap.get("default"):orderMap.get(sortBy);
	}

	private Map<String,String> orderMap(){
		Map<String,String> orderMap = new HashMap<>();
		orderMap.put("default", "referenceId");
		orderMap.put("createDate","createDate");
		orderMap.put("content","content");
		orderMap.put("senderName","senderAccountId");
		orderMap.put("referenceId","referenceId");
		return orderMap;
	}

	@Override
	@Transactional
	public String terminateJob(Long id) {
		SMSJob job = jobRepository.findOne(id);
		List<SMSJobDetail> dtls = job.getJobDetails();
		boolean canTerminate = false;
		if(dtls != null && !dtls.isEmpty()){
			for(SMSJobDetail dtl : dtls){
				String insertSql = "";
				if(SMSStatus.NEW.name().equals(dtl.getStatus())){
					insertSql = "update sms_job_detail d set d.status = ? where d.id = ?";
			        Query query = entityManager.createNativeQuery(insertSql);
			        query.setParameter(1, SMSStatus.FAIL.name());
			        query.setParameter(2, dtl.getId());
			        query.executeUpdate();
					canTerminate = true;
				}
			}
		}
		if(!canTerminate){
			return "1001";
		}
		job.setStatus("TRMT");
		jobRepository.save(job);
		return "0000";
	}

	@Override
	@Transactional
	public String findMobile(Long id, String status, String mobile) {
		List<SMSJobDetail> details = jobDetailRepository.findByJobIdAndStatusAndMobileNumber(id, status, mobile);
		List<SystemParameter> params = systemParameterService.getParamByCategory("areaCode");
		String result = "";
		if(details != null && !details.isEmpty()){
			List<SmsSendDetail> sendDetails = details.stream().map(item -> {
				SmsSendDetail sendDetail = new SmsSendDetail();
				sendDetail.setMobile(item.getMobileNumber());
				sendDetail.setSendTime(item.getSentTimestamp());
				boolean b = false;
				for(SystemParameter param : params){
					if(mobile.startsWith(param.getParamId())){
						sendDetail.setCountry(param.getParamValue());
						b = true;
						break;
					}
				}
				if(!b){
					sendDetail.setCountry("Others");
				}
				return sendDetail;
			}).collect(Collectors.toList());

			result = JsonUtils.toJSon(sendDetails);
		}
		return result;
	}
	
}