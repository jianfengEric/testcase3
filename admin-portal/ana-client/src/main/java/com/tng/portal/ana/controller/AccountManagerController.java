package com.tng.portal.ana.controller;


import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.constant.RoleSystem;
import com.tng.portal.ana.constant.TopupPermession;
import com.tng.portal.ana.dto.ResetPasswordDto;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.service.*;
import com.tng.portal.ana.util.JWTTokenUtil;
import com.tng.portal.ana.util.ToolUtil;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.FieldDatas;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.PageQuery;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Zero on 2016/11/8.
 */
@RestController
@RequestMapping("account")
public class AccountManagerController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Autowired
    public HttpServletRequest request;

    @Autowired
    @Qualifier("anaEwpCallerService")
    private EwpCallerService ewpCallerService;

    /**
     * Query ANA account list
     * 
     * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 *
	 * @param sortBy
	 * 			sort by
	 * 
	 * @param isAscending
	 * 			true--ascend or false--descend
	 * 
     * @return
     */
    //@PreAuthorize("hasPermission('USER_ACCOUNT',1)")
    @ApiOperation(value="Query ANA account list", notes="")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<PageDatas> listAccounts(@ApiParam(value="current page number")@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                   @ApiParam(value="page size")@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @ApiParam(value="account search value")@RequestParam(value = "accountSearch", required = false) String accountSearch,
                                                   @ApiParam(value="sort by")@RequestParam(value = "sortBy", required = false) String sortBy,
                                                   @ApiParam(value="true--ascend or false--descend")@RequestParam(value = "isAscending", required = false) Boolean isAscending,
                                                   @RequestParam(value = "externalGroupIdSearch", required = false) String externalGroupIdSearch,
                                                   @RequestParam(value = "rolesSearch", required = false) String rolesSearch,
                                                   @RequestParam(value = "statusSearch", required = false) String statusSearch,
                                                   @RequestParam(value = "nameSearch", required = false) String nameSearch){

        return accountService.listAccounts(pageNo, pageSize,accountSearch,sortBy,isAscending, externalGroupIdSearch, rolesSearch, statusSearch,nameSearch);//sonarmodify
    }


    /**
     * Create ANA account
     *
     * @param request HttpServletRequest
     * @param postDto
     * 			New account info
     *
     * @return

     */
    @ApiOperation(value="Add account info", notes="")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public @ResponseBody RestfulResponse<String> newAddAccount(HttpServletRequest request,@ApiParam(value="account dto") @RequestBody AddAccountPostDto postDto)  {
        return accountService.newCreateAccount(ToolUtil.getRemoteHost(request), postDto);
    }

    @ApiOperation(value="Add account info", notes="")
    @RequestMapping(value = "addRole", method = RequestMethod.POST)
    public @ResponseBody RestfulResponse addAccountRoles( @RequestBody AddAccountRolePostDto postDto)  {
        return accountService.addAccountRoles(postDto);
    }

    @ApiOperation(value="Add account info", notes="")
    @RequestMapping(value = "bindingAccount", method = RequestMethod.POST)
    public @ResponseBody RestfulResponse bindingAccount( @RequestBody BindingAccountPostDto postDto)  {
        return accountService.bindingAccount(postDto);
    }


    /**
     * Query account list by external group id
     * 
     * @param externalGroupId
     * 		     ANA_ACCOUNT.EXTERNAL_GROUP_ID
     * 
     * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 * 
	 * @param sortBy
	 * 			sort by
	 * 
	 * @param isAscending
	 * 			true--ascend or false--descend
	 * 
     * @return
     *
     */
    @ApiOperation(value="Query account list by external group id", notes="")
    @RequestMapping(value = "/byExternalGroup",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<PageDatas> byExternalGroup(@ApiParam(value="external GroupId")@RequestParam("externalGroupId") String externalGroupId,
    		@ApiParam(value="current page number")@RequestParam(required = false) Integer pageNo,
    		@ApiParam(value="page size")@RequestParam(required = false) Integer pageSize,
    		@ApiParam(value="sort by")@RequestParam(required = false) String sortBy,
    		@ApiParam(value="true--ascend or false--descend")@RequestParam(required = false) String isAscending,
    		@ApiParam(value="search by")@RequestParam(required = false) String searchBy,
    		@ApiParam(value="search value")@RequestParam(required = false) String search)  {
        return accountService.getAccountByExternalGroup(externalGroupId,pageNo,pageSize,sortBy,isAscending,searchBy,search);
    }


    /**
     * Query account detail
     * 
     * @param accountId
     * 			ref. ANA_ACCOUNT.ID
     * 
     * @return
     * @
     */
    @ApiOperation(value="Query account detail", notes="")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<AccountDetailDto> detailAccount(@ApiParam(value="ref. ANA_ACCOUNT.ID")@PathVariable("id") String accountId)  {
        return accountService.getAccountDetail(accountId);
    }

    /**
     * Update account info
     * 
     * @param request HttpServletRequest
     * @param updateDto
     * 			update account info
     * 
     * @return
     * @
     */
    @ApiOperation(value="Update account info", notes="")
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<String> updateAccount(HttpServletRequest request,@ApiParam(value="account dto")@RequestBody AccountUpdateDto updateDto)  {
        return accountService.updateAccount(ToolUtil.getRemoteHost(request), updateDto);
    }
    
    /**
     * Update account profile such as name, email, language
     * 
     * @param request
     * @param updateDto
     * 			update account profile info
     * 
     * @return
     * @
     */
    @ApiOperation(value="Update account profile such as name, email, language", notes="")
    @RequestMapping(value = "updateProfile",method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<String> updateProfile(HttpServletRequest request,@ApiParam(value="account dto")@RequestBody ProfileUpdateDto updateDto)  {
        return accountService.updateProfile(ToolUtil.getRemoteHost(request), updateDto);
    }

    @RequestMapping(value = "updateStatus",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<String> updateStatus(HttpServletRequest request,
                                                               @ApiParam(value="account id", required = false)@RequestParam String id,
                                                               @ApiParam(value="status", required = true)@RequestParam String status)  {
        return accountService.updateStatus(ToolUtil.getRemoteHost(request), id, status);
    }

    /**
     * Update account password
     * 
     * @param request HttpServletRequest
     * @param passDto
     * 			update password info
     * 
     * @return
     * @
     */
    @ApiOperation(value="Update account password", notes="")
    @RequestMapping(value = "updatePassword",method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<String> updatePassword(HttpServletRequest request,@ApiParam(value="account dto")@RequestBody AccountUpPassDto passDto)  {
        return accountService.updatePassword(ToolUtil.getRemoteHost(request), passDto);
    }

    /**
     * Delete account by account id, just mark the user's is_active field to false, but not physical delete.
     * 
     * @param request HttpServletRequest
     * @param accountId
     * 			ref. ANA_ACCOUNT.ID
     * 
     * @return
     * @
     */
    @ApiOperation(value="Delete account by account id, just mark the user's is_active field to false, but not physical delete.", notes="")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public @ResponseBody RestfulResponse<String> deleteAccount(HttpServletRequest request,@ApiParam(value="ref. ANA_ACCOUNT.ID")@PathVariable("id") String accountId)  {
        return accountService.deleteAccount(ToolUtil.getRemoteHost(request), accountId);
    }

    /**
     * Query account list by role name
     * 
     * @param roleName
     * 			role name
     * 
     * @return
     * @
     */
    @ApiOperation(value="Query account list by role name", notes="")
    @RequestMapping(value = "listByRoleName",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<AccountDto>> listAccounts(@ApiParam(value="roleName")@RequestParam("roleName") String roleName)  {
        return accountService.getAccountByRoleName(roleName, false);
    }

    /**
     * Query all the accounts whose role is BD_Sales_Role
     * 
     * @return
     * @
     */
    @ApiOperation(value="Query all the accounts whose role is BD_Sales_Role", notes="")
    @RequestMapping(value = "listBySalesRole",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<AccountDto>> listDBRoleAccount()  {
        return accountService.getAccountByRoleName(RoleSystem.BD_Sales_Role.getName(),true);
    }

    /**
     * Query all roles of current account by token
     * 
     * @return
     *
     */
    @ApiOperation(value="Query all roles of current account by token", notes="")
    @RequestMapping(value = "listAccountRoles",method = RequestMethod.GET)
    public RestfulResponse<List<RoleDto>> listRolesByToken() {
        String account = userService.getCurrentAccount();
        return roleService.listRolesByAccount(account);
    }

    /**
     * User logout the ANA system
     * 
     * @return
     *
     */
    @ApiOperation(value="User logout", notes="")
    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public RestfulResponse<String> logout()  {
    	Account currentAccount = userService.getCurrentAccountInfo();
        if (currentAccount==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_token_account.getErrorCode());
        }
        return RestfulResponse.ofData(accountService.logout(currentAccount.getAccountId()));
    }

    /**
     * check whether the account has provided permission
     * 
     * @param token
     * 			user token
     * 
     * @param accountId
     * 			ref. ANA_ACCOUNT.ID
     * 
     * @param permissionType
     * 			permission type, such as CREATE, VIEW, EDIT, DELETE
     * 			
     * @return
     *
     */
    @ApiOperation(value="check whether the account has provided permission", notes="")
    @RequestMapping(value = "hasPermession",method = RequestMethod.POST)
    public RestfulResponse<Boolean> hasPermession(@ApiParam(value="token")@RequestParam(required = true)String token,
    		@ApiParam(value="ref. ANA_ACCOUNT.ID")@RequestParam(required = true) String accountId,
    		@ApiParam(value="permission type, such as CREATE, VIEW, EDIT, DELETE")@RequestParam(required = true) TopupPermession permissionType){
        Boolean permession = accountService.hasPermession(accountId,permissionType);
        return  RestfulResponse.ofData(permession);
    }

    /**
     * Query all account list
     * @return
     */
    @ApiOperation(value="Query all account list", notes="")
    @RequestMapping(value = "all",method = RequestMethod.GET)
    @ResponseBody
    public RestfulResponse<List<AccountDto>> queryAllAccounts() {
        return accountService.queryAllAccounts();
    }

    /**
     * Edit User(Account) Info
     * @param request HttpServletRequest
     * @param updateDto update account info
     * @return
     *
     */
    @ApiOperation(value="Edit User(Account) Info", notes="")
    @RequestMapping(value = "editUser",method = RequestMethod.PUT)
    public @ResponseBody RestfulResponse<String> editUser(HttpServletRequest request,@ApiParam(value="account dto")@RequestBody AccountUpdateDto updateDto)  {
        return accountService.editUser(ToolUtil.getRemoteHost(request), updateDto);
    }

    /**
     * Reset Send Email
     * @param accountId accountId
     * @return
     * @
     */
    @ApiOperation(value="Reset Send Email", notes="")
    @RequestMapping(value = "reSendEmail",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse reSendEmail(@ApiParam(value="accountId")@RequestParam("accountId") String accountId)  {
        return accountService.reSendEmail(accountId);
    }

    @ApiOperation(value="Reset Password", notes="")
    @RequestMapping(value = "rePassword",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse rePassword(@ApiParam(value="accountId")@RequestParam("accountId") String accountId)  {
        return accountService.rePassword(accountId);
    }

    /**
     * Get current account info
     * @return
     */
    @ApiOperation(value="Get current account info", notes="")
    @RequestMapping(value = "currentAccount",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<Account> getCurrentAccountInfo() {
    	Account account = userService.getCurrentAccountInfo();
        return RestfulResponse.ofData(account);
    }
    
    /**
     * Get current account info
     * @return
     */
    @ApiOperation(value="Get local account info", notes="")
    @RequestMapping(value = "localAccount",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<Account> getLocalAccountInfo(@RequestParam(required = true) String applicationCode,
    		@RequestParam(required = true) String loginId)  {
    	Account account = accountService.getLocalAccountInfo(applicationCode, loginId);
        return RestfulResponse.ofData(account);
    }
    
    /**
     * Get current account info
     * @return
     */
    @ApiOperation(value="Get sso account info", notes="")
    @RequestMapping(value = "ssoAccount",method = RequestMethod.GET)
    public @ResponseBody RestfulResponse<String> getSSOAccountInfo(@RequestParam(required = true) String accountId)  {
    	String ssoLoginId = accountService.getSSOAccountInfo(accountId);
        return RestfulResponse.ofData(ssoLoginId);
    }

    @RequestMapping(value = "/checkFirstNameAndLastName",method = RequestMethod.GET)
    public @ResponseBody  RestfulResponse<Boolean> checkFirstNameAndLastName(@RequestParam(required = false) String firstName,
                                                                             @RequestParam(required = false) String lastName,
                                                                             @RequestParam(required = false) String code) {
        return  accountService.checkFirstNameAndLastName(firstName, lastName, code);
    }
    
    @RequestMapping(value = "/activationAccount",method = RequestMethod.PUT)
    @ResponseBody
    public RestfulResponse<AccountDto> activationAccount(
    		@RequestParam(required = false) String code,
    		@RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
    		@RequestParam(required = false) String password) {
      return  accountService.activationAccount(firstName, lastName, password, code);
    }

    /**
     * Modification password in mail link 
     * @param reset
     * @return
     */
    @RequestMapping(value = "/resetPassword",method = RequestMethod.POST)
    public @ResponseBody RestfulResponse<AccountDto> resetPassword(@ApiParam(value="reset")@RequestBody ResetPasswordDto reset)  {
        return accountService.resetPassword(reset.getCode(), reset.getNewPwd());
    }

    @RequestMapping(value = "/checkActivation",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<Boolean> checkActivation(HttpServletRequest request, @RequestParam(value = "code", required = false) String code){
    	Boolean b = accountService.checkActivation(code);
    	return RestfulResponse.ofData(b);
    }
    
    @RequestMapping(value = "/checkEmailDate",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<Boolean> checkEmailDate(@RequestParam(value = "code", required = false) String code){
    	Date sendDate = JWTTokenUtil.getIssuedAt(code);
    	Date now = new Date();
    	return RestfulResponse.ofData(!(now.getTime() - sendDate.getTime() > Long.valueOf(PropertiesUtil.getAppValueByKey("email.link.timeout"))));  
    }
    
    @RequestMapping(value = "/checkResetPwd",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<Boolean> checkResetPwd(@RequestParam(value = "code", required = false) String code){
    	Boolean b = accountService.checkResetPwd(code);
    	return RestfulResponse.ofData(b);
    }

    @ApiOperation(value="Query ANA account list", notes="")
    @RequestMapping(value = "/listAnaAccounts",method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<com.tng.portal.common.vo.PageDatas> listAnaAccounts(@ApiParam(value="pageQuery")@RequestBody PageQuery<AnaAccount> pageQuery){
        FieldDatas fieldDatas = FieldDatas.getInstance("USER_ACCOUNT",null);
        pageQuery.setMustQueries(fieldDatas.getMustQueries());
        pageQuery.setFieldNames(fieldDatas.getFieldNames());
        return accountService.listAccounts(pageQuery);
    }

    /**
     * MAM Call API From Ana To Create Account,And bind MAM Account
     * @param postDto ana account info
     * @return
     */
    @RequestMapping(value = "createAndBindAnaAccount", method = RequestMethod.POST)
    @ResponseBody
    public RestfulResponse<Map<String, List<String>>> createAndBindAnaAccount(@ApiParam(value = "account dto") @RequestBody MamAddAccountPostDto postDto) {
        return remoteAccountService.createAndBindAnaAccount(ToolUtil.getRemoteHost(request), postDto);
    }


    @GetMapping("get-all-Participant-list")
    @ResponseBody
    public RestfulResponse<List<ParticipantDto>> getAllParticipantList() {
        List<ParticipantDto> list = ewpCallerService.getParticipantList("");
        return RestfulResponse.ofData(list);
    }
}