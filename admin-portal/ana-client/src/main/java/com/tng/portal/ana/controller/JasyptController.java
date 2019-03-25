package com.tng.portal.ana.controller;


import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Zero on 2016/11/8.
 */
@RestController
@RequestMapping("jasypt")
public class JasyptController {

    @ApiOperation(value="encrypt", notes="")
    @RequestMapping(value = "encrypt",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> encrypt(@RequestParam(required = true) String password){

        RestfulResponse<String> restResponse = new RestfulResponse<>();
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("tng");
        restResponse.setData(textEncryptor.encrypt(password));
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @ApiOperation(value="decrypt", notes="")
    @RequestMapping(value = "decrypt",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<String> decrypt(@RequestParam(required = true) String password){

        RestfulResponse<String> restResponse = new RestfulResponse<>();
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("tng");
        restResponse.setData(textEncryptor.decrypt(password));
        restResponse.setSuccessStatus();
        return restResponse;
    }

}