package com.tng.portal.sms.server.service;

import java.util.List;

import com.tng.portal.common.vo.sms.SMSProviderDto;
import com.tng.portal.sms.server.vo.SMSServiceApplicationDto;

public interface SMSProviderService {

	List<SMSProviderDto> getSMSProviderList(String applicationCode);

	String querySMSProviderStatus(String smsProviderId);

	void changeSMSProviderStatus(String applicationCode, String smsProviderId, String status);

	String updateSMSProvider(SMSProviderDto dto);

	void changePriority(List<SMSServiceApplicationDto> dtos);

}
