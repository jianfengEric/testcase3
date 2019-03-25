package com.tng.portal.ana.service;

import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.vo.AnaAccountAccessTokenDto;

public interface TokenService {
    void syncToken(AnaAccountAccessTokenDto anaAccountAccessTokenDto);

    void clearToken(AnaAccountAccessTokenDto anaAccountAccessTokenDto);

    UserDetails updateToken(UserDetails userDetails, String accessToken);

    UserDetails updateClientToken(UserDetails userDetails, String accessToken);
}
