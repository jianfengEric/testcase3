package com.tng.portal.ana.service;

import com.tng.portal.ana.vo.*;
import com.tng.portal.common.vo.rest.RestfulResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Zero on 2016/11/14.
 */
public interface LoginService {
    LoginRestDto login(HttpServletRequest request, String username, String password, String applicationCode) ;

    RefreshTokenDto refreshToken(String token);

    void syncToken(AccountAccessTokenDto token);

    RestfulResponse<String> loginCheck(LoginDto loginDto);

}
