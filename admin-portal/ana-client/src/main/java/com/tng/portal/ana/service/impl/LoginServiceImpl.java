package com.tng.portal.ana.service.impl;


import com.tng.portal.ana.authentication.AnaPrincipalAuthenticationToken;
import com.tng.portal.ana.bean.TokenType;
import com.tng.portal.ana.bean.UserDetails;
import com.tng.portal.ana.constant.AccountApplicationStatus;
import com.tng.portal.ana.constant.AccountStatus;
import com.tng.portal.ana.constant.AccountType;
import com.tng.portal.ana.entity.*;
import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.ana.repository.AnaAccountApplicationRepository;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.repository.AnaLoginSessionRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.LoginService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.util.JWTTokenUtil;
import com.tng.portal.ana.vo.AccountAccessTokenDto;
import com.tng.portal.ana.vo.LoginDto;
import com.tng.portal.ana.vo.LoginRestDto;
import com.tng.portal.ana.vo.RefreshTokenDto;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Zero on 2016/11/14.
 */
@Service("loginService")
@Transactional
public class LoginServiceImpl implements LoginService {
    private transient Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private AnaAccountRepository anaAccountRepository;
    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;
    @Autowired
    private AccountService accountService;

    private static final String TOKENEXPIRESMINS="token.expires.mins";

    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Autowired
    private AnaAccountApplicationRepository anaAccountApplicationRepository;

    @Autowired
    private AnaLoginSessionRepository anaLoginSessionRepository;

