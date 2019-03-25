package com.tng.portal.ana.controller;


import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.vo.ValidDto;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Zero on 2016/11/29.
 */
@RestController
@RequestMapping("/validCode")
public class ForgotPasswordController {

    @Autowired
    private AccountService accountService;

    /**
     * Valid and update user password
     * @param validDto include valid code, new password
     * @return
     *
     */
    @ApiOperation(value="Valid and update user password", notes="")
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse validAndUpdatePassword(@ApiParam(value="valid dto")@RequestBody ValidDto validDto)  {
        accountService.updatePasswordByValidCode(validDto);
        RestfulResponse restResponse = new RestfulResponse();
        restResponse.setSuccessStatus();
        return restResponse;
    }

}
