package com.tng.portal.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/public")
public class SMSController {

	/**
	 * Health check
	 * @return
	 */
    @RequestMapping(value = "/healthCheck",method = RequestMethod.GET)
    public @ResponseBody String checkHealth(){
        return "OK";
    }
    
}
