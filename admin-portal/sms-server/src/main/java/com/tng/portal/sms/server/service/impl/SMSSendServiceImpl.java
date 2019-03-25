package com.tng.portal.sms.server.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.util.CoderUtil;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.sms.server.constant.SMSStatus;
import com.tng.portal.sms.server.entity.SMSProvider;
import com.tng.portal.sms.server.repository.SMSProviderRepository;
import com.tng.portal.sms.server.service.SMSSendService;
import com.tng.portal.sms.server.vo.SendSMSResponseVo;

@Service
public class SMSSendServiceImpl implements SMSSendService{
    private static final Logger logger = LoggerFactory.getLogger(SMSSendServiceImpl.class);

	@Autowired
	@Qualifier("httpClientUtils")
	private HttpClientUtils httpClientUtils;
	
    @Autowired
    private AnaApplicationRepository applicationRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private SMSProviderRepository smsProviderRepository;
    
	@Override
	public SendSMSResponseVo sendSMS(String providerId, String to, String smsText) {
		SendSMSResponseVo vo = new SendSMSResponseVo();
		String responseStr = "";
		Map<String, String> param = new HashMap<>();
		SMSProvider smsProvider = smsProviderRepository.findOne(providerId);
		vo.setSmsProvider(smsProvider);
		switch(providerId){
			case "MACROKIOSK" :
				param.put("User", CoderUtil.decrypt(smsProvider.getUsername()));
				param.put("Pass", CoderUtil.decrypt(smsProvider.getPassword()));
				param.put("ServID", "SIN001");
				param.put("From", "TNG");
				param.put("To", to);
				param.put("Type", "0");
				param.put("text", smsText);
				try {
					responseStr = httpClientUtils.postObjectForString(smsProvider.getEndpointUrl(),String.class,param);
				} catch (Exception e) {
					logger.error("providerId:{} to:{} smsText:{}",providerId,to,smsText,e);
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setErrorMsg(e.getMessage());
					return vo;
				}
				if(responseStr == null || responseStr.split(",").length != 3 || !responseStr.split(",")[2].contains("200")){
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setRspMsg(responseStr);
					return vo;
				}
				vo.setStatus(SMSStatus.SUCCESS.getDesc());
				vo.setRspMsg(responseStr);
				break;
				
			case "M800" :
				param.put("Username", CoderUtil.decrypt(smsProvider.getUsername()));
				param.put("Password", CoderUtil.decrypt(smsProvider.getPassword()));
				param.put("SrcTON", "5");
				param.put("SrcNPI", "0");
				param.put("SrcID", "TNG");
				param.put("DestTON", "1");
				param.put("DestNPI", "1");
				param.put("Mobile", to);
				param.put("DCS", "1");
				param.put("LongMsg", "0");
				param.put("MsgText", smsText);
				try {
					responseStr = httpClientUtils.postObjectForString(smsProvider.getEndpointUrl(),String.class,param);
				} catch (Exception e) {
					logger.error("providerId:{} to:{} smsText:{}",providerId,to,smsText,e);
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setErrorMsg(e.getMessage());
					return vo;
				}
				if(responseStr == null || !responseStr.contains("Message sent successfully")){
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setRspMsg(responseStr);
					return vo;
				}
				vo.setStatus(SMSStatus.SUCCESS.getDesc());
				vo.setRspMsg(responseStr);
				break;
				
			case "ACCESSU" :
				param.put("accountno", "11029691");
				param.put("user", CoderUtil.decrypt(smsProvider.getUsername()));
				param.put("pwd", CoderUtil.decrypt(smsProvider.getPassword()));
				param.put("phone", to);
				param.put("msg", smsText);
				try {
					responseStr = httpClientUtils.httpGet(smsProvider.getEndpointUrl(),String.class,param);
				} catch (Exception e) {
					logger.error("providerId:{} to:{} smsText:{}",providerId,to,smsText,e);
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setErrorMsg(e.getMessage());
					return vo;
				}
				if(responseStr == null || !responseStr.contains("<msg_status>100")){
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setRspMsg(responseStr);
					return vo;
				}
				vo.setStatus(SMSStatus.SUCCESS.getDesc());
				vo.setRspMsg(responseStr);
				break;
				
			case "TNG" :
				param.put("operator", "cu");
				param.put("phone_no", to);
				param.put("msg", smsText);
				try {
					responseStr = httpClientUtils.httpGet(smsProvider.getEndpointUrl(),String.class,param);
				} catch (Exception e) {
					logger.error("providerId:{} to:{} smsText:{}",providerId,to,smsText,e);
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setErrorMsg(e.getMessage());
					return vo;
				}
				if(responseStr == null || !responseStr.contains("OK")){
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setRspMsg(responseStr);
					return vo;
				}
				vo.setStatus(SMSStatus.SUCCESS.getDesc());
				vo.setRspMsg(responseStr);
				break;
				
			case "NEXMO" :
				param.put("api_key", "TnGAsia");
				param.put("api_secret", "hfj7F3d5");
				param.put("from", "9753");
				param.put("to", to);
				param.put("type", "ascii");
				param.put("text", smsText);
				try {
					responseStr = httpClientUtils.postObjectForString(smsProvider.getEndpointUrl(),String.class,param);
				} catch (Exception e) {
					logger.error("providerId:{} to:{} smsText:{}",providerId,to,smsText,e);
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setErrorMsg(e.getMessage());
					return vo;
				}
				if(responseStr == null || !responseStr.contains("\"status\": \"0\"")){
					vo.setStatus(SMSStatus.FAIL.getDesc());
					vo.setRspMsg(responseStr);
					return vo;
				}
				vo.setStatus(SMSStatus.SUCCESS.getDesc());
				vo.setRspMsg(responseStr);
				break;
		}
		return vo;
	}
	
	@Override
	public SendSMSResponseVo sendVirtualSMS(String providerId, String to, String smsText) {
		SendSMSResponseVo vo = new SendSMSResponseVo();
		String responseStr = "";
		Map<String, String> param = new HashMap<>();
		SMSProvider smsProvider = smsProviderRepository.findOne(providerId);
		vo.setSmsProvider(smsProvider);
		AnaApplication anaApplication = applicationRepository.findByCode("LOG");
		if(anaApplication == null){
			vo.setStatus(SMSStatus.FAIL.getDesc());
			vo.setErrorMsg("Log uri not set");
			return vo;
		}
		param.put("mobileNumber", to);
		param.put("content", smsText);
		try {
			responseStr = restTemplate.postForObject(anaApplication.getInternalEndpoint()+"/smsVirtualServer/send", null, String.class, param);
		} catch (Exception e) {
			logger.error("providerId:{} to:{} smsText:{}",providerId,to,smsText,e);
			vo.setStatus(SMSStatus.FAIL.getDesc());
			vo.setErrorMsg(e.getMessage());
			return vo;
		}
		if(!"OK".equals(responseStr)){
			vo.setStatus(SMSStatus.FAIL.getDesc());
			vo.setRspMsg(responseStr);
			return vo;
		}
		vo.setStatus(SMSStatus.SUCCESS.getDesc());
		vo.setRspMsg(responseStr);
		return vo;
	}
	
}