package com.tng.portal.ana.controller;

import com.tng.portal.ana.service.LoginService;
import com.tng.portal.ana.vo.LoginDto;
import com.tng.portal.ana.vo.LoginRestDto;
import com.tng.portal.ana.vo.RefreshTokenDto;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Zero on 2016/11/10.
 */
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;
    @Resource
    public HttpServletRequest request;

    /**
     * User login the ANA system
     * @param loginDto login info include username, password
     * @return
     */
    @ApiOperation(value="User login the ANA system", notes="")
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    RestfulResponse<LoginRestDto> login(@ApiParam(value="login dto")@RequestBody LoginDto loginDto){
        RestfulResponse<LoginRestDto> restResponse = new RestfulResponse<>();
        restResponse.setSuccessStatus();
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if(StringUtils.isBlank(username)){
            throw new BusinessException(102006,"user name is empty!");
        }
        if(StringUtils.isBlank(username)){
            throw new BusinessException(102006,"password is empty!");
        }
        LoginRestDto dto = loginService.login(request,username,password, loginDto.getApplicationCode());
        restResponse.setData(dto);
        return restResponse;
    }


    /**
     * There is a counter in front end ,when front end send a request to back-end, front end will calculate whether the session time remains 2 minutes,
     * If yes, front end will call this function to refresh current user's token info, if not, do nothing
     * If the front end call back-end after session expire, it will redirect to login page
     * This function will return new token and new session time to front end.
     * @param request
     * @return 
     *
     */
    @ApiOperation(value="Refresh current user's token info, will return new token and new session time to front end", notes="")
    @RequestMapping(value = "/refeshToken",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<RefreshTokenDto> refreshToken(HttpServletRequest request)  {
        String token = request.getHeader("token");
        RestfulResponse<RefreshTokenDto> restResponse = new RestfulResponse<>();
        if(null==token){
            throw new BusinessException(SystemMsg.ServerErrorMsg.REFRESH_TOKEN_FAILED.getErrorCode());
        }
        RefreshTokenDto refreshTokenDto = loginService.refreshToken(token);
        if(null==refreshTokenDto){
            throw new BusinessException(SystemMsg.ServerErrorMsg.REFRESH_TOKEN_FAILED.getErrorCode());
        }
        restResponse.setData(refreshTokenDto);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @ApiOperation(value="login check", notes="")
    @RequestMapping(value = "/loginCheck",method = RequestMethod.POST)
    public @ResponseBody RestfulResponse<String> loginCheck(@ApiParam(value="login check dto")@RequestBody LoginDto LoginDto){
       return loginService.loginCheck(LoginDto);
    }
}
