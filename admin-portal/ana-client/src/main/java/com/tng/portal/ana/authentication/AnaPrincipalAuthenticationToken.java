package com.tng.portal.ana.authentication;


import com.tng.portal.ana.authority.AnaAuthority;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.Principal;
import com.tng.portal.ana.bean.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zero on 2016/11/14.
 */
public class AnaPrincipalAuthenticationToken implements Authentication,CredentialsContainer {

    private Object details;
    private boolean authenticated;
    private final Principal principal;
    private final List<AnaAuthority> authorities;
    private Account account;
    private String token;
    private String ip;

    public AnaPrincipalAuthenticationToken(String userName,Object credentials){
        this.principal = new Principal(userName,credentials);
        this.authorities = new ArrayList<>();
    }

    public AnaPrincipalAuthenticationToken(Principal principal){
        this.principal = principal;
        this.authenticated = false;
        this.authorities = new ArrayList<>();
    }

    public AnaPrincipalAuthenticationToken(UserDetails userDetail){
        this.principal = new Principal(userDetail.getUsername());
        this.authenticated = true;
        this.authorities = userDetail.getAuthorities();
        this.account = userDetail.getAccount();
    }


    public AnaPrincipalAuthenticationToken(UserDetails userDetail,String token){
        this.principal = new Principal(userDetail.getUsername());
        this.authenticated = true;
        this.authorities = userDetail.getAuthorities();
        this.account = userDetail.getAccount();
        this.token = token;
    }

    public AnaPrincipalAuthenticationToken(UserDetails userDetail,String token,String ip){
        this.principal = new Principal(userDetail.getUsername());
        this.authenticated = true;
        this.authorities = userDetail.getAuthorities();
        this.account = userDetail.getAccount();
        this.token = token;
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public List<AnaAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return principal.getCredentials();
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        if(this.authenticated){
            throw new IllegalArgumentException("Token already authenticated");
        }else{
            authenticated = false;
        }
    }

    @Override
    public void eraseCredentials() {
        principal.setCredentials(null);
    }

    @Override
    public String getName() {
        return principal.getName();
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object object){
        if(object == this){
            return true;
        }

        if(object == null){
            return false;
        }

        if(!object.getClass().equals(this.getClass())){
            return false;
        }

        AnaPrincipalAuthenticationToken obj = (AnaPrincipalAuthenticationToken)object;

        return this.principal.equals(obj.getPrincipal()) && (this.authenticated == obj.isAuthenticated());//sonarmodify
    }

    @Override
    public int hashCode(){
        int prime = 31;
        int result = 7;

        if(principal != null){
            result = result * prime + principal.hashCode();
        }
        if(authenticated){
            result = result * prime + 1;
        }
        return result;
    }
}
