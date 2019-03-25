package com.tng.portal.ana.bean;


import com.tng.portal.ana.authority.AnaAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zero on 2016/11/10.
 */
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private Account account;

    public UserDetails(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public List<AnaAuthority> getAuthorities() {
        List<AnaAuthority> grantedAuthorities = new ArrayList<>();
        List<Role> roles = account.getRoles();
        if(null!=roles && !roles.isEmpty()){
            for (Role role:roles){
                AnaAuthority authority = new AnaAuthority(role);
                grantedAuthorities.add(authority);
            }
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return account.getEnabled();
    }
}
