package com.tng.portal.sms.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.common.vo.wfl.request.SMSJobInputVo;
import com.tng.portal.sms.server.service.SMSJobService;

@Controller
@RequestMapping("/job")
public class SMSJobController {
	
	@Autowired
	private SMSJobService jobService;

    /**
	 * add sms job 
	 * 
	 * @param vo
	 * 			sms job info
	 * 
	 * @return
	 */
    @RequestMapping(value = "",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<Boolean> addJob(@RequestBody SMSJobInputVo vo) {
    	RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
    	try{
    		jobService.addJob(vo);
    	} catch(Exception e){
	        restResponse.setFailStatus();
	        return restResponse;
		}
    	restResponse.setData(true);
        restResponse.setSuccessStatus();
        return restResponse;
    }
}
