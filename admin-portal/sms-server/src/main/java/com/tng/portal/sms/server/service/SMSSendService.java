package com.tng.portal.sms.server.service;

import com.tng.portal.sms.server.vo.SendSMSResponseVo;

public interface SMSSendService {

	SendSMSResponseVo sendSMS(String providerName, String to, String smsText);

	SendSMSResponseVo sendVirtualSMS(String id, String mobileNumber, String content);

}
