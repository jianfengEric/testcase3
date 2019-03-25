package com.tng.portal.sms.server.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.vo.sms.SMSProviderDto;
import com.tng.portal.sms.server.constant.SMSServiceApplicationStatus;
import com.tng.portal.sms.server.constant.SMSStatus;
import com.tng.portal.sms.server.constant.SystemMsg;
import com.tng.portal.sms.server.entity.SMSProvider;
import com.tng.portal.sms.server.entity.SMSServiceApplication;
import com.tng.portal.sms.server.entity.SMSServiceApplicationPk;
import com.tng.portal.sms.server.repository.JobDetailRepository;
import com.tng.portal.sms.server.repository.SMSProviderRepository;
import com.tng.portal.sms.server.repository.SMSServiceApplicationRepository;
import com.tng.portal.sms.server.service.SMSProviderService;
import com.tng.portal.sms.server.service.SMSSendService;
import com.tng.portal.sms.server.util.AccountUtil;
import com.tng.portal.sms.server.vo.AccountDto;
import com.tng.portal.sms.server.vo.SMSServiceApplicationDto;

@Service
public class SMSProviderServiceImpl implements SMSProviderService{
    @Autowired
	private SMSServiceApplicationRepository smsServiceApplicationRepository;
    
    @Autowired
    private AnaApplicationRepository anaApplicationRepository;
    
    @Autowired
    private SMSProviderRepository smsProviderRepository;
    
    @Autowired
    private SMSSendService smsSendService;
    
	@Qualifier("anaUserService")
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobDetailRepository jobDetailRepository;
	
