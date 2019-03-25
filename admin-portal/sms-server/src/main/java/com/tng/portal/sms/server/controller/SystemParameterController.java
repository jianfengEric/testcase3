package com.tng.portal.sms.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.sms.server.entity.SystemParameter;
import com.tng.portal.sms.server.service.SystemParameterService;

@Controller
@RequestMapping("/systemParameter")
public class SystemParameterController {
	
	@Autowired
	private SystemParameterService systemParameterService;

	@RequestMapping(value = "/{paramCat}",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<SystemParameter>> listJobsByPage(@PathVariable(value = "paramCat") String paramCat) {
    	RestfulResponse<List<SystemParameter>> restResponse = new RestfulResponse<>();
    	List<SystemParameter> params = systemParameterService.getParamByCategory(paramCat);
    	restResponse.setData(params);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    
}
