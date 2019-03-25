package com.tng.portal.ana.service;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.Role;
import com.tng.portal.ana.bean.UserDetails;

import java.util.List;

/**
 * Created by Zero on 2016/11/14.
 */
public interface UserService {
    UserDetails getUserDetailByToken(String token);
    List<Account> getClientAccountByRoleName(String roleName);

    String getCurrentAccount();

    List<Role> getCurrentAccountRoles();

    Account getCurrentAccountInfo();

    String getRemoteIp();

    String getToken();
    
    String getLoginAccountId();

}