    /**
     * User login the ANA system
     * 
     * @param request HttpServletRequest
     * @param username login id
     * 
     * @param password
     * 			user password
     * 
     * @return
     */
    @Override
    public LoginRestDto login(HttpServletRequest request,String username, String password, String applicationCode)  {

        LoginRestDto loginRestDto;
        List<String> status = Stream.of(AccountStatus.values()).filter(item->item.equals(AccountStatus.NotVerified) || item.equals(AccountStatus.Active)).map(AccountStatus::getCode).collect(Collectors.toList());
        AnaAccount account = anaAccountRepository.findByAccountAndStatusIn(username, status);
        if(account != null){
             if(account.getStatus().equals(AccountStatus.Inactive.getCode())){
                throw new BusinessException(SystemMsg.ServerErrorMsg.account_status_error.getErrorCode(),AccountStatus.Inactive.name());
            } else if(account.getStatus().equals(AccountStatus.Terminated.getCode())){
                throw new BusinessException(SystemMsg.ServerErrorMsg.account_status_error.getErrorCode(),AccountStatus.Terminated.name());
            }
            String userPassword = account.getPassword();
            password  = DigestUtils.md5DigestAsHex((password + username).getBytes());
            if(password.equals(userPassword)){
                loginRestDto = new LoginRestDto(account.getAccount());
                AnaAccountAccessToken token = account.getAccountToken();
                long nowMillis = System.currentTimeMillis();
                String expiresMinus = PropertiesUtil.getPropertyValueByKey(TOKENEXPIRESMINS);
                Long expires = Long.parseLong(expiresMinus) * 60 ;
                long expiresMillis = nowMillis+expires * 1000;
                String accessToken = JWTTokenUtil.generateToken(username, TokenType.USER_TOKEN, expiresMillis);
                Date now = new Date(expiresMillis);
                if(null == token){
                    token = new AnaAccountAccessToken();
                    token.setToken(accessToken);
                    token.setAnaAccount(account);
                    token.setExpriedTime(now);
                }else {
                    Date n = new Date();
                    if(n.after(token.getExpriedTime())){
                        token.setToken(accessToken);
                        token.setExpriedTime(now);
                    }else {
                        expires = (token.getExpriedTime().getTime()-n.getTime())/1000;
                    }
                }
                anaAccountAccessTokenRepository.save(token);
                loginRestDto.setAccessToken(token.getToken());
                loginRestDto.setExpriesIn(expires);
                loginRestDto.setPhoneNumber(account.getMobile());
                loginRestDto.setLanguage(account.getLanguage());
                loginRestDto.setEmail(account.getEmail());
                loginRestDto.setAccountId(account.getId());
                List<AnaRole> anaRoles = account.getAnaRoles();
                List<String> roles = anaRoles.stream().map(role -> role.getName()).collect(Collectors.toList());
                loginRestDto.setRoles(roles);
                loginRestDto.setInternal(account.getInternal());
                loginRestDto.setExternalGroupId(account.getExternalGroupId());
                loginRestDto.setSystemUrl(account.getAnaAccountApplications());
                long logCount = anaLoginSessionRepository.countByAccount(account.getAccount());
                if(logCount == 0){
                    loginRestDto.setFirstLogin(true);
                    loginRestDto.setIsRestPwd(false);
                }else {
                    loginRestDto.setFirstLogin(false);
                    if(null == account.getResetPwdTime()){
                        loginRestDto.setIsRestPwd(false);
                    }else{
                        long count = anaLoginSessionRepository.countByAccountAndSessionDateTimeAfter(account.getAccount(), account.getResetPwdTime());
                        if(count == 0){
                            loginRestDto.setIsRestPwd(true);
                        }else {
                            loginRestDto.setIsRestPwd(false);
                        }
                    }
                }
                loginRestDto.setUserType(AccountType.getName(account.getUserType()));
                loginRestDto.setFirstName(account.getFirstName());
                loginRestDto.setLastName(account.getLastName());
                if(account.getStatus().equals(AccountStatus.NotVerified.getCode())){
                    account.setStatus(AccountStatus.Active.getCode());
                }
                try {
                    if (ApplicationContext.Modules.ANA.equals(PropertiesUtil.getServiceName())
                            && ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))) {
                        for (AnaAccountApplication anaAccountApplication : account.getAnaAccountApplications()) {
                            if(ApplicationContext.Modules.ANA.equals(anaAccountApplication.getAnaApplication().getCode())){
                                continue;
                            }
                            if(anaAccountApplication.getStatus().equals(AccountApplicationStatus.Temporary.getCode())) {
                                AnaApplication anaApplication = anaAccountApplication.getAnaApplication();
                                // Modify binding state 
                                anaAccountApplication.setStatus(AccountApplicationStatus.Connected.getCode());
                                anaAccountApplicationRepository.saveAndFlush(anaAccountApplication);
                                try {
                                    // Modify sub module user state 
                                    Map<String, String> params = new HashMap<>();
                                    params.put("id", anaAccountApplication.getBindingAccountId());
                                    params.put("status", AccountStatus.Active.name());
                                    params.put("apiKey", PropertiesUtil.getAppValueByKey(anaApplication.getCode().toLowerCase()+".comment.api.key"));
                                    params.put("consumer", ApplicationContext.Modules.ANA);
                                    String updateClientStatus = PropertiesUtil.getPropertyValueByKey("remote.account.updateClientStatus");
                                    httpClientUtils.httpGet(anaApplication.getInternalEndpoint() + updateClientStatus, RestfulResponse.class, params);
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                   log.error(e.getMessage(), e);
                }
            }else {
                throw new BusinessException(SystemMsg.LoginMsg.LOGIN_PASSWORD_ERROR.getErrorCode());
            }
        }else {
            throw new BusinessException(SystemMsg.LoginMsg.LOGIN_ACCOUNT_ERROR.getErrorCode());
        }
        // Write a log 
        accountService.writeLoginOrLogoutLog(request,account,true);
        return  loginRestDto;
    }
    
    /**
     * There is a counter in front end ,when front end send a request to back-end, front end will calculate whether the session time remains 2 minutes,
     * If yes, front end will call this function to refresh current user's token info, if not, do nothing
     * If the front end call back-end after session expire, it will redirect to login page
     * This function will return new token and new session time to front end.
     * 
     * @param token
     * 			token string
     * 
     * @return
     */
    @Override
    public synchronized RefreshTokenDto refreshToken(String token) {
        AnaAccountAccessToken accountToken = anaAccountAccessTokenRepository.findByToken(token);
        if(null!=accountToken){       	        	        
            RefreshTokenDto dto = new RefreshTokenDto();
            long nowMillis = System.currentTimeMillis();
            String expiresMinus = PropertiesUtil.getPropertyValueByKey("token.expires.mins");
            Long expires = Long.parseLong(expiresMinus) * 60;
            long expiresMillis = nowMillis+expires * 1000;
            String username = accountToken.getAnaAccount().getAccount();
            String accessToken = JWTTokenUtil.generateToken(username, TokenType.USER_TOKEN, expiresMillis);
            accountToken.setToken(accessToken);
            accountToken.setExpriedTime(new Date(expiresMillis));
            anaAccountAccessTokenRepository.save(accountToken);
            dto.setAccessToken(accessToken);
            dto.setExpriesIn(expires);
            return dto;
        }
        return null;
    }

    @Override
    public synchronized void syncToken(AccountAccessTokenDto token) {
        try {
            AnaAccount anaAccount = anaAccountRepository.findOne(token.getAccount());
            AnaAccountAccessToken anaAccountAccessToken = anaAccount.getAccountToken();
            long nowMillis = System.currentTimeMillis();
            String expiresMinus = PropertiesUtil.getPropertyValueByKey("token.expires.mins");
            Long expires = Long.parseLong(expiresMinus) * 60 ;
            long expiresMillis = nowMillis+expires * 1000;
            String accessToken = JWTTokenUtil.generateToken(anaAccount.getAccount(), TokenType.USER_TOKEN, expiresMillis);
            Date now = new Date(expiresMillis);
            if(null == anaAccountAccessToken){
                anaAccountAccessToken = new AnaAccountAccessToken();
                anaAccountAccessToken.setToken(token.getToken());
                anaAccountAccessToken.setAnaAccount(anaAccount);
                anaAccountAccessToken.setExpriedTime(token.getExpriedTime());
            }else {
                anaAccountAccessToken.setToken(token.getToken());
                anaAccountAccessToken.setExpriedTime(token.getExpriedTime());
                Date n = new Date();
                if(n.after(anaAccountAccessToken.getExpriedTime())){
                    anaAccountAccessToken.setToken(accessToken);
                    anaAccountAccessToken.setExpriedTime(now);
                }else {
                    expires = (anaAccountAccessToken.getExpriedTime().getTime()-n.getTime())/1000;
                }
            }
            anaAccountAccessTokenRepository.save(anaAccountAccessToken);

            TokenType tokenType = JWTTokenUtil.getTokenType(token.getToken());
            if(TokenType.USER_TOKEN.equals(tokenType)){
                UserDetails userDetails = userService.getUserDetailByToken(token.getToken());
                if(null!=userDetails){
                    AnaPrincipalAuthenticationToken authentication = new AnaPrincipalAuthenticationToken(userDetails,token.getToken(),token.getRemoteAddr());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            if(token.isUpdateStatus() && anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode())){
                anaAccount.setStatus(AccountStatus.Active.getCode());
                anaAccountRepository.save(anaAccount);
            }
        } catch (Exception e) {
            log.error("syncToken",e);
        }
    }

    @Override
    public RestfulResponse<String> loginCheck(LoginDto loginDto) {
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        List<AnaAccount> accountList = anaAccountRepository.findByAccount(loginDto.getUsername());
        AnaAccount account = accountList.stream().filter(item -> item.getStatus().equals("ACT")).findFirst().orElse(null);
        if(account == null){
        	account = accountList.stream().findFirst().orElse(null);
        }
        if(null == account){
            throw new BusinessException(SystemMsg.LoginMsg.LOGIN_ACCOUNT_ERROR.getErrorCode());
        } else if(account.getStatus().equals(AccountStatus.Inactive.getCode())){
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_status_error.getErrorCode(),new String[]{AccountStatus.Inactive.name()});
        } else if(account.getStatus().equals(AccountStatus.Terminated.getCode())){
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_status_error.getErrorCode(),new String[]{AccountStatus.Terminated.name()});
        }
        String userPassword  = DigestUtils.md5DigestAsHex((loginDto.getPassword() + loginDto.getUsername()).getBytes());
        if(!userPassword.equals(account.getPassword())){
            throw new BusinessException(SystemMsg.LoginMsg.LOGIN_PASSWORD_ERROR.getErrorCode());
        }
        if(account.getStatus().equals(AccountStatus.NotVerified.getCode())){
            restfulResponse.setData(account.getFirstName()+"@"+account.getLastName());
        }
        restfulResponse.setSuccessStatus();
        return restfulResponse;
    }
}
