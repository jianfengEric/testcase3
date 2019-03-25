package com.tng.portal.sms.server.service.impl;


import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.CoderUtil;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.sms.ReportDataVo;
import com.tng.portal.common.vo.sms.ReportDataVto;
import com.tng.portal.common.vo.sms.SmsSendDetail;
import com.tng.portal.sms.server.entity.SMSJob;
import com.tng.portal.sms.server.entity.SMSJobDetail;
import com.tng.portal.sms.server.entity.SMSProvider;
import com.tng.portal.sms.server.entity.Sms;
import com.tng.portal.sms.server.entity.SystemParameter;
import com.tng.portal.sms.server.repository.JobRepository;
import com.tng.portal.sms.server.repository.PageDatas;
import com.tng.portal.sms.server.repository.SMSProviderRepository;
import com.tng.portal.sms.server.service.ReportService;
import com.tng.portal.sms.server.service.SystemParameterService;
import com.tng.portal.sms.server.util.ReportUtils;
import com.tng.portal.sms.server.vo.ReportDto;

/**
 * Created by Owen on 2017/6/12.
 */
@Service("smsReportService")
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
	private SystemParameterService systemParameterService;

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;
    
	@PersistenceContext
    private EntityManager entityManager;
	@Autowired
	private SMSProviderRepository smsProviderRepository;

    private  SimpleDateFormat dateFmd = new SimpleDateFormat("MM-dd-yyyy (EEE)", Locale.ENGLISH);
    private  SimpleDateFormat dateFmm = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    private  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private  SimpleDateFormat dateFma = new SimpleDateFormat("yyyy-MM-dd (EEE) HH:mm:ss", Locale.ENGLISH);
    private  SimpleDateFormat dateYMD = new SimpleDateFormat(DateCode.dateFormatMd, Locale.ENGLISH);

    @Override
    public ReportDataVto getReportData(Integer pageNo, Integer pageSize, String searchBy, String search, String sortBy, Boolean isAscending, String appCode, String providerId) {
        logger.info("getReportData() pageNo:{} pageSize:{} searchBy:{} search:{} sortBy:{} isAscending:{} appCode:{} providerId:{}",
                pageNo, pageSize, searchBy, search, sortBy, isAscending, appCode, providerId);
        PageDatas<ReportDataVo> pageDatas = new PageDatas<>(pageNo, pageSize);
        List<ReportDataVo> result = getReportDataVos(search,appCode,searchBy);
        maskMobile(result);//mask the 4th to 7th digits by X

        List<ReportDataVo> newResult = sortData(result, sortBy, isAscending);// sort 
        if(result.size() > (pageNo-1)*pageSize) {
            newResult = newResult.stream().skip(pageSize*(pageNo-1)).limit(pageSize).collect(Collectors.toList());// paging 
        }
        pageDatas.setList(newResult);
        pageDatas.setTotal(result.size());
        pageDatas.setTotalPages((result.size() + pageSize - 1) / pageSize);
        return getReportDataVto(pageDatas);
    }

    private List<ReportDataVo> getReportDataVos(final String searchDate, String appCode, String searchType){
        List<ReportDataVo> reportDataVos = new ArrayList<>();
        List<Sms> smsList = getSmsList(searchDate,appCode);
        if(smsList.isEmpty()){
            return reportDataVos;
        }
        Map<String, String> accountMap = null;
        Map<String, List<Sms>> smsListMap=null;
        if("jobId".equals(searchType)){
            smsListMap = smsList.stream().collect(Collectors.groupingBy(item -> item.getJobReferenceId()));
            accountMap = httpClientUtils.httpGet(PropertiesUtil.getAppValueByKey("sms.users.url"), Map.class, null);
        } else if("daily".equals(searchType)){
            smsListMap = smsList.stream().collect(Collectors.groupingBy(item->dateFmd.format(getDbDate(item.getCreateDate()))));
        } else if("monthly".equals(searchType)){
            smsListMap = smsList.stream().collect(Collectors.groupingBy(item->dateFmm.format(getDbDate(item.getCreateDate()))));
        } else if("sender".equals(searchType)){
            smsListMap = smsList.stream().collect(Collectors.groupingBy(item -> item.getSenderAccountId()));
            accountMap = httpClientUtils.httpGet(PropertiesUtil.getAppValueByKey("sms.users.url"), Map.class, null);
        } else if("provider".equals(searchType)){
            smsListMap = smsList.stream().filter(item->!StringUtils.isBlank(item.getProviderId()))
                    .collect(Collectors.groupingBy(item -> item.getProviderId()));
        } else {
            logger.error("Unknown search type:{}",searchType);
            return reportDataVos;
        }
        List<SMSProvider> smsProviders = smsProviderRepository.findAll();
        Map<String,SMSProvider> providerMap = new HashMap<>();
        for (SMSProvider provider : smsProviders) {
            providerMap.put(provider.getId(),provider);
        }
        final Map<String, String> finalAccountMap = accountMap;
        smsListMap.forEach((key,value)->{
            ReportDataVo reportDataVo = new ReportDataVo();
            List<String> jobId = new ArrayList<>();
            int successCount = 0;
            int failCount = 0;
            StringBuilder providers = new StringBuilder();
            StringBuilder frontNumbers = new StringBuilder(); //  The first five numbers 
            StringBuilder mobileNumbers = new StringBuilder(); //  All numbers 
            for (int i = 0;i<value.size();i++) {
                Sms sms = value.get(i);
                if (!jobId.contains(sms.getJobReferenceId())) {
                    jobId.add(sms.getJobReferenceId());
                }
                if ("SUCCESS".equals(sms.getStatus())) {
                    successCount++;
                } else if ("FAIL".equals(sms.getStatus())) {
                    failCount++;
                }
                mobileNumbers.append(sms.getMobileNumber()).append(";");
                if(i<=4){
                    frontNumbers.append(sms.getMobileNumber()).append(";");
                }
                if("jobId".equals(searchType)){
                    String providerId = sms.getProviderId();
                    if(!StringUtils.isBlank(providerId)) {
                        String provider = providerMap.get(providerId).getProviderName();
                        if (providers.indexOf(provider) == -1) {
                            providers.append(provider).append(";");
                        }
                    }
                }
            }
            if("jobId".equals(searchType)){
                reportDataVo.setJobId(key);
                if(providers.length()>0) {
                    reportDataVo.setProvider(providers.toString().substring(0, providers.length() - 1));
                }
                reportDataVo.setSender(finalAccountMap.get(value.get(0).getSenderAccountId()));
                reportDataVo.setDate(dateFma.format(getDbDate(value.get(0).getCreateDate())));
                reportDataVo.setFormatDate(dateYMD.format(getDbDate(value.get(0).getCreateDate())));
            } else if("daily".equals(searchType)){
                reportDataVo.setDate(key);
                reportDataVo.setFormatDate(dateYMD.format(getDbDate(value.get(0).getCreateDate())));
            } else if("monthly".equals(searchType)){
                reportDataVo.setDate(key);
                reportDataVo.setFormatDate(dateYMD.format(getDbDate(value.get(0).getCreateDate())));
            } else if("sender".equals(searchType)){
                reportDataVo.setSender(finalAccountMap.get(key));
                reportDataVo.setFormatDate(searchDate);
            } else if("provider".equals(searchType)){
                reportDataVo.setProvider(providerMap.get(key).getProviderName());
                reportDataVo.setFormatDate(searchDate);
            }
            reportDataVo.setAccountId(CoderUtil.encrypt(value.get(0).getSenderAccountId()));
            reportDataVo.setJobCount(jobId.size());
            reportDataVo.setSuccessCount(successCount);
            reportDataVo.setFailCount(failCount);
            reportDataVo.setTotalCount(successCount+failCount);
            if(frontNumbers.length()>0) {
                reportDataVo.setMobNo(frontNumbers.toString().substring(0, frontNumbers.length() - 1));
            }
            if(mobileNumbers.length()>0) {
                reportDataVo.setAllMobileNumber(mobileNumbers.toString().substring(0, mobileNumbers.length() - 1));
            }
            reportDataVos.add(reportDataVo);
        });
        return reportDataVos;
    }

    private List<Sms> getSmsList(String searchDate, String appCode){
        String[] split = searchDate.split("-");
        String startTime=null;
        String endTime=null;
        if (split.length > 1) {
            startTime = split[0] + " 00:00:00";
            endTime = split[1] + " 23:59:59";
        }else{
            String date = searchDate;
            if (searchDate.contains("-")) {
                date = searchDate.replace("-", "");
            }
            startTime = date + " 00:00:00";
            endTime = date + " 23:59:59";
        }
        String dbDateFormat = "yyyymmdd hh24:mi:ss";
        String startDate = "to_date('" + startTime + "','" + dbDateFormat + "')";
        String endDate = "to_date('" + endTime + "','" + dbDateFormat + "')";
        String sql = "select j.id, j.reference_id, j.sender_account_id, j.create_date from sms_job j"
                + " where j.application_code = '" + appCode + "'"
                + " and j.create_date between " + startDate + " and " + endDate;
        Query jobQuery = entityManager.createNativeQuery(sql);
        List<Object> jobList = jobQuery.getResultList();
        if(jobList.isEmpty()){
            return new ArrayList<>();
        }
        StringBuilder idIn = new StringBuilder();
        Map<Long, Sms> jobMap = jobList.stream().map(item->{
            Sms sms = new Sms();
            Object[] column = (Object[]) item;
            sms.setId(Long.valueOf(column[0].toString()));
            sms.setJobReferenceId(column[1].toString());
            sms.setSenderAccountId(column[2].toString());
            sms.setCreateDate(column[3].toString());
            idIn.append(sms.getId()).append(",");
            return sms;
        }).collect(Collectors.toMap(key->key.getId(),value->value));

        sql = "select d.sms_job_id, d.mobile_number, d.status, d.sms_provider_id from sms_job_detail d"
                + " where d.sms_job_id in(" + idIn.substring(0,idIn.length()-1)+")";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<Object> resultList = nativeQuery.getResultList();
        return resultList.stream().map(item -> {
            Sms sms = new Sms();
            Object[] column = (Object[]) item;

            Sms job = jobMap.get(Long.valueOf(column[0].toString()));
            sms.setId(job.getId());
            sms.setJobReferenceId(job.getJobReferenceId());
            sms.setSenderAccountId(job.getSenderAccountId());
            sms.setCreateDate(job.getCreateDate());
            sms.setMobileNumber(column[1].toString());
            sms.setStatus(column[2].toString());
            if(column[3]!=null) {
                sms.setProviderId(column[3].toString());
            }
            return sms;
        }).collect(Collectors.toList());
    }

    private Date getDbDate(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date parse = null;
        try {
            parse = dateFormat.parse(date);
        } catch (ParseException e) {
            logger.error("getDbDate",e);
        }
        return parse;
    }

	private void maskMobile(List<ReportDataVo> result) {
        if(result != null && !result.isEmpty()){
        	for(ReportDataVo vo : result){
        		String mobNo = vo.getMobNo();
        		if(StringUtils.isNotBlank(mobNo)){
        			String [] mobs = mobNo.split(",");
        			List<String> mobList = new ArrayList<>();
        			for(String mob : mobs){
        				if(mob.length() >= 7){
        					mob = new StringBuffer(mob).replace(3, 7, "XXXX").toString();
        					mobList.add(mob);
        				}
        			}
        			if(mobList.stream().reduce((o1, o2) -> o1 + ";" + o2).isPresent()){
        			    vo.setMobNo(mobList.stream().reduce((o1, o2) -> o1 + ";" + o2).get());
                    }
        		}
        	}
        }
	}

	@SuppressWarnings("unchecked")
	private List<SMSJob> search(String search, String appCode) {
		String sql = "SELECT a.ID,a.REFERENCE_ID,a.SENDER_ACCOUNT_ID,a.CREATE_DATE,b.MOBILE_NUMBER,b.STATUS,b.SENT_TIMESTAMP,b.SMS_PROVIDER_ID"
				+ " FROM SMS_JOB a left join SMS_JOB_DETAIL b on a.id = b.sms_job_id where 1=1";
		
		if(StringUtils.isNotBlank(appCode)){
        	sql += " and a.APPLICATION_CODE = ?";
        }
		
		String startDate = null;
        String endDate = null;
        if(null != search && !search.isEmpty()){
	        String[] strs = search.split("-");
	        try{
		        if(strs.length > 1){
		            startDate = strs[0] + " 00:00:00";
		            endDate = strs[1] + " 23:59:59";
		        }else {
		            startDate = search + " 00:00:00";
		            endDate = search + " 23:59:59";
		        }
	        }catch(Exception e){
                logger.info("search: "+e);
            }
	        
	        if(sql.contains("where")){
	        	sql += " and a.CREATE_DATE >= TO_DATE(?,'yyyymmdd hh24:mi:ss')  and a.CREATE_DATE <= TO_DATE(?,'yyyymmdd hh24:mi:ss')";
	        }
        }
        sql += " order by a.id";
        
        Query query =  entityManager.createNativeQuery(sql);
        int count = 0;
        Pattern r = Pattern.compile("\\?");
        Matcher m = r.matcher(sql);
        while (m.find()) {
        	count++;
        }
        switch(count){
        case 1 : 
        	query.setParameter(1, appCode); break;
        case 2 : 
        	query.setParameter(1, startDate);
        	query.setParameter(2, endDate); break;
        case 3 : 
        	query.setParameter(1, appCode);
        	query.setParameter(2, startDate);
        	query.setParameter(3, endDate); break;
        }
        
        List<Object[]> resultList = query.getResultList();
        SMSJob job = new SMSJob();
        SMSJobDetail detail = new SMSJobDetail();
        SMSProvider provider = new SMSProvider();
        long jobId = 0;
        if(resultList != null && !resultList.isEmpty()){
	        for(Object[] o : resultList){
	        	if(jobId != (Long)o[0]){
	        		job.setId((Long)o[0]);
		        	job.setReferenceId((String)o[1]);
		        	job.setSenderAccountId((String)o[3]);
		        	job.setCreateDate((Date)o[4]);
	        	}
	        	detail.setMobileNumber((String)o[5]);
	        	detail.setStatus((String)o[6]);
	        	detail.setSentTimestamp((Date)o[7]);
	        	provider.setId((String)o[8]);
	        }
        }
        return null;
	}

	private ReportDataVto getReportDataVto(PageDatas<ReportDataVo> pageDatas){
		ReportDataVto reportDataVto=new ReportDataVto();
        reportDataVto.setList(pageDatas.getList());
        reportDataVto.setTotal(pageDatas.getTotal());
        reportDataVto.setTotalPages(pageDatas.getTotalPages());

        int jobCount=0;
        int successCount=0;
        int failedCount=0;
        int totalCount=0;
        for (ReportDataVo reportData : pageDatas.getList()) {// Statistical longitudinal numbers 
            jobCount+=reportData.getJobCount();
            successCount+=reportData.getSuccessCount();
            failedCount+=reportData.getFailCount();
            totalCount+=reportData.getTotalCount();
        }
        reportDataVto.setJobCount(jobCount);
        reportDataVto.setSuccessCount(successCount);
        reportDataVto.setFailedCount(failedCount);
        reportDataVto.setTotalCount(totalCount);
        return reportDataVto;
    }

    @Override
    public com.tng.portal.common.vo.PageDatas<SmsSendDetail> getReportItemData(String searchType, String searchTime,String itemValue, String sendResult, String appCode, String sortBy, Boolean isAscending, Integer pageNumber, Integer pageSize) {
        return getItemData(searchType,searchTime,itemValue,sendResult,appCode,sortBy,isAscending,pageNumber,pageSize,"");
    }

    private com.tng.portal.common.vo.PageDatas<SmsSendDetail> getItemData(String searchType, String searchTime,String itemValue, String sendResult, String appCode, String sortBy, Boolean isAscending, Integer pageNumber, Integer pageSize,String searchNumber){
        boolean queryAll = false;
        if(pageNumber == null || pageSize == null){
            queryAll=true;
        }else{
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            if (pageSize < 1) {
                pageSize = 20;
            }
        }
        com.tng.portal.common.vo.PageDatas<SmsSendDetail> pageDatas = new com.tng.portal.common.vo.PageDatas<>(pageNumber,pageSize);
        List<SMSJob> jobList = null;
        Specifications<SMSJob> itemWhere = getItemWhere(searchType,searchTime,itemValue,sendResult,appCode,searchNumber);
        jobList = jobRepository.findAll(itemWhere);
        if(jobList.isEmpty()){
            pageDatas.setList(new ArrayList<SmsSendDetail>());
            return pageDatas;
        }
        final List<SmsSendDetail> smsSendDetailList = new ArrayList<>();
        List<SystemParameter> params = systemParameterService.getParamByCategory("areaCode");
        jobList.stream().forEach(job -> {
            List<SMSJobDetail> jobDetails = job.getJobDetails();
            jobDetails.stream().forEach(detail -> {
                if(!sendResult.equals(detail.getStatus())){
                    return;
                }
                if("provider".equals(searchType)) {
                    if (detail.getSmsProvider()==null || !itemValue.equals(detail.getSmsProvider().getProviderName())) {
                        return;
                    }
                }
                SmsSendDetail smsSendDetail = new SmsSendDetail();
                smsSendDetail.setJobId(job.getReferenceId());
                smsSendDetail.setMobile(detail.getMobileNumber());
                smsSendDetail.setSendTime(detail.getSentTimestamp());
                boolean b = false;
                for (SystemParameter param : params) {
                    if (smsSendDetail.getMobile().startsWith(param.getParamId())) {
                        smsSendDetail.setCountry(param.getParamValue());
                        b = true;
                        break;
                    }
                }
                if(!b){
                    smsSendDetail.setCountry("Others");
                }
                smsSendDetailList.add(smsSendDetail);
            });
        });
        pageDatas.setTotal(Long.valueOf(smsSendDetailList.size()));
        if(queryAll){
            pageDatas.setList(smsSendDetailList);
        }else {
            int mo = smsSendDetailList.size() % pageSize;
            int page = smsSendDetailList.size() / pageSize;
            pageDatas.setTotalPages(mo == 0 ? page : page + 1);
            int start = (pageNumber - 1) * pageSize;
            int end = pageNumber * pageSize;
            if (start < smsSendDetailList.size()) {
                if (end < smsSendDetailList.size()) {
                    pageDatas.setList(smsSendDetailList.subList(start, end));
                } else {
                    pageDatas.setList(smsSendDetailList.subList(start, smsSendDetailList.size()));
                }
            } else {
                pageDatas.setList(new ArrayList<SmsSendDetail>());
            }
        }
        logger.info("pageDatas.getList().size() "+pageDatas.getList().size());
        return pageDatas;
    }

    private List<SMSJob> getSmsJob(String searchType,String searchTime,String itemValue,String sendResult,String appCode,String searchNumber){
        List<SMSJob> smsJobs = null;
        Date[] selectDate = getSelectDate(searchTime);
        if ("jobId".equals(searchType)) {
            if (StringUtils.isBlank(searchNumber)) {
                smsJobs = jobRepository.findJobJobId(sendResult, itemValue);
            } else {
                smsJobs = jobRepository.findJobJobIdNumber(sendResult, itemValue,searchNumber);
            }
        } else if ("daily".equals(searchType)) {
            if (StringUtils.isBlank(searchNumber)) {
                smsJobs = jobRepository.findJobDate(selectDate[0],selectDate[1],sendResult);
            } else {
                smsJobs = jobRepository.findJobDateNumber(selectDate[0],selectDate[1],sendResult,searchNumber);
            }
        } else if ("monthly".equals(searchType)) {
            Date[] monthlyDate = getMonthlyDate(itemValue);
            if (StringUtils.isBlank(searchNumber)) {
                smsJobs = jobRepository.findJobDate(monthlyDate[0],monthlyDate[1],sendResult);
            } else {
                smsJobs = jobRepository.findJobDateNumber(monthlyDate[0],monthlyDate[1],sendResult,searchNumber);
            }
        } else if ("sender".equals(searchType)) {
            if (StringUtils.isBlank(searchNumber)) {
                smsJobs = jobRepository.findJobSender(selectDate[0],selectDate[1],sendResult,CoderUtil.decrypt(itemValue));
            } else {
                smsJobs = jobRepository.findJobSenderNumber(selectDate[0],selectDate[1],itemValue,appCode,searchNumber);
            }
        } else if ("provider".equals(searchType)) {
            if (StringUtils.isBlank(searchNumber)) {
                smsJobs = jobRepository.findJobProvider(selectDate[0],selectDate[1],sendResult,itemValue,appCode);
            } else {
                smsJobs = jobRepository.findJobProviderNumber(selectDate[0],selectDate[1],sendResult,itemValue,appCode,searchNumber);
            }
        } else {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        return smsJobs;
    }


    @Override
    public com.tng.portal.common.vo.PageDatas<SmsSendDetail> searchReportItemData(String searchType, String searchTime, String itemValue, String sendResult, String searchNumber, String appCode, String sortBy, Boolean isAscending, Integer pageNumber, Integer pageSize) {
        com.tng.portal.common.vo.PageDatas<SmsSendDetail> reportItemData = getItemData(searchType, searchTime, itemValue, sendResult, appCode, sortBy, isAscending, pageNumber, pageSize,searchNumber);
        List<SmsSendDetail> collect = reportItemData.getList().stream().filter(item -> searchNumber.equals(item.getMobile())).collect(Collectors.toList());
        reportItemData.setList(collect);
        reportItemData.setTotal(Long.valueOf(collect.size()));
        return reportItemData;
    }

    private Specifications<SMSJob> getItemWhere(String searchType,String searchTime,final String itemValue,String sendResult,String appCode,String searchNumber){
        Specifications<SMSJob> where = Specifications.where((root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
            return criteriaBuilder.isNotNull(root.get("id"));
        });
        where = where.and((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.join("jobDetails").get("status"), sendResult);
        });
        if(!StringUtils.isBlank(searchNumber)){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> {
                Join<Object, Object> jobDetails = root.join("jobDetails");
                return criteriaBuilder.equal(jobDetails.get("mobileNumber"), searchNumber);
            });
        }
        if(StringUtils.isBlank(searchType) || StringUtils.isBlank(itemValue)){
            logger.error("StringUtils.isBlank(itemValue) || StringUtils.isBlank(itemValue)");
            return where;
        }
        if("jobId".equals(searchType)){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("referenceId"),itemValue));
        } else if("daily".equals(searchType)){
            try {
                Date startDate = sdf.parse(itemValue + " 00:00:00");
                Date endDate = sdf.parse(itemValue + " 23:59:59");
                where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("createDate"), startDate, endDate));
            } catch (ParseException e) {
                logger.error("ParseException",e);
            }
        } else if("monthly".equals(searchType)){
            try {
                String month = itemValue.substring(0, itemValue.length() - 2);
                Date startDate = sdf.parse(month + "01" + " 00:00:00");
                int day = getDayOfMonth(startDate);
                Date endDate = sdf.parse(month + day + " 23:59:59");
                where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("createDate"), startDate, endDate));
            } catch (ParseException e) {
                logger.error("ParseException",e);
            } catch (Exception e) {
                logger.error("Exception",e);
            }
        } else if("sender".equals(searchType)){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get("senderAccountId"), CoderUtil.decrypt(itemValue));
            });
            try {
                Date startDate;
                Date endDate;
                String date = searchTime;
                String[] split = searchTime.split("-");
                if(split.length>1){
                    startDate = sdf.parse(split[0] + " 00:00:00");
                    endDate = sdf.parse(split[1] + " 23:59:59");
                }else {
                    if(date.contains("-")) {
                    }
                    startDate = sdf.parse(date + " 00:00:00");
                    endDate = sdf.parse(date + " 23:59:59");
                }
                where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("createDate"), startDate, endDate));
            } catch (ParseException e) {
                logger.error("ParseException",e);
            }
        } else if("provider".equals(searchType)){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                Predicate providerId = criteriaBuilder.equal(root.join("jobDetails").get("smsProvider").get("providerName"), itemValue);
                predicates.add(providerId);
                if(!StringUtils.isBlank(appCode)) {
                    Predicate app = criteriaBuilder.equal(root.get("anaApplication").get("code"), appCode);
                    predicates.add(app);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            });
            try {
                Date startDate;
                Date endDate;
                String date = searchTime;
                String[] split = searchTime.split("-");
                if(split.length>1){
                    startDate = sdf.parse(split[0] + " 00:00:00");
                    endDate = sdf.parse(split[1] + " 23:59:59");
                }else {
                    if(date.contains("-")) {
                    }
                    startDate = sdf.parse(date + " 00:00:00");
                    endDate = sdf.parse(date + " 23:59:59");
                }
                where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("createDate"), startDate, endDate));
            } catch (ParseException e) {
                logger.error("ParseException",e);
            }
        } else {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        return where;
    }

    private int getDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private List<SMSJob> sortItemData(List<SMSJob> result, Boolean isAscending) {
        if(null == result || result.isEmpty()){
            return result;
        }
        if(null == isAscending){
            return result;
        }
        Collections.sort(result, new Comparator<SMSJob>() {
            @Override
            public int compare(SMSJob o1, SMSJob o2) {
                SMSJob a = o1;
                SMSJob b = o2;
                if (!isAscending) {
                    a = o2;
                    b = o1;
                }
                if(a.getCreateDate().before(b.getCreateDate())){
                    return 1;
                }else if(a.getCreateDate().after(b.getCreateDate())){
                    return -1;
                }
                return 0;
            }
        });
        return result;
    }

    private List<SmsSendDetail> replaceNumber(List<SmsSendDetail> list){
        if(list != null && !list.isEmpty()){//mask the 4th to 7th digits by X
            for(SmsSendDetail detail : list){
                String mobile = detail.getMobile();
                if(!StringUtils.isBlank(mobile) && mobile.length()>7){
                    detail.setMobile(new StringBuffer(mobile).replace(3, 7, "XXXX").toString());
                }
            }
        }
        return list;
    }

    private List<ReportDataVo> sortData(List<ReportDataVo> result, String sortBy, Boolean isAscending) {
        if(null == result || result.isEmpty()){
            return result;
        }
        if(null == sortBy || null == isAscending || sortBy.isEmpty() ){
            return result;
        }
        Collections.sort(result, new Comparator<ReportDataVo>() {
            @Override
            public int compare(ReportDataVo o1, ReportDataVo o2) {
                ReportDataVo a = o1;
                ReportDataVo b = o2;
                if (!isAscending) {
                    a = o2;
                    b = o1;
                }
                if (sortBy.equals("date") && null != a.getDate() && null != b.getDate()) {
                    try {
                        if (a.getDate().length() == dateFma.toPattern().length()) {
                            return dateFma.parse(a.getDate()).compareTo(dateFma.parse(b.getDate()));
                        } else if (a.getDate().length() == dateFmd.toPattern().length()) {
                            return dateFmd.parse(a.getDate()).compareTo(dateFmd.parse(b.getDate()));
                        } else {
                            return dateFmm.parse(a.getDate()).compareTo(dateFmm.parse(b.getDate()));
                        }
                    } catch (ParseException e) {
                        logger.error("ParseException", e);
                    }
                }
                if (sortBy.equals("sender") && null != a.getSender() && null != b.getSender()) {
                    return a.getSender().compareTo(b.getSender());
                }
                if (sortBy.equals("jobId") && null != a.getJobId() && null != b.getJobId()) {
                    return a.getJobId().compareTo(b.getJobId());
                }
                if (sortBy.equals("provider") && null != a.getProvider() && null != b.getProvider()) {
                    return a.getProvider().compareTo(b.getProvider());
                }
                return 0;
            }
        });
        return result;
    }

    private  List<ReportDataVo> getResult(List<SMSJob> jobList, String searchBy, String searchDate, String providerId){
        Map<String, String> accounts = new HashMap<>();
        if(null != searchBy && (searchBy.equals("jobId") || searchBy.equals("sender"))){
           accounts = httpClientUtils.httpGet(PropertiesUtil.getAppValueByKey("sms.users.url"), Map.class, null);
        }
        List<ReportDataVo> result = new ArrayList<>();
        if(null == jobList || jobList.isEmpty()){
            return result;
        }
        Map<String, List<SMSJob>> listMap;
        if (null != searchBy && searchBy.equals("monthly")) {
            listMap = jobList.stream().collect(Collectors.groupingBy(item -> dateFmm.format(item.getCreateDate())));
        } 
        
        else if(null != searchBy && searchBy.equals("sender")){
            listMap = jobList.stream().collect(Collectors.groupingBy(item->item.getSenderAccountId()));
        } 
        
        else if(null != searchBy && searchBy.equals("jobId")){
            listMap = jobList.stream().collect(Collectors.groupingBy(SMSJob::getReferenceId));
        } 
        
        else if(null != searchBy && searchBy.equals("provider")){
            listMap=getProviderData(jobList,providerId);
        } 
        
        else{
            listMap = jobList.stream().collect(Collectors.groupingBy(item->dateFmd.format(item.getCreateDate())));//daily
        }
        
        for (Map.Entry<String, List<SMSJob>> map : listMap.entrySet()) {
            long successCount = 0;
            long failCount = 0;
			List<String> failMobiles = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            StringBuffer allMobileNumber = new StringBuffer();
            ReportDataVo vo = new ReportDataVo();
            List<SMSJob> jobs = map.getValue();

            if(null != jobs && !jobs.isEmpty()) {// Back to background parameters 
                if(null != searchBy && ("sender".equals(searchBy) || "provider".equals(searchBy))){
                    vo.setFormatDate(searchDate);
                }else {
                    vo.setFormatDate(dateYMD.format(jobs.get(0).getCreateDate()));
                }
                vo.setAccountId(CoderUtil.encrypt(jobs.get(0).getSenderAccountId()));
            }

            if(null != searchBy && searchBy.equals("sender")){
                vo.setSender(accounts.get(map.getKey()));
            } 
            
            else if(null != searchBy && searchBy.equals("jobId")){
                vo.setJobId(map.getKey());
            } 
            
            else if(null != searchBy && searchBy.equals("provider")){
                vo.setProvider(map.getKey());
            } 
            
            else{
                vo.setDate(map.getKey());//monthly / daily
            }

            if(null != searchBy && searchBy.equals("jobId") && null != jobs && !jobs.isEmpty()){
                vo.setSender(accounts.get(jobs.get(0).getSenderAccountId()));
                vo.setDate(dateFma.format(jobs.get(0).getCreateDate()));
                
                List<SMSJobDetail> details = jobs.get(0).getJobDetails();

                if(details.size() > 5)//FE Only the top five must be shown. 
                	details = details.subList(0, 5);
                
                Optional<String> reduce = details.stream()
                		.map(SMSJobDetail::getMobileNumber).reduce((o1, o2) -> o1 + ";" + o2);
                vo.setMobNo(reduce.isPresent() ? reduce.get() : "");
                
                String providerNames = "";
                for(SMSJobDetail detail : details){
                	if(detail.getSmsProvider() != null){
                		String providerName = detail.getSmsProvider().getProviderName();
                		if(providerName != null && !providerNames.contains(providerName)){
                			providerNames += providerName + ";";
                		}
                	}
                }
                if(StringUtils.isNotBlank(providerNames)) {
                    vo.setProvider(providerNames.substring(0, providerNames.length() - 1));
                } else {
                    vo.setProvider("");
                }
            }
            int i = 0;
            for (SMSJob job : jobs){
                List<SMSJobDetail> details = job.getJobDetails();
                for(SMSJobDetail detail : details){
					String mobile = detail.getMobileNumber();
                	if(detail.getStatus().equals("SUCCESS")){
                		successCount ++;
                	}
                	
                	else if(detail.getStatus().equals("FAIL")){
						failMobiles.add(mobile);
                		failCount ++;
                	}
                	if(i <= 5){
	                	sb.append(mobile).append(",");
	                	i++;
                    }
                    allMobileNumber.append(mobile).append(";");
                }
                vo.setFailMobiles(failMobiles);
            }
            String mobiles = sb.toString();
            if(StringUtils.isNotBlank(mobiles)) 
				vo.setMobNo(mobiles.substring(0, mobiles.length()-1));
            vo.setAllMobileNumber(allMobileNumber.toString().substring(0, allMobileNumber.length()-1));
            if(!jobs.isEmpty()){
                vo.setJobCount(jobs.size());
            }
            vo.setSuccessCount(successCount);
            vo.setFailCount(failCount);
            vo.setTotalCount(successCount+failCount);
            result.add(vo);
        }

        return result;
    }

    private Map<String, List<SMSJob>> getProviderData(List<SMSJob> jobList,String providerId){
        Map<String, List<SMSJob>> listMap=new HashMap<>();
        for(SMSJob job:jobList){
            List<SMSJobDetail> jobDetails = job.getJobDetails();
            for (SMSJobDetail detail : jobDetails) {
                SMSProvider smsProvider = detail.getSmsProvider();
                if(smsProvider == null){
                    continue;
                }
                if(StringUtils.isBlank(providerId) || providerId.equals(smsProvider.getId())){
                    SMSJob copy=copySmsJob(job,detail);// because provider stay SmsDetail Inside, one. SmsDetail A new one SmsJob
                    String providerName = smsProvider.getProviderName();
                    List<SMSJob> smsJobs = listMap.get(providerName);
                    if(smsJobs==null){
                        smsJobs=new ArrayList<>();
                        listMap.put(providerName,smsJobs);
                    }
                    smsJobs.add(copy);
                }
            }
        }
        return listMap;
    }

    private SMSJob copySmsJob(SMSJob job,SMSJobDetail detail){
        SMSJob smsJob=new SMSJob();
        smsJob.setId(job.getId());
        smsJob.setSenderAccountId(job.getSenderAccountId());
        smsJob.setAnaApplication(job.getAnaApplication());
        smsJob.setContent(job.getContent());
        smsJob.setCreateDate(job.getCreateDate());
        smsJob.setOriginalJob(job.getOriginalJob());
        smsJob.setReferenceId(job.getReferenceId());
        smsJob.setSmsServiceProvider(job.getSmsProvider());
        List<SMSJobDetail> detailList=new ArrayList<>();
        detailList.add(detail);
        smsJob.setJobDetails(detailList);
        return smsJob;
    }

    @Override
    public ReportDto report(String docType, String searchBy, String startDate, String endDate, String sortBy, Boolean isAscending, String appCode) {
        ReportUtils.DocType type = ReportUtils.getEnumDocType(docType);
        String filePath;
        String fileName;
        String search = "";
        if(null != startDate && null != endDate && !startDate.isEmpty() && !endDate.isEmpty()){
            search = startDate + "-" + endDate;
        }else if(null != startDate && !startDate.isEmpty()){
            search = startDate + "-" + startDate;
        }else if(null != endDate && !endDate.isEmpty()){
            search = endDate + "-" + endDate;
        }
        if(searchBy.equals("sender")){
            filePath = "jasper/senderReport_"+docType+".jrxml";
            fileName = "Sender Report_" + search + "."+docType;
        }else if(searchBy.equals("monthly")){
            filePath = "jasper/monthlyReport_"+docType+".jrxml";
            String year = search.length()>4?search.substring(0, 4):"";
            fileName = "Monthly Report_"+ year +"."+docType;
        } else if(searchBy.equals("jobId")){
            filePath = "jasper/jobIdReport_"+docType+".jrxml";
            fileName = "JobId Report_"+ search +"."+docType;
        } else if(searchBy.equals("provider")){
            filePath = "jasper/providerReport_"+docType+".jrxml";
            fileName = "Provider Report_"+ search +"."+docType;
        } else{
            filePath = "jasper/dailyReport_"+docType+".jrxml";
            fileName = "Daily Report_"+ search +"."+docType;
        }
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filePath);
        List<ReportDataVo> list = getReportDataVos(search,appCode,searchBy);
        list = sortData(list, sortBy, isAscending);
        ReportDto dto = new ReportDto();
        dto.setContentType(ReportUtils.getContentType(type));
        dto.setContent(ReportUtils.report(type, in, list, null));
        dto.setFileName(fileName);
        return dto;
    }

    /**
     *  Returns the start date and the end date. 
     * @param date yyyyMMdd[-yyyyMMdd]
     * @return
     */
    private Date[] getSelectDate(String date) {
        Date[] dates = new Date[2];
        try {
            String[] split = date.split("-");
            if (split.length > 1) {
                dates[0] = sdf.parse(split[0] + " 00:00:00");
                dates[1] = sdf.parse(split[1] + " 23:59:59");
            } else {
                if (date.contains("-")) {
                    date = date.replace("-", "");
                }
                dates[0] = sdf.parse(date + " 00:00:00");
                dates[1] = sdf.parse(date + " 23:59:59");
            }
        } catch (ParseException e) {
            logger.error("ParseException ", e);
        } catch (Exception e) {
            logger.error("Exception ", e);
        }
        return dates;
    }

    /**
     *  Returns the start date and the end date. 
     * @param date yyyyMMdd
     * @return
     */
    private Date[] getMonthlyDate(String date) {
        Date[] dates = new Date[2];
        try {
            String month = date.substring(0, date.length() - 2);
            Date startDate = sdf.parse(month + "01" + " 00:00:00");
            int day = getDayOfMonth(startDate);
            Date endDate = sdf.parse(month + day + " 23:59:59");
            dates[0] = startDate;
            dates[1] = endDate;
        } catch (ParseException e) {
            logger.error("ParseException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return dates;
    }

   private Specifications<SMSJob> getWhere(String search, String appCode){
        Specifications<SMSJob> where = Specifications.where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("id")));
        if(null != search && !search.isEmpty()){
            try {
                Date startDate;
                Date endDate;
                String[] split = search.split("-");
                if(split.length>1){
                    startDate = sdf.parse(split[0] + " 00:00:00");
                    endDate = sdf.parse(split[1] + " 23:59:59");
                }else {
                    if(search.contains("-")) {
                        search = search.replace("-", "");
                    }
                    startDate = sdf.parse(search + " 00:00:00");
                    endDate = sdf.parse(search + " 23:59:59");
                }
                where = where.and(Specifications.where((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("createDate"), startDate, endDate)));
            } catch (ParseException e) {
                logger.error("ParseException",e);
            }
        }
       if(null != appCode && !appCode.isEmpty()){
           where = where.and(Specifications.where((root, criteriaQuery, criteriaBuilder) -> {
               Path<AnaApplication> anaApplicationPath = root.get("anaApplication");
               Path<String> codePath = anaApplicationPath.get("code");
               return criteriaBuilder.equal(codePath, appCode);
           }));
       }
        return where;
    }

}
