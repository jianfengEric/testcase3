package com.tng.portal.ana.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

/**
 * Created by Zero on 2016/11/10.
 */
public class AnaPrincipalAuthenticationProvider implements AuthenticationProvider, Serializable {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
