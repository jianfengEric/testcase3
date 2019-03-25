package com.tng.portal.ana.service;


import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.constant.TopupPermession;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaAccountAccessToken;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.PageQuery;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Zero on 2016/11/9.
 */
public interface AccountService {
    List<AnaAccount> getAccounts();

    @PreAuthorize("hasPermission('USER_ACCOUNT',1)")
    RestfulResponse<String> newCreateAccount(String ip, AddAccountPostDto postDto) ;

    @PreAuthorize("hasPermission('USER_ACCOUNT',64)")
    RestfulResponse<String> addAccountRoles(AddAccountRolePostDto postDto) ;

    @PreAuthorize("hasPermission('USER_ACCOUNT',128)")
    RestfulResponse<String> bindingAccount(BindingAccountPostDto postDto) ;


    @PreAuthorize("hasPermission('USER_ACCOUNT',2)")
    RestfulResponse<PageDatas> listAccounts(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending, String externalGroupIdSearch, String rolesSearch, String statusSearch,String nameSearch);

    Account getAuthUserInfoByUserAccount(String userAccount) ;
    Account getAuthUserInfoByAccountId(String accountId) ;
    @PreAuthorize("hasPermission('USER_ACCOUNT',2)")
    RestfulResponse<AccountDetailDto> getAccountDetail(String accountId) ;
    @PreAuthorize("hasPermission('USER_ACCOUNT',4)")
    RestfulResponse<String> updateAccount(String ip, AccountUpdateDto updateDto) ;
    @PreAuthorize("hasPermission('USER_ACCOUNT',8)")
    RestfulResponse<String> deleteAccount(String ip, String accountId) ;


    RestfulResponse<List<AccountDto>> getAccountByRoleName(String roleName, boolean exceptLoginAccount) ;

    RestfulResponse<List<Account>> getClientAccountByRoleName(String roleName) ;

    RestfulResponse<String> updateProfile(String ip, ProfileUpdateDto updateDto) ;
    @PreAuthorize("hasPermission('USER_ACCOUNT',256)")
    RestfulResponse<String> updateStatus(String ip, String id, String status) ;

    RestfulResponse<String> updatePassword(String ip, AccountUpPassDto passDto) ;

    AnaAccountAccessToken getAccountTokenByToken(String token);


    void updatePasswordByValidCode(ValidDto validDto) ;

    String logout(String accountId) ;
    void writeLoginOrLogoutLog(HttpServletRequest request, AnaAccount anaAccount, boolean isLogin);

    Account getAuthuserInfoByToken(String token) ;

    Boolean hasPermession(String accountId, TopupPermession permessionType) ;

    //added by Kaster 20170214
    RestfulResponse<PageDatas> getAccountByExternalGroup(String externalGroupId, Integer pageNo, Integer pageSize, String sortBy, String isAscending, String searchBy, String search) ;

    RestfulResponse<List<AccountDto>> queryAllAccounts();
    @PreAuthorize("hasPermission('USER_ACCOUNT',4)")
    RestfulResponse<String> editUser(String remoteHost, AccountUpdateDto updateDto);
    @PreAuthorize("hasPermission('USER_ACCOUNT',1024)")
    RestfulResponse reSendEmail(String accountId);
    
	Account getLocalAccountInfo(String applicationCode, String loginId) ;
	
	String getSSOAccountInfo(String accountId) ;
	
	String getSSOUserInfoByLocalUserAccountId(String accountId) ;
    @PreAuthorize("hasPermission('USER_ACCOUNT',512)")
    RestfulResponse rePassword(String accountId);
    RestfulResponse<AccountDto> resetPassword(String token, String newPwd);

    AnaAccount getAccount(String id);

    AnaAccountAccessTokenDto getToken(String token, String code);
	RestfulResponse<Boolean> checkFirstNameAndLastName(String firstName, String lastName, String accountId);
	RestfulResponse<AccountDto> activationAccount(String firstName,String lastName,String password,String code);
	public Boolean checkActivation(String code);
	
	public Boolean checkResetPwd(String code);

    RestfulResponse<com.tng.portal.common.vo.PageDatas> listAccounts(PageQuery<AnaAccount> pageQuery);

    String getAccountName(String id);
}
