package com.tng.portal.sms.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Owen on 2016/12/16.
 */
@RestController
@RequestMapping("public")
public class PublicController {
    @RequestMapping(value = "healthCheck",method = RequestMethod.GET)
    public @ResponseBody String checkHealth(){
        return "OK";
    }
}
