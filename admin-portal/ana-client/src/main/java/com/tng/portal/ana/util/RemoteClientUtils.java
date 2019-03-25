package com.tng.portal.ana.util;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.dto.MerchantDto;
import com.tng.portal.ana.entity.AnaPermission;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.constant.PermissionId;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.EmailService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.CoderUtil;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.EmailParameterVo;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Roger on 2017/12/11.
 */
@Component("remoteClientUtils")
public class RemoteClientUtils {
    private final Logger logger = LoggerFactory.getLogger(RemoteClientUtils.class);

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Qualifier("commonEmailService")
    @Autowired
    private EmailService commonEmailService;

    private static final String SUCCESS="success";

    public String getSSOAccountByLocalAccountId(String accountId, String urlEndPoint){
        try {
            String getSsoAccount = PropertiesUtil.getPropertyValueByKey("remote.account.getSsoAccount");
            Map<String, String> params = new HashMap<>();
            params.put("id", accountId);
            RestfulResponse<String> restfulResponse = httpClientUtils.httpGet(urlEndPoint + getSsoAccount, RestfulResponse.class, params);
            if(null != restfulResponse && SUCCESS.equals(restfulResponse.getStatus())){
                return restfulResponse.getData();
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorSsoAccessingAccountInformation.getErrorCode());
        }
    }

    public String bindSso(MamAddAccountPostDto postDto, String urlEndPoint) {
        String bindAccount = PropertiesUtil.getPropertyValueByKey("remote.account.bindAccount");
        return httpClientUtils.postObject(urlEndPoint + bindAccount, String.class, postDto);
    }

    public Account getAccountInfoByAccount(String account, String urlEndPoint) {
        try {
            String getAccountInfoByAccount = PropertiesUtil.getPropertyValueByKey("remote.account.getAccountInfoByAccount");
            Map<String, String> params = new HashMap<>();
            params.put("account", account);
            RestfulResponse<Account> restfulResponse = httpClientUtils.httpGet(urlEndPoint + getAccountInfoByAccount, RestfulResponse.class, params);

            if(null != restfulResponse && SUCCESS.equals(restfulResponse.getStatus())){
                return restfulResponse.getData();
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorAccessingAccountInformation.getErrorCode());
        }
    }

