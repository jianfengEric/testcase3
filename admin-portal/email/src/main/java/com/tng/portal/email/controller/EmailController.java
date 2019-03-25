package com.tng.portal.email.controller;

import com.tng.portal.common.vo.email.EMailInput;
import com.tng.portal.common.vo.rest.RestfulResponse;
import com.tng.portal.email.service.EmailAccountService;
import com.tng.portal.email.service.EmailService;
import com.tng.portal.email.vo.EmailAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("remote")
public class EmailController {


    @Autowired
    @Qualifier("emailService")
    private EmailService emailService;

    @Autowired
    private EmailAccountService emailAccountService;


    @RequestMapping(value = "getEmailAccount", method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<EmailAccountDto> getEmailAccount(String hostCode,Long hostSizeLimit){
        RestfulResponse restResponse = new RestfulResponse<>();
        restResponse.setData(emailService.getEmailAccount(hostCode,hostSizeLimit));
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    public @ResponseBody RestfulResponse sendEmail(@RequestBody EMailInput emailInput){
        RestfulResponse restResponse = new RestfulResponse<>();
        emailService.sendEmail(emailInput);
        restResponse.setSuccessStatus();
        return restResponse;
    }

}
