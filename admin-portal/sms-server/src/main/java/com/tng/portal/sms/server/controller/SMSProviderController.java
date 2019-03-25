package com.tng.portal.sms.server.controller;

import java.util.List;

import com.tng.portal.common.vo.sms.SMSProviderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.sms.server.service.SMSProviderService;
import com.tng.portal.sms.server.vo.SMSServiceApplicationDto;

@Controller
@RequestMapping("/smsProvider")
public class SMSProviderController {
	
	@Autowired
	private SMSProviderService smsProviderService;

	/**
	 * Query service provider list 
	 * 
	 * @return
	 */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<SMSProviderDto>> listSMSProviders(@RequestParam(required = true) String applicationCode){
    	RestfulResponse<List<SMSProviderDto>> restResponse = new RestfulResponse<>();
    	List<SMSProviderDto> serviceProviders = smsProviderService.getSMSProviderList(applicationCode);
    	restResponse.setData(serviceProviders);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    /**
	 * Query service provider health status 
	 * 
	 * @return
	 */
    @RequestMapping(value = "smsProviderStatus",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> querySMSProviderStatus(@RequestParam(required = true) String smsProviderId){
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	String status = smsProviderService.querySMSProviderStatus(smsProviderId);
    	restResponse.setData(status);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    /**
	 * change sms provider status
	 * 
	 * @return
	 */
    @RequestMapping(value = "changeSMSProviderStatus",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<Boolean> changeSMSProviderStatus(@RequestParam(required = true) String applicationCode,
    		@RequestParam(required = true) String smsProviderId,
    		@RequestParam(required = true) String status){
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	smsProviderService.changeSMSProviderStatus(applicationCode, smsProviderId, status);
    	restResponse.setData(true);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    /**
	 * update sms provider
	 * 
	 * @return
	 */
    @RequestMapping(value = "",method = RequestMethod.PUT)
    @ResponseBody
    public RestfulResponse<String> update(@RequestBody SMSProviderDto dto){
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
    	String providerId = smsProviderService.updateSMSProvider(dto);
    	restResponse.setData(providerId);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
    /**
	 * change sms provider priority
	 * 
	 * @return
	 */
    @RequestMapping(value = "priority",method = RequestMethod.PUT)
    @ResponseBody
    public RestfulResponse<Boolean> changePriority(@RequestBody List<SMSServiceApplicationDto> dtos){
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	smsProviderService.changePriority(dtos);
    	restResponse.setData(true);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
}
