package com.tng.portal.sms.controller;

import java.util.List;

import com.tng.portal.common.vo.sms.SMSProviderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.sms.entity.SMSServiceProvider;
import com.tng.portal.sms.service.SMSServiceProviderService;

@Controller
@RequestMapping("/smsServiceProvider")
public class SMSServiceProviderController {
	
	@Autowired
	private SMSServiceProviderService serviceProviderService;

	/**
	 * Query service provider list
	 * @return
	 */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @ResponseBody
	public RestfulResponse<List<SMSServiceProvider>> listSMSServiceProviders(){
		RestfulResponse<List<SMSServiceProvider>> restResponse = new RestfulResponse<>();
		List<SMSServiceProvider> serviceProviders = serviceProviderService.getSMSServiceProviderList();
		restResponse.setData(serviceProviders);
		restResponse.setSuccessStatus();
		return restResponse;
	}

    /**
	 * Query sms server provider list
	 * @return
	 */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<SMSProviderDto>> listSmsServiceProviders(@RequestParam(required = true) String applicationCode){
    	RestfulResponse<List<SMSProviderDto>> restResponse = new RestfulResponse<>();
    	List<SMSProviderDto> serviceProviders = serviceProviderService.getSMSServiceProviderList(applicationCode);
    	restResponse.setData(serviceProviders);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
}
