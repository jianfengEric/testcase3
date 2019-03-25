package com.tng.portal.ana.controller;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.vo.AccountUsernameDto;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Zero on 2016/11/14.
 */
@RestController
@RequestMapping("oauth")
public class AuthValidController {
    @Autowired
    private AccountService accountService;
    
    /**
     * Query current user's authentication info by username
     *
     * @param usernameDto
     * 			username
     *
     * @return

     */
    @ApiOperation(value="Query current user's authentication info by username", notes="")
    @RequestMapping(value = "username",method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse<Account> authUserInfo(@ApiParam(value="username dto")@RequestBody(required = true) AccountUsernameDto usernameDto) {
        if(null==usernameDto){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String username = usernameDto.getUsername();
        String accountId = usernameDto.getAccountId();
        RestfulResponse<Account> restResponse = new RestfulResponse<>();
        Account account = null;
        if(null != username && !username.isEmpty()){
           account =  accountService.getAuthUserInfoByUserAccount(username);
        }else if(null != accountId && !accountId.isEmpty()){
            account =  accountService.getAuthUserInfoByAccountId(accountId);
        }
        restResponse.setData(account);
        restResponse.setSuccessStatus();
        return  restResponse;
    }

    /**
     * Query user's authentication info by token
     * 
     * @param token
     * 			user token 
     * 
     * @return
     * @
     */
    @ApiOperation(value="Query user's authentication info by token", notes="")
    @RequestMapping(method = RequestMethod.GET)
    Account authUserInfoByToken(@ApiParam(value="token")@RequestHeader(name = "token") String token)  {
        if(null==token){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        return accountService.getAuthuserInfoByToken(token);
    }

    /**
     * Query account list by role name string, separated by comma
     * 
     * @param roleName
     * 			the role name string, separated by commas
     * 
     * @return
     * @
     */
    @ApiOperation(value="Query account list by role name string, separated by comma", notes="")
    @RequestMapping(value = "roleAcc",method = RequestMethod.GET)
    public @ResponseBody
    RestfulResponse<List<Account>> roleAcc(@ApiParam(value="roleName")@RequestParam("roleName") String roleName)  {
        return accountService.getClientAccountByRoleName(roleName);
    }
    
    /**
     * Query sso account info by local username
     *
     * @param usernameDto
     * 			username
     *
     * @return
     * @
     */
    @ApiOperation(value="Query sso account info by local username", notes="")
    @RequestMapping(value = "ssoAccount",method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse<String> querySSOAccount(@ApiParam(value="username dto")@RequestBody(required = true) AccountUsernameDto usernameDto) {
    	RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null==usernameDto){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String accountId = usernameDto.getAccountId();
        String account = accountService.getSSOUserInfoByLocalUserAccountId(accountId);
        restResponse.setData(account);
        restResponse.setSuccessStatus();
        return  restResponse;
    }
}
