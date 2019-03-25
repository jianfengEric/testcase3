package com.tng.portal.ana.service;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by Roger on 2017/12/4.
 */
public interface RemoteAccountService {

    RestfulResponse active(String id);

    RestfulResponse<Map<String, List<String>>> createAndBindAnaAccount(String remoteHost, MamAddAccountPostDto postDto);

    RestfulResponse<String> getSsoAccount(String id);

    RestfulResponse bindAccount(MamAddAccountPostDto postDto);

    RestfulResponse<Account> getAccountInfoByAccount(String account);

    RestfulResponse<String> addSsoAccount(String remoteHost, AddAccountPostDto postDto);

    RestfulResponse<String> updateLocalAccountInfo(String remoteHost, AccountUpdateDto updateDto);

    RestfulResponse<String> updateClientStatus(String remoteHost, String id, String status, String code);

    RestfulResponse<String> updateClientAccountInfo(String remoteHost, AccountUpdateDto updateDto);

    RestfulResponse resendClientEmail(String accountId, String code);

    RestfulResponse resetPassword(String id, String password, String code);

    RestfulResponse<List<AnaAccountApplicationViewDto>> getBindAccounts(String id);

    RestfulResponse<Account> getAccountInfoByBindAccountId(String id);

    RestfulResponse<List<AccountDto>> queryAccountList(AccountQueryPostDto postDto);

    RestfulResponse<PageDatas> listAccountsWithoutSso(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending, String mid, String accountId, String applicationCode);

    RestfulResponse<PageDatas> listAccounts(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending, String mid, String accountId);

    RestfulResponse<String> merchantManagementBindingSso(String remoteHost, MamAddAccountPostDto postDto);

    RestfulResponse<Account> getAccountInfoByNameOrId(AccountUsernameDto accountDto);

    RestfulResponse inactive(String mid);

    List<AccountDto> getClientAccounts(String accountId);
    
    public String findBindingId(String bindingAccountId,String srcApplicationCode,String trgApplicationCode);
}
