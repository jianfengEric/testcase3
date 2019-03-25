package com.tng.portal.ana.service.impl;


import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.authority.AnaAuthority;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.Principal;
import com.tng.portal.ana.bean.Role;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Zero on 2016/11/14.
 */
@Service("anaUserService")
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private AccountService accountService;

    /**
     * Get user detail info by token
     * @param token
     * @return
     *
     */
    @Override
    public UserDetails getUserDetailByToken(String token)   {
        Account accountInfo = accountService.getAuthuserInfoByToken(token);
        if(accountInfo == null){
            logger.error("get userinfo from ana server is error!");
            throw new BusinessException("Not Account found with user name : '" +  token + "'");
        }
        return new UserDetails(accountInfo);
    }

    /**
     * Get client account by role name
     * 
     * @param roleName
     * 			role name
     * 
     * @return
     * @throws Exception
     */
    @Override
    public List<Account> getClientAccountByRoleName(String roleName)  {
        List<Account> accounts = new ArrayList<>();
        if (roleName==null||"".equals(roleName)){
            return accounts;
        }
        RestfulResponse<List<Account>> response = accountService.getClientAccountByRoleName(roleName);
        if (response == null || !"success".equals(response.getStatus())){
        	logger.error("Get accounts by role fail!");
            throw new BusinessException("Get accounts by role : '" + roleName  + "'");
        }
        return response.getData();
    }

    /**
     * Get current login account
     * 
     * @return
     */
    @Override
    public String getCurrentAccount() {
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            Principal principal = (Principal)authentication.getPrincipal();
            if(principal!=null){
                return principal.getName();
            }
        }
        return null;
    }

    /**
     * Get all role of current user
     * 
     * @return
     */
    @Override
    public List<Role> getCurrentAccountRoles() {
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        List<Role> roles = new ArrayList<>();
        if(authentication!=null){
            List<AnaAuthority> anaAuthorities = authentication.getAuthorities();
            roles = anaAuthorities.stream().map(item-> item.getRole()).collect(Collectors.toList());
        }
        return roles;
    }

    /**
     * Get current account info
     * 
     * @return
     */
    @Override
    public Account getCurrentAccountInfo() {
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
             Account account =  authentication.getAccount();
             if(null!=account){
                 account.setPassword(null);
                 return account;
             }
        }
        return null;
    }

    /**
     * Get remote ip
     * 
     * @return
     */
    @Override
    public String getRemoteIp() {
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            return authentication.getIp();
        }
        return null;
    }

    /**
     * Get token of current user
     * 
     * @return
     */
    @Override
    public String getToken() {
        AnaPrincipalAuthenticationToken authentication =  (AnaPrincipalAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            return authentication.getToken();
        }
        return null;
    }

	@Override
	public String getLoginAccountId() {
		Account accountInfo = this.getCurrentAccountInfo();
        if (accountInfo == null) {
            return null;
        }
        return accountInfo.getAccountId();
	}


}
