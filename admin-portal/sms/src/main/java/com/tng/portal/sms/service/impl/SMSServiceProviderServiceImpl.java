package com.tng.portal.sms.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.sms.SMSProviderDto;
import com.tng.portal.sms.entity.SMSServiceProvider;
import com.tng.portal.sms.repository.SMSServiceProviderRepository;
import com.tng.portal.sms.service.SMSServiceProviderService;

@Service
public class SMSServiceProviderServiceImpl implements SMSServiceProviderService{
    private static final Logger logger = LoggerFactory.getLogger(SMSServiceProviderServiceImpl.class);
	
    @Autowired
	private SMSServiceProviderRepository serviceProviderRepository;

	@Autowired
	@Qualifier("httpClientUtils")
	private HttpClientUtils httpClientUtils;

	@Autowired
	private UserService userService;

	@Override
	public List<SMSServiceProvider> getSMSServiceProviderList() {
		return this.serviceProviderRepository.findAll();
	}

	@Override
	public List<SMSProviderDto> getSMSServiceProviderList(String appCode) {
		Map<String, String> pram = new HashMap<>();
		pram.put("applicationCode", appCode);
		List<SMSProviderDto> smsProviderDtoList=new ArrayList<>();
		try {
			RestfulResponse<List<SMSProviderDto>> restfulResponse = httpClientUtils.httpGet(
					PropertiesUtil.getAppValueByKey("sms.server.provider.api"),
					new ParameterizedTypeReference<RestfulResponse<List<SMSProviderDto>>>() {},
					pram,userService.getToken());
			if(restfulResponse!=null && restfulResponse.hasSuccessful()){
				smsProviderDtoList = restfulResponse.getData();
			}
		} catch (Exception e) {
			logger.error("Exception",e);
		}

		return smsProviderDtoList;
	}
    
}