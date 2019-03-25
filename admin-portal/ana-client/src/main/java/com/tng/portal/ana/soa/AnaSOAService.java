package com.tng.portal.ana.soa;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.constant.TopupPermession;
import com.tng.portal.ana.entity.AnaAccountAccessToken;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.RemoteAccountService;
import com.tng.portal.ana.vo.AnaAccountAccessTokenDto;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.soa.AbstractMQBasicService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.I18nMessge;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;


@Service("AnaSOAService")
public class AnaSOAService  extends AbstractMQBasicService{
	private static final Logger logger = LoggerFactory.getLogger(AnaSOAService.class);

	@Autowired
	private I18nMessge i18nMessge;

	@Autowired
	private RemoteAccountService remoteAccountService;

	@PostConstruct
	public void initConnectToMQ(){
		if(ApplicationContext.Modules.ANA.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))
				&&ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
			logger.info("AnaSOAService connectToMQ start!!");
			startListening();
			logger.info("AnaSOAService connectToMQ end!!");
		}else{
			logger.info("AnaSOAService will not run!!");
		}
		
	}
	
    @Autowired
    private AccountService accountService;

	public String getServiceName(){
		return MessageFormat.format(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceNameTemplate),PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName));
	}
	
	/**
     * Query account token info by token
     * 
     * @param token
     * 			token string
     * 
     * @return
     */
    public RestfulResponse<Boolean> checkToken(String token) {
        RestfulResponse responseBody = new RestfulResponse();
        try{
        if(null==token){
        	responseBody.setFailStatus();
            responseBody.setData(false);
            return responseBody;
        }
        
        AnaAccountAccessToken anaAccountToken = accountService.getAccountTokenByToken(token);
        if(null == anaAccountToken){
        	responseBody.setFailStatus();
            responseBody.setData(false);
            return responseBody;
        }
        
        responseBody.setSuccessStatus();
        responseBody.setData(true);
        return responseBody;
        }catch(Exception e){
        	logger.error("checkToken error:"+e.getMessage());
        	responseBody.setFailStatus();
            responseBody.setData(false);
            return responseBody;
        }
     }
    
//    /**
//     * Registered account
//     * usage scenario : Create external account after MAM publish merchant successfully
//     *
//     * @param token
//     * 			token string
//     * @param dto
//     * 			New account info
//     *
//     * @return
//     */
//    public RestfulResponse<Boolean> registered(String token, AccountRegisteredDto dto) {
//    	RestfulResponse<Boolean> restResponse = new RestfulResponse<Boolean>();
//    	try {
//    		this.tokenAuthentication(token);
//			accountService.registeredAccount(null, dto);
//		} catch (BusinessException e) {
//			restResponse = i18nMessge.getErrorMessageByErrorCode(String.valueOf(e.getErrorcode()));
//			return restResponse;
//		}
//    	restResponse.setSuccessStatus();
//        return restResponse;
//    }
    
    /**
     * Query account list by role name string
     * usage scenario : if operator execute workflow action, system will send email to next node operators who find by role name
     * 
     * @param roleName
     * 			the role name string, separated by commas
     * 
     * @return
     */
    public RestfulResponse<List<Account>> roleAcc(String roleName) {
    	RestfulResponse<List<Account>> restResponse = new RestfulResponse<>();
    	try {
    		restResponse = accountService.getClientAccountByRoleName(roleName);
		} catch (BusinessException e) {
			restResponse = i18nMessge.getErrorMessageByErrorCode(String.valueOf(e.getErrorcode()));
			return restResponse;
		}
    	restResponse.setSuccessStatus();
        return restResponse;
    }
    
    /**
     * Query user's account info by token
     * usage scenario : The client limited resources visits each time
     * 
     * @param token
     * 			user token 
     * 
     * @return
     */
    public RestfulResponse<Account> getUserInfo(String token) {
    	RestfulResponse<Account> restResponse = new RestfulResponse<>();
    	try {
    		restResponse.setData(accountService.getAuthuserInfoByToken(token));
		} catch (BusinessException e) {
			restResponse = i18nMessge.getErrorMessageByErrorCode(String.valueOf(e.getErrorcode()));
			return restResponse;
		}
    	restResponse.setSuccessStatus();
        return restResponse;
    }

    /**
     * Query current user's account info by userAccount
     * 
     *  @param userAccount
     * 			ref.  ANA_ACCOUNT.ACCOUNT
     * 
     * @return
     */
	public RestfulResponse<Account> authUserInfo(String userAccount) {
		RestfulResponse<Account> restResponse = new RestfulResponse<>();
		try {
			Account account = accountService.getAuthUserInfoByUserAccount(userAccount);
			restResponse.setData(account);
		} catch (BusinessException e) {
			restResponse = i18nMessge.getErrorMessageByErrorCode(String.valueOf(e.getErrorcode()));
			return restResponse;
		}
		restResponse.setSuccessStatus();
		return  restResponse;
	}

	/**
     * Query account list by external group id
     * 
     * @param externalGroupId
     * 			ANA_ACCOUNT.EXTERNAL_GROUP_ID
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
	public RestfulResponse<PageDatas> byExternalGroup(String externalGroupId, Integer pageNo, Integer pageSize, String sortBy, String isAscending,String searchBy, String search)  {
		try {
			return accountService.getAccountByExternalGroup(externalGroupId, pageNo, pageSize, sortBy, isAscending, searchBy, search);
		}catch (BusinessException e) {
			logger.error("BusinessException",e);
			return i18nMessge.getErrorMessageByErrorCode(String.valueOf(e.getErrorcode()));
		}
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
	 */
	@Deprecated
	public RestfulResponse<Boolean> hasPermession(String token, String accountId, String permissionType) {
		try {
			RestfulResponse<Boolean> restfulResponse = new RestfulResponse<>();
			Boolean permession =  accountService.hasPermession(accountId, TopupPermession.valueOf(permissionType));
			restfulResponse.setData(permession);
			restfulResponse.setSuccessStatus();
			return  restfulResponse;
		} catch (BusinessException e) {
			logger.error("BusinessException",e);
			return i18nMessge.getErrorMessageByErrorCode(String.valueOf(e.getErrorcode()));
		}
	}

	/**
     * Get handler instance
     * 
     * @return
     */
	 public Object getHandleInstance(){		 
		 return this;
	 }
	 
	 /**
     * swap user's authentication info by token
     * 
     * @param token
     * 			token string
     * 
     * @return
     */
	 public void tokenAuthentication(String token) {
		 UserDetails userDetails = new UserDetails(accountService.getAuthuserInfoByToken(token));
		 AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,token,null);
		 SecurityContextHolder.getContext().setAuthentication(authentication);
	 }

	public RestfulResponse<AnaAccountAccessTokenDto> getToken(String token, String code) {
		RestfulResponse<AnaAccountAccessTokenDto> restfulResponse = new RestfulResponse<>();
		AnaAccountAccessTokenDto anaAccountAccessTokenDto =  accountService.getToken(token, code);
		restfulResponse.setData(anaAccountAccessTokenDto);
		restfulResponse.setSuccessStatus();
		return  restfulResponse;
	}

	public RestfulResponse<String> findBindingId(String bindingAccountId,String srcApplicationCode,String trgApplicationCode){
		String res = remoteAccountService.findBindingId(bindingAccountId, srcApplicationCode, trgApplicationCode);
		return RestfulResponse.ofData(res);
	}

}
