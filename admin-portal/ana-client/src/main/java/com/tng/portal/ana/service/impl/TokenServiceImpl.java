package com.tng.portal.ana.service.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.bean.Role;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.constant.AccountStatus;
import com.tng.portal.ana.constant.AccountType;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaAccountAccessToken;
import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.service.TokenService;
import com.tng.portal.ana.util.AnaBeanUtils;
import com.tng.portal.ana.util.StringUtil;
import com.tng.portal.ana.vo.AnaAccountAccessTokenDto;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.util.*;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tokenServiceImpl")
@Transactional
public class TokenServiceImpl implements TokenService{
    private transient Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Resource
    private AnaAccountRepository anaAccountRepository;

    @Resource
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Autowired
    private AnaApplicationRepository anaApplicationRepository;

    @Override
    public void syncToken(AnaAccountAccessTokenDto anaAccountAccessTokenDto) {
        try {
            AnaAccount anaAccount = anaAccountRepository.findOne(anaAccountAccessTokenDto.getAccountid());
            AnaAccountAccessToken anaAccountAccessToken = anaAccount.getAccountToken();
            if(null == anaAccountAccessToken){
                anaAccountAccessToken = new AnaAccountAccessToken();
                anaAccountAccessToken.setAnaAccount(anaAccount);
            }
            anaAccountAccessToken.setToken(anaAccountAccessTokenDto.getToken());
            anaAccountAccessToken.setExpriedTime(DateUtils.parseDate(anaAccountAccessTokenDto.getExpriedtime()));
            anaAccountAccessTokenRepository.save(anaAccountAccessToken);
            Account account = new Account();
            account.setAccountId(anaAccount.getId());
            account.setUsername(anaAccount.getAccount());
            account.setFirstName(anaAccount.getFirstName());
            account.setLastName(anaAccount.getLastName());
            account.setPassword(anaAccount.getPassword());
            account.setEmail(anaAccount.getEmail());
            List<Role> roles = AnaBeanUtils.toRoles(anaAccount.getAnaRoles());
            account.setRoles(roles);
            account.setInternal(anaAccount.getInternal());
            account.setExternalGroupId(anaAccount.getExternalGroupId());
            account.setUserType(AccountType.getName(anaAccount.getUserType()));
            UserDetails userDetails = new UserDetails(account);
            AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,anaAccountAccessTokenDto.getToken(),anaAccountAccessTokenDto.getRemoteaddr());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if(AccountStatus.NotVerified.getCode().equals(anaAccount.getStatus())){
                anaAccount.setStatus(AccountStatus.Active.getCode());
            }
        }catch (Exception e) {
            log.error("syncToken Error",e);
        }
    }

    @Override
    public void clearToken(AnaAccountAccessTokenDto anaAccountAccessTokenDto) {
        AnaAccount anaAccount = anaAccountRepository.findOne(anaAccountAccessTokenDto.getAccountid());
        if(null == anaAccount){
            return;
        }
        AnaAccountAccessToken anaAccountToken = anaAccountAccessTokenRepository.findByAnaAccount(anaAccount);
        if (null != anaAccountToken){
            anaAccountAccessTokenRepository.delete(anaAccountToken);
        }
    }
    @Override
    public UserDetails updateToken(UserDetails userDetails,String accessToken){
        try {
            AnaAccountAccessToken accountAccessToken = anaAccountAccessTokenRepository.findByToken(accessToken);
            if(accountAccessToken!=null){
                return userDetails;
            }
            AnaAccountAccessTokenDto anaAccountAccessTokenDto;
            if(ApplicationContext.Communication.MQ.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.communicationStyle))){
                anaAccountAccessTokenDto = getTokenByMQ(accessToken);
            }else {
                anaAccountAccessTokenDto = getTokenByHttp(accessToken, ApplicationContext.Modules.ANA);
            }
            if(null == anaAccountAccessTokenDto){
                log.error("get token no success !");
                return userDetails;
            }
            return parse(anaAccountAccessTokenDto);
        } catch (Exception e) {
            log.error("get token error!", e);
        }
        return userDetails;
    }

   private AnaAccountAccessTokenDto getTokenByHttp(String accessToken, String serviceName){
       try {
           AnaApplication anaApplication = anaApplicationRepository.findByCode(serviceName);
           Map<String, String> params = new HashMap<>();
           params.put("token", accessToken);
           params.put("code", PropertiesUtil.getServiceName());
           params.put("consumer", PropertiesUtil.getAppValueByKey("service.name"));
           params.put("apiKey", PropertiesUtil.getAppValueByKey(serviceName.toLowerCase() + ".comment.api.key"));
           String restfulResponse = httpClientUtils.httpGet(anaApplication.getInternalEndpoint() + "/remote/account/getClientToken", String.class, params);
           if (StringUtils.isBlank(restfulResponse)) {
               return null;
           }
           JsonParser jsonParser = new JsonParser();
           JsonObject jsonObject = jsonParser.parse(restfulResponse).getAsJsonObject();
           if (jsonObject.has("status") && jsonObject.get("status").getAsString().equals("success") && jsonObject.has("data")) {
               return JsonUtils.readValue(jsonObject.get("data").toString(), AnaAccountAccessTokenDto.class);
           }
       }catch (Exception e) {
           log.error("get token error!", e);
       }
        return null;
    }

    private AnaAccountAccessTokenDto getTokenByMQ(String accessToken){
        try {
            RestfulResponse<AnaAccountAccessTokenDto> restfulResponse = RabbitMQUtil.sendRequestToSOAService(
            		PropertiesUtil.getPropertyValueByKey("ana.service.name"),
            		PropertiesUtil.getPropertyValueByKey("ana.method.getToken"),
                    AnaAccountAccessTokenDto.class,
                    MqParam.gen(accessToken), MqParam.gen(PropertiesUtil.getServiceName()));
            if (null != restfulResponse && restfulResponse.hasSuccessful()) {
                return restfulResponse.getData();
            }
        }catch (Exception e) {
            log.error("get token error!", e);
        }
        return null;
    }


    @Override
    public UserDetails updateClientToken(UserDetails userDetails, String accessToken) {
        try {
            AnaAccountAccessToken accountAccessToken = anaAccountAccessTokenRepository.findByToken(accessToken);
            if (accountAccessToken != null) {
                return userDetails;
            }
            AnaAccountAccessTokenDto anaAccountAccessTokenDto = getTokenByHttp(accessToken, ApplicationContext.Modules.SMS);
            if(null == anaAccountAccessTokenDto){
                log.error("get token no success !");
                return userDetails;
            }
            return parse(anaAccountAccessTokenDto);
        }catch (Exception e) {
            log.error("get token error!", e);
        }
        return userDetails;
    }

    private UserDetails parse(AnaAccountAccessTokenDto anaAccountAccessTokenDto){
        AnaAccount anaAccount = anaAccountRepository.findOne(anaAccountAccessTokenDto.getAccountid());
        AnaAccountAccessToken anaAccountAccessToken = anaAccount.getAccountToken();
        if (null == anaAccountAccessToken) {
            anaAccountAccessToken = new AnaAccountAccessToken();
            anaAccountAccessToken.setAnaAccount(anaAccount);
            anaAccountAccessToken.setRemoteAddr(anaAccountAccessTokenDto.getRemoteaddr());
            anaAccountAccessToken.setOptimisticLockVersion(0);
        }
        anaAccountAccessToken.setToken(anaAccountAccessTokenDto.getToken());
        anaAccountAccessToken.setExpriedTime(DateUtils.parseDate(anaAccountAccessTokenDto.getExpriedtime()));
        anaAccountAccessTokenRepository.saveAndFlush(anaAccountAccessToken);
        Account account = new Account();
        account.fill(anaAccount);
        UserDetails userDetails = new UserDetails(account);
        AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails, anaAccountAccessTokenDto.getToken(), anaAccountAccessTokenDto.getRemoteaddr());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userDetails;
    }

}
