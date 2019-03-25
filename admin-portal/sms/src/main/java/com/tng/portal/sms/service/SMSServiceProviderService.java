package com.tng.portal.sms.service;

import java.util.List;

import com.tng.portal.common.vo.sms.SMSProviderDto;
import com.tng.portal.sms.entity.SMSServiceProvider;

public interface SMSServiceProviderService {

	List<SMSServiceProvider> getSMSServiceProviderList();

	List<SMSProviderDto> getSMSServiceProviderList(String appCode);

}