    private Map<String, String> setParams(String serverCode){
        Map<String, String> params= new HashMap<>();
        params.put("apiKey", PropertiesUtil.getAppValueByKey(serverCode.toLowerCase()+".comment.api.key"));
        params.put("consumer", PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName));
        return params;
    }

    public void sendEmail(String pwd, String account,  String receiver, String password, String mid){
        String model = PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName);
        EmailParameterVo emailParameterVo = new EmailParameterVo();
        emailParameterVo.setJob("sendRegisteredAccountEmail");
        emailParameterVo.setReceivers(receiver);
        LinkedHashMap<String,String> templateInput = new LinkedHashMap<>();
        templateInput.put("account", account);
        templateInput.put("password", pwd);
        templateInput.put("companyNameEn", StringUtils.isBlank(mid)?"TNG":mid);
        String url = PropertiesUtil.getPropertyValueByKey("registration.activation.link.path");
        String token = JWTTokenUtil.generateToken(account, password,null);
        templateInput.put("link", url + "model="+model+"&code=" + token);
        emailParameterVo.setTemplateInput(templateInput);
        try {
            commonEmailService.sendByHttp(emailParameterVo,null);
        }catch (Exception e){
            logger.error("mam send email error", e);
        }
    }

    public RestfulResponse<String> addSsoAccount(AddAccountPostDto addAccountPostDto, String urlEndPoint,String serverCode) {
        String addSsoAccount = PropertiesUtil.getPropertyValueByKey("remote.account.addSsoAccount");
        return httpClientUtils.httpPost(urlEndPoint + addSsoAccount, addAccountPostDto, RestfulResponse.class, null, setParams(serverCode));
    }

    public RestfulResponse<String> updateLocalAccountInfo(AccountUpdateDto updateDto, String urlEndPoint,String serverCode) {
        String updateLocalAccountInfo = PropertiesUtil.getPropertyValueByKey("remote.account.updateLocalAccountInfo");
        return httpClientUtils.httpPut(urlEndPoint + updateLocalAccountInfo, RestfulResponse.class, null, updateDto, setParams(serverCode));
    }

    public RestfulResponse<String> updateClientStatus(Map<String, String> params, String urlEndPoint) {
        String updateClientStatus = PropertiesUtil.getPropertyValueByKey("remote.account.updateClientStatus");
        return httpClientUtils.httpGet(urlEndPoint + updateClientStatus, RestfulResponse.class, params);
    }

    public RestfulResponse<String> updateClientAccountInfo(AccountUpdateDto updateDto, String urlEndPoint,String serverCode) {
        String updateClientAccountInfo = PropertiesUtil.getPropertyValueByKey("remote.account.updateClientAccountInfo");
        return httpClientUtils.httpPut(urlEndPoint + updateClientAccountInfo, RestfulResponse.class, null, updateDto, setParams(serverCode));
    }

    public RestfulResponse<String> resendClientEmail(Map<String, String> params, String urlEndPoint) {
        String resendClientEmail = PropertiesUtil.getPropertyValueByKey("remote.account.resendClientEmail");
        return httpClientUtils.httpGet(urlEndPoint + resendClientEmail, RestfulResponse.class, params);
    }

    public RestfulResponse<String> resetPassword(Map<String, String> params, String urlEndPoint) {
        String resetPassword = PropertiesUtil.getPropertyValueByKey("remote.account.resetPassword");
        return httpClientUtils.httpGet(urlEndPoint + resetPassword, RestfulResponse.class, params);
    }

    public RestfulResponse getBindAccounts(Map<String, String> params, String urlEndPoint) {
        String getBindAccounts = PropertiesUtil.getPropertyValueByKey("remote.account.getBindAccounts");
        return httpClientUtils.httpGet(urlEndPoint + getBindAccounts, RestfulResponse.class, params);
    }

    public Account getAccountInfoByBindAccountId(String accountId, String urlEndPoint) {
        try {
            String getAccountInfoByBindAccountId = PropertiesUtil.getPropertyValueByKey("remote.account.getAccountInfoByBindAccountId");
            Map<String, String> params = new HashMap<>();
            params.put("id", accountId);
            RestfulResponse<Account> restfulResponse = httpClientUtils.httpGet(urlEndPoint + getAccountInfoByBindAccountId, new ParameterizedTypeReference<RestfulResponse<Account>>() {}, params);
            if(null != restfulResponse){
                if("success".equals(restfulResponse.getStatus())){
                    return restfulResponse.getData();
                }
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorAccessingAccountInformation.getErrorCode());
        }
    }

    public RestfulResponse queryAccountList(AccountQueryPostDto postDto, String urlEndPoint,String serverCode) {
        String queryAccountList = PropertiesUtil.getPropertyValueByKey("remote.account.queryAccountList");
        return httpClientUtils.httpPost(urlEndPoint + queryAccountList, postDto, RestfulResponse.class, null, setParams(serverCode));
    }

    public boolean isViewAllMerchant(Account account, String function, String urlEndPoint) {
        boolean viewAllMerchant=false;
        //TNG Internal users can judge viewAllMerchant Jurisdiction 
        String permission = PropertiesUtil.getPropertyValueByKey("mam.role.permission");
        String url = urlEndPoint+permission;
        if(isInternalMerchant(account.getMerchantId(),account.getExternalGroupId())){
            Map<String,String> map=new HashMap<>() ;
            map.put("permissionId", String.valueOf(PermissionId.VIEW_ALL_MERCHANT));
            try {
                RestfulResponse<AnaPermissionDto> response=httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<AnaPermissionDto>>() {}, map);
                logger.info("url:{} response:{}", url, response);
                if(response.hasSuccessful()) {
                    AnaPermissionDto anaPermissionDto = response.getData();
                    AnaPermission anaPermission = new AnaPermission();
                    anaPermission.setId(anaPermissionDto.getId());
                    anaPermission.setName(anaPermissionDto.getName());
                    anaPermission.setDescription(anaPermissionDto.getDescription());
                    List<AnaPermission> viewAllMerchantPermission = new ArrayList<>();
                    viewAllMerchantPermission.add(anaPermission);
                    viewAllMerchant = RoleFunctionPermissionUtil.hasPermission(account, function, viewAllMerchantPermission);
                }
            } catch (Exception e) {
                logger.error("url:{} ", url, e);
            }
        }
        return viewAllMerchant;
    }

    private static boolean isInternalMerchant(Long merchantId,String mid) {
        if (merchantId == null) {
            return false;
        }
        MerchantDto merchantDto = RabbitClientUtils.getMerchant(CoderUtil.encrypt(
                StringUtils.trimToEmpty(String.valueOf(merchantId))),
                CoderUtil.encrypt(StringUtils.trimToEmpty(mid)));
        if (merchantDto == null) {
            return false;
        }
        return "TNG".equals(merchantDto.getMerchantType());
    }

    public RestfulResponse inactive(String urlEndPoint, String mid) {
        String accountInactive = PropertiesUtil.getPropertyValueByKey("remote.account.inactive");
        Map<String,String> params = new HashMap<>();
        params.put("mid",mid);
        return httpClientUtils.httpGet(urlEndPoint+accountInactive, RestfulResponse.class, params);
    }
}