	/**
	 * Query service provider list 
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public List<SMSProviderDto> getSMSProviderList(String applicationCode) {
		Sort sort = new Sort(Direction.ASC, "priority");
		Specification<SMSServiceApplication> querySpecifi = new Specification<SMSServiceApplication>() {  
            @Override  
            public Predicate toPredicate(Root<SMSServiceApplication> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {  
                List<Predicate> predicates = new ArrayList<>();  
                if (StringUtils.isNotBlank(applicationCode)){
                	Join<SMSServiceApplication, AnaApplication> join = root.join("anaApplication", JoinType.LEFT);
                	predicates.add(cb.like(join.get("code").as(String.class), "%" + applicationCode + "%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));  
            }  
        };  
        
        List<SMSServiceApplication> smsServiceApplications = this.smsServiceApplicationRepository.findAll(querySpecifi, sort);
        
        List<SMSProvider> providerList = this.smsProviderRepository.findAll();
        if(smsServiceApplications != null){
        	for(SMSServiceApplication serviceApplication : smsServiceApplications){
        		if(providerList.contains(serviceApplication.getSmsProvider())){
        			providerList.remove(serviceApplication.getSmsProvider());
        		}
        	}
        }

        int priority =0;
        if(null != smsServiceApplications){//sonar modify 
        	priority = smsServiceApplications.size();
			for(SMSProvider smsProvider : providerList){
				SMSServiceApplication serviceApplication = new SMSServiceApplication();
				SMSServiceApplicationPk pk = new SMSServiceApplicationPk();
				pk.setApplicationCode(applicationCode);
				pk.setSmsProviderId(smsProvider.getId());
				serviceApplication.setSmsServiceApplicationPk(pk);
				serviceApplication.setStatus(SMSServiceApplicationStatus.NACT.getDesc());
				serviceApplication.setPriority(++priority);
				serviceApplication.setAnaApplication(this.anaApplicationRepository.findOne(applicationCode));
				serviceApplication.setSmsProvider(smsProvider);
				this.smsServiceApplicationRepository.save(serviceApplication);

				smsServiceApplications.add(serviceApplication);
			}

			return smsServiceApplications.stream().map(item -> {
				SMSProviderDto vo = new SMSProviderDto();
				SMSProvider sp = item.getSmsProvider();
				vo.setId(sp.getId());
				vo.setProviderName(sp.getProviderName());
				vo.setHttpMethod(sp.getHttpMethod());
				vo.setLongSMS(sp.isLongSMS());
				vo.setSpecialCharacter(sp.isSpecialCharacter());
				vo.setSendForeignCountry(sp.isSendForeignCountry());
				vo.setEndpointUrl(sp.getEndpointUrl());
				vo.setUsername(sp.getUsername());
				vo.setPassword(sp.getPassword());
				vo.setStatus(item.getStatus());
				vo.setPriority(item.getPriority());
				vo.setApplicationCode(item.getAnaApplication().getCode());
				vo.setLastUsedTime(jobDetailRepository.findLastUsedBySmsProvider(sp.getId()));

				if(SMSServiceApplicationStatus.NACT.getDesc().equals(item.getStatus())){
					vo.setHealthStatus(SMSStatus.FAIL.getDesc());
				}else{
					vo.setHealthStatus(SMSStatus.SUCCESS.getDesc());
				}

				return vo;
			}).collect(Collectors.toList());
		}
        return null;
	}

	/**
	 * Query service provider health status 
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public String querySMSProviderStatus(String smsProviderId) {
		SMSServiceApplication smsServiceApplication = smsServiceApplicationRepository.findBySmsProvider(smsProviderRepository.findOne(smsProviderId));
		if(SMSServiceApplicationStatus.NACT.getDesc().equals(smsServiceApplication.getStatus())){
			return SMSStatus.FAIL.getDesc();
		}else{
			return SMSStatus.SUCCESS.getDesc();
		}
	}

	/**
	 * change sms provider status
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public void changeSMSProviderStatus(String applicationCode, String smsProviderId, String status){
		SMSServiceApplication serviceApplication = this.smsServiceApplicationRepository.findByAnaApplicationAndSmsProvider(
				this.anaApplicationRepository.findOne(applicationCode), this.smsProviderRepository.findOne(smsProviderId));
		if(serviceApplication == null){
			throw new BusinessException(SystemMsg.ServerErrorMsg.SMS_SERVICE_APPLICATION_NOT_EXISTS_ERROR.getErrorCode());
		}
		serviceApplication.setStatus(status);
		this.smsServiceApplicationRepository.save(serviceApplication);
	}

	/**
	 * update sms provider
	 * 
	 * @return
	 */
	@Override
	@Transactional
	public String updateSMSProvider(SMSProviderDto dto) {
		AccountDto accountDto = AccountUtil.extractAccountInfo(userService.getCurrentAccountInfo());
		SMSProvider sp = this.smsProviderRepository.findOne(dto.getId());
		sp.setLongSMS(dto.isLongSMS());
		sp.setSpecialCharacter(dto.isSpecialCharacter());
		sp.setSendForeignCountry(dto.isSendForeignCountry());
		sp.setHttpMethod(dto.getHttpMethod());
		sp.setEndpointUrl(dto.getEndpointUrl());
		sp.setUsername(dto.getUsername());
		sp.setPassword(dto.getPassword());
		sp.setUpdateDate(new Date());
		sp.setUpdateBy(accountDto.getName());
		return this.smsProviderRepository.save(sp).getId();
	}

	@Override
	@Transactional
	public void changePriority(List<SMSServiceApplicationDto> dtos){
		if(dtos != null){
			for(SMSServiceApplicationDto dto : dtos){
				SMSServiceApplication serviceApplication = this.smsServiceApplicationRepository.findByAnaApplicationAndSmsProvider(
						this.anaApplicationRepository.findOne(dto.getApplicationCode()), this.smsProviderRepository.findOne(dto.getSmsProviderId()));
				if(serviceApplication == null){
					throw new BusinessException(SystemMsg.ServerErrorMsg.SMS_SERVICE_APPLICATION_NOT_EXISTS_ERROR.getErrorCode());
				}
				serviceApplication.setPriority(dto.getPriority());
				this.smsServiceApplicationRepository.save(serviceApplication);
			}
		}
	}
    
}