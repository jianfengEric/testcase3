package com.tng.portal.ana.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.constant.*;
import com.tng.portal.ana.entity.*;
import com.tng.portal.ana.repository.*;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.EwpCallerService;
import com.tng.portal.ana.service.RoleService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.ana.util.*;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.entity.EmailMessage;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.repository.CommonRepository;
import com.tng.portal.common.service.AnaApplicationService;
import com.tng.portal.common.service.EmailService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.DateUtils;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.PageQuery;
import com.tng.portal.common.vo.rest.EmailParameterVo;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Zero on 2016/11/9.
 */
@Transactional
@Service
public class AccountServiceImpl implements AccountService {
    private transient Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    public static final String EMAIL_TYPE_REGISTER_ACCOUNT = "EMAIL_TYPE_REGISTER_ACCOUNT";
    public static final String EMAIL_TYPE_RESEND_ACTIVATION = "EMAIL_TYPE_RESEND_ACTIVATION";
    public static final String EMAIL_TYPE_RESET_ENCRYPTEDPASS = "EMAIL_TYPE_RESET_PASSWORD";//sonar modify 

    @Autowired
    private AnaAccountRepository anaAccountRepository;
    @Autowired
    private AnaRoleRepository roleRepository;
    @Autowired
    private AnaApplicationRepository applicationRepository;
    @Autowired
    private AnaRoleFunctionPermissionRepository roleFunctionPermissionRepository;
    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;
    @Autowired
    private AnaLoginSessionRepository loginSessionRepository;

    @Autowired
    private AnaPermissionRepository anaPermissionRepository;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;
    
    @Autowired
    private AnaApplicationService anaApplicationService;


    @Autowired
    @Qualifier("httpClientUtils")
    private HttpClientUtils httpClientUtils;

    @Autowired
    @Qualifier("remoteClientUtils")
    private RemoteClientUtils remoteClientUtils;
    
    @Qualifier("commonEmailService")
    @Autowired
    private EmailService commonEmailService;

    @Autowired
    private AnaAccountApplicationRepository anaAccountApplicationRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private RoleService roleService;

    @Qualifier("anaEwpCallerService")
    @Autowired
    private EwpCallerService ewpCallerService;

    private static final String ROLES="roles";
    private static final String STATUS="status";
    private static final String SUCCESS="success";
    private static final String DATA="data";
    private static final String NAME="name";

    @Override
    public List<AnaAccount> getAccounts() {
        return anaAccountRepository.findAll();
    }

    @Override
    public RestfulResponse<String> bindingAccount(BindingAccountPostDto postDto)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto || null == postDto.getId() || null == postDto.getBindingData()){
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        AnaAccount anaAccount = anaAccountRepository.findById(postDto.getId());
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        if(!(anaAccount.getStatus().equals(AccountStatus.Active.getCode()) || anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode()))){
            throw new BusinessException(SystemMsg.ErrorMsg.IncorrectAccountStatus.getErrorCode());
        }
        Account loginAccount = userService.getCurrentAccountInfo();
        for(AnaAccountApplication app : anaAccount.getAnaAccountApplications()){
            Optional<AnaAccountApplicationDto> optional = postDto.getBindingData().stream().filter(item->item.getApplicationCode().equals(app.getAnaApplication().getCode())).findFirst();
            if(optional.isPresent()){
                postDto.getBindingData().remove(optional.get());
            }else {
                anaAccountApplicationRepository.delete(app.getId());
            }
        }
        for (AnaAccountApplicationDto applicationDto : postDto.getBindingData()) {
            AnaApplication anaApplication = applicationRepository.findByCode(applicationDto.getApplicationCode());
            if (null == anaApplication || !anaApplication.getDisplay()) {
                throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_application.getErrorCode());
            }
            Account client = getClientAccountByUserName(applicationDto.getAccount(), null, anaApplication.getInternalEndpoint());
            if (null == client){
                throw new BusinessException(SystemMsg.ServerErrorMsg.LOGIN_ID_NOT_EXISTS.getErrorCode(), new String[]{applicationDto.getAccount()});
            }else{
                if(!checkMid(anaAccount.getExternalGroupId(), client.getExternalGroupId())){
                    throw new BusinessException(SystemMsg.ErrorMsg.EnterSameMidAccountSso.getErrorCode(), new String[]{applicationDto.getAccount()});
                }
            }
            List<AnaAccountApplication> list = anaAccountApplicationRepository.findByAnaApplication_CodeAndBindingAccountId(anaApplication.getCode(), client.getAccountId());
            if(null != list && !list.isEmpty()){
                throw new BusinessException(SystemMsg.ServerErrorMsg.BINDING_ERROR_LOGIN_ID.getErrorCode(), new String[]{applicationDto.getAccount()});
            }
            // Judging user status 
            if(client.getStatus().equals(AccountStatus.Terminated) || client.getStatus().equals(AccountStatus.Inactive)){
                throw new BusinessException(SystemMsg.ServerErrorMsg.BINDING_ERROR_LOGIN_ID.getErrorCode(), new String[]{applicationDto.getAccount()});
            }

            AnaAccountApplication anaAccountApplication = new AnaAccountApplication();
            anaAccountApplication.setAnaAccount(anaAccount);
            anaAccountApplication.setAnaApplication(anaApplication);
            anaAccountApplication.setCreatedTime(new Date());
            anaAccountApplication.setCreatedBy(loginAccount.getUsername());
            anaAccountApplication.setBindingAccountId(client.getAccountId());
            anaAccountApplication.setStatus(AccountApplicationStatus.Temporary.getCode());
            anaAccountApplicationRepository.saveAndFlush(anaAccountApplication);
        }

        restResponse.setSuccessStatus();
        return restResponse;
    }

    private boolean checkMid(String local, String client){
        if(StringUtils.isBlank(local) && StringUtils.isBlank(client)){
            return true;
        }
        return StringUtils.isNotBlank(local) && StringUtils.isNotBlank(client) && local.equals(client);//sonarmodify
    }

    private Account getClientAccountByUserName(String userName, String accountId, String url)  {
        try {
            AccountUsernameDto pram = new AccountUsernameDto();
            pram.setUsername(userName);
            pram.setAccountId(accountId);
            String getAccountInfoByNameOrId = PropertiesUtil.getPropertyValueByKey("remote.account.getAccountInfoByNameOrId");
            String restfulResponse =  httpClientUtils.postSendJson(url + getAccountInfoByNameOrId, String.class, pram);
            if(null != restfulResponse){
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(restfulResponse).getAsJsonObject();
                if(jsonObject.has(STATUS) && jsonObject.get(STATUS).getAsString().equals(SUCCESS) && jsonObject.has(DATA)){
                    Account client = gson.fromJson(jsonObject.get(DATA), Account.class);
                    if(null != client){
                        return client;
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorAccessingAccountInformation.getErrorCode());
        }
        return null;
    }

    @Override
    public RestfulResponse<String> addAccountRoles(AddAccountRolePostDto postDto)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(null == postDto || null == postDto.getId() || null == postDto.getRoles()){
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        AnaAccount anaAccount = anaAccountRepository.findById(postDto.getId());
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        Account loginAccount = userService.getCurrentAccountInfo();
        List<AnaRole> anaRoles = postDto.getRoles().stream().map(item -> {
            return roleRepository.findById(item);
        }).collect(Collectors.toList());
        anaAccount.setAnaRoles(anaRoles);
        anaAccount.setUpdatedBy(loginAccount.getUsername());
        anaAccount.setUpdatedTime(new Date());
        anaAccountRepository.save(anaAccount);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public RestfulResponse<String> newCreateAccount(String ip, AddAccountPostDto postDto)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();

        Account loginAccount = userService.getCurrentAccountInfo();
        String account = postDto.getAccount();
        AnaAccount anaAccount = anaAccountRepository.findByAccountIgnoreCaseAndStatusNot(account, AccountStatus.Terminated.getCode());
        if (Objects.nonNull(anaAccount)) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_has_been_occupied.getErrorCode());
        }

        String passStr = StringUtil.getRandomString(6);
        postDto.setPassword(passStr);// Password for sub module use 
        String password = DigestUtils.md5DigestAsHex((passStr + account).getBytes());
        anaAccount = new AnaAccount();
        anaAccount.setAccount(account);
        anaAccount.setEmail(postDto.getEmail());
        anaAccount.setMobile(postDto.getMobile());
        anaAccount.setFirstName(postDto.getFirstName());
        anaAccount.setLastName(postDto.getLastName());
        anaAccount.setPassword(password);
        anaAccount.setLanguage("en");
        anaAccount.setCreatedBy(loginAccount.getUsername());
        anaAccount.setCreatedTime(new Date());
        anaAccount.setIpAddress(ip);
        anaAccount.setStatus(AccountStatus.NotVerified.getCode());
        anaAccount.setVerifyEmailType(EmailSendTo.User.getCode());
        anaAccount.setUserType(postDto.getUserType().getCode());

        if(StringUtils.isNotBlank(postDto.getExternalGroupId())){
            anaAccount.setInternal(false);
            ParticipantDto participant = getParticipant(postDto.getExternalGroupId());
            if(Objects.isNull(participant)){
                throw new BusinessException(SystemMsg.ServerErrorMsg.NOTFINDMERCHANT.getErrorCode());
            }
            anaAccount.setMerchantId(Long.valueOf(participant.getParticipantId()));
            roleService.autoCreateRole(postDto.getExternalGroupId());
            anaAccount.setExternalGroupId(postDto.getExternalGroupId());//ewp geaParticipantRefId
            List<AnaRole> list = roleRepository.findByRoleTypeAndMid(RoleType.findByUserType(postDto.getUserType().name()), postDto.getExternalGroupId());
            anaAccount.setAnaRoles(list);
        } else {
            anaAccount.setInternal(true);
            anaAccount.setDepartmentId(Long.valueOf(postDto.getDepartment().ordinal()));
            if(postDto.getUserType() == AccountType.Admin){
                List<AnaRole> list = roleRepository.findByRoleType(Arrays.asList(RoleType.GEA_ADMIN.name()));
                anaAccount.setAnaRoles(list);
            }else{
                List<AnaRole> list = roleRepository.findByRoleType(RoleType.findByDepartmentAndUserType(postDto.getDepartment().name(), postDto.getUserType().name()));
                anaAccount.setAnaRoles(list);
            }
        }

        AnaAccount saveAccount = anaAccountRepository.saveAndFlush(anaAccount);

        if(PropertiesUtil.getServiceName().equals(ApplicationContext.Modules.ANA)){
            newBindingAccount(postDto, anaAccount.getId());
        }

        if(StringUtils.isNotBlank(postDto.getEmail())){
            sendEmail(EMAIL_TYPE_REGISTER_ACCOUNT,saveAccount);
        }

        restResponse.setData(saveAccount.getId());

        return restResponse;
    }


    private Map<String,List<String>> newBindingAccount(AddAccountPostDto postDto , String anaAccountId) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        List<String> agentFail = new ArrayList<>();
        AnaAccount anaAccount = anaAccountRepository.findById(anaAccountId);
        List<AnaApplication> anaApplicationList = applicationRepository.findAll();
        for (AnaApplication anaApplication : anaApplicationList) {
            if (null == anaApplication || !anaApplication.getDisplay()
                    || anaApplication.getCode().equals(ApplicationContext.Modules.ANA)
                    || anaApplication.getCode().equals(ApplicationContext.Modules.DPY)
                    || anaApplication.getCode().equals(ApplicationContext.Modules.AGE)
                    || anaApplication.getCode().equals(ApplicationContext.Modules.MSG)
                    || anaApplication.getCode().equals(ApplicationContext.Modules.ENY)) {
                continue;
            }
            try {
                RestfulResponse<String> restfulResponse = remoteClientUtils.addSsoAccount(postDto, anaApplication.getInternalEndpoint(),anaApplication.getCode());
                log.info("bindingAccount() restfulResponse:{}", restfulResponse);
                if(Objects.isNull(restfulResponse)){
                    fail.add(anaApplication.getCode());
                    continue;
                }
                if (restfulResponse.hasFailed()) {
                    if (SystemMsg.ServerErrorMsg.exist_merchant_agent.getCode().equals(restfulResponse.getErrorCode())) {
                        agentFail.add(anaApplication.getCode());
                    } else {
                        fail.add(anaApplication.getCode());
                    }
                    continue;
                }
                String bindingAccountId = restfulResponse.getData();
                success.add(anaApplication.getCode());

                createAccountApplication(anaAccount, anaApplication, bindingAccountId);
            } catch (Exception e) {
                fail.add(anaApplication.getCode());
                log.error("sso create client account error", e);
            }
        }
        map.put("success", success);
        map.put("fail", fail);
        map.put("agent", agentFail);
        return map;
    }

    private void createAccountApplication(AnaAccount anaAccount,AnaApplication anaApplication, String bindingAccountId){
        if(anaAccount == null || anaApplication == null || StringUtils.isEmpty(bindingAccountId)){
            log.error("createAppAccountApplication() anaAccount:{} anaApplication:{} bindAccountId:{}",
                    anaAccount,anaApplication,bindingAccountId);
            return;
        }
        AnaAccountApplication anaAccountApplication = new AnaAccountApplication();
        anaAccountApplication.setAnaAccount(anaAccount);
        anaAccountApplication.setAnaApplication(anaApplication);
        anaAccountApplication.setCreatedTime(new Date());
        anaAccountApplication.setCreatedBy(anaAccount.getFullName());
        anaAccountApplication.setBindingAccountId(bindingAccountId);
        anaAccountApplication.setStatus(AccountApplicationStatus.Temporary.getCode());
        anaAccountApplicationRepository.saveAndFlush(anaAccountApplication);
    }

   /* private void sendEmail(String pwd, String account,  String receiver, String password, String mid){
    	String model = ApplicationContext.get(ApplicationContext.Key.serviceName);
        EmailParameterVo emailParameterVo = new EmailParameterVo();
        emailParameterVo.setJob("sendRegisteredAccountEmail");
        emailParameterVo.setReceivers(receiver);
        LinkedHashMap<String,String> templateInput = new LinkedHashMap<>();
        templateInput.put("account", account);
        templateInput.put("password", pwd);
        templateInput.put("companyNameEn", StringUtils.isBlank(mid)?"TNG":mid);
        String url = StringUtil.getPropertyValueByKey("registration.activation.link.path");
        String token = JWTTokenUtil.generateToken(account, password);
        templateInput.put("link", url + "model="+model+"&code=" + token);
        emailParameterVo.setTemplateInput(templateInput);
        try {
            commonEmailService.sendByHttp(emailParameterVo);
        }catch (Exception e){
//            logger.error("mam send email error", e);
        }
    }*/

    private void sendEmail(String type, AnaAccount account){
    	EmailMessage emailMessage = commonEmailService.saveEmailMessage(null, null, null, null, null, null, null, null, null);
    	Account currentUser = userService.getCurrentAccountInfo();
        String model = PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName);
        EmailParameterVo emailParameterVo = new EmailParameterVo();
        emailParameterVo.setSenderId(currentUser.getAccountId());
        emailParameterVo.setReceiversId(account.getId());
        emailParameterVo.setReceivers(account.getEmail());
        if(StringUtils.isBlank(account.getExternalGroupId())){
            emailParameterVo.setJob("sendRegisteredInsideAccountEmail");
        } else {
            emailParameterVo.setJob("sendRegisteredParticipantAccountEmail");
        }
        LinkedHashMap<String,String> templateInput = new LinkedHashMap<>();
        templateInput.put("account", account.getAccount());
        String url = PropertiesUtil.getPropertyValueByKey("registration.activation.link.path");
        String token = JWTTokenUtil.generateToken(account.getAccount(), account.getPassword(),emailMessage.getId().toString());
        String register = DigestUtils.md5DigestAsHex("register".getBytes());
        String active = DigestUtils.md5DigestAsHex("active".getBytes());
        String pwd = DigestUtils.md5DigestAsHex("pwd".getBytes());
        String modelMd5 = DigestUtils.md5DigestAsHex(model.getBytes());

        switch (type) {
            case EMAIL_TYPE_REGISTER_ACCOUNT:
                templateInput.put("link", url + "model="+modelMd5+"&code=" + token+"&type="+register);
                break;
            case EMAIL_TYPE_RESEND_ACTIVATION:
                templateInput.put("link", url + "model="+modelMd5+"&code=" + token+"&type="+active);
                break;
            case EMAIL_TYPE_RESET_ENCRYPTEDPASS:
                emailParameterVo.setJob("sendResetPasswordEmail");
                templateInput.put("link", url + "model="+modelMd5+"&code=" + token+"&type="+pwd);
                break;
            default:
                log.error("Unknown email type:{}, account:{} mid:{} receiver:{}",type,account.getAccount(),account.getExternalGroupId(),account.getEmail());
        }
        emailParameterVo.setTemplateInput(templateInput);
        try {
            commonEmailService.sendByHttp(emailParameterVo,emailMessage.getId());
        }catch (Exception e){
            log.error("send email error type:{} account:{} mid:{} receiver:{}",type,account.getAccount(),account.getExternalGroupId(),account.getEmail(),e);
        }
    }


    /**
     * Update Account info
     * 
      * @param ip
     * 			client's ip address
     * 
     * @param updateDto
     * 			updated ANA account info
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<String> updateAccount(String ip, AccountUpdateDto updateDto)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findOne(updateDto.getId());
        if(anaAccount == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String account = updateDto.getAccount();
        if (!StringUtil.isEmpty(updateDto.getPassword())){
            if (!updateDto.getPassword().equals(updateDto.getRePassword())){
                throw new BusinessException(SystemMsg.ServerErrorMsg.sure_password.getErrorCode());
            }
            String password = DigestUtils.md5DigestAsHex((updateDto.getPassword() + account).getBytes());
            anaAccount.setPassword(password);
            anaAccount.setResetPwdTime(new Date());
        }
        String mobile = updateDto.getMobile();
        if(!StringUtil.isEmpty(mobile)){
            anaAccount.setMobile(mobile);
        }
        if (!StringUtil.isEmpty(updateDto.getEmail())){
            anaAccount.setEmail(updateDto.getEmail());
        }
        if (StringUtils.isNotBlank(updateDto.getFirstName())){
            anaAccount.setFirstName(updateDto.getFirstName());
        }
        if (StringUtils.isNotBlank(updateDto.getLastName())){
            anaAccount.setLastName(updateDto.getLastName());
        }
        List<Long> roleIds = updateDto.getRoles();
        List<AnaRole> anaRoles = roleIds.stream().map(item -> {
            return roleRepository.findOne(item);
        }).collect(Collectors.toList());
        anaAccount.setAnaRoles(anaRoles);

        String externalGroupId = updateDto.getExternalGroupId();
        if(!StringUtil.isEmpty(externalGroupId)){
            anaAccount.setExternalGroupId(externalGroupId);
        }
        Account loginAccount = userService.getCurrentAccountInfo();
        if (loginAccount != null){
        	anaAccount.setUpdatedBy(loginAccount.getUsername());
        }
        anaAccount.setUpdatedTime(new Date());
        anaAccount.setIpAddress(ip);
        restResponse.setData(anaAccount.getId());

        return restResponse;
    }

    /**
     * Update account profile such as name, email, language
     * 
      * @param ip
     * 			client's ip address
     * 
     * @param updateDto
     * 			updated ANA account profile
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<String> updateProfile(String ip, ProfileUpdateDto updateDto)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findOne(updateDto.getId());
        if(anaAccount == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String newMobile = updateDto.getMobile();
        if(!StringUtil.isEmpty(newMobile)){
            anaAccount.setMobile(newMobile);
        }
        if (StringUtils.isNotBlank(updateDto.getFirstName())){
            anaAccount.setFirstName(updateDto.getFirstName());
        }
        if (StringUtils.isNotBlank(updateDto.getLastName())){
            anaAccount.setLastName(updateDto.getLastName());
        }
        if (!StringUtil.isEmpty(updateDto.getEmail())){
            anaAccount.setEmail(updateDto.getEmail());
        }
        if (!StringUtil.isEmpty(updateDto.getLanguage())){
            anaAccount.setLanguage(updateDto.getLanguage());
        }
        Account loginAccount = userService.getCurrentAccountInfo();
        if (loginAccount != null){
        	anaAccount.setUpdatedBy(loginAccount.getUsername());
        }
        anaAccount.setUpdatedTime(new Date());
        anaAccount.setIpAddress(ip);

        restResponse.setData(anaAccount.getId());
        return restResponse;
    }

    @Override
    public RestfulResponse<String> updateStatus(String ip, String id, String status)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount;
        Account loginAccount = userService.getCurrentAccountInfo();
        if(null == id || id.isEmpty()){
            anaAccount = anaAccountRepository.findById(loginAccount.getAccountId());
        }else {
            anaAccount = anaAccountRepository.findById(id);
        }
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        AccountStatus newStatus = AccountStatus.valueOf(status);
        if(anaAccount.getStatus().equals(AccountStatus.Terminated.getCode()) || newStatus.equals(AccountStatus.NotVerified)){
            throw new BusinessException(SystemMsg.ErrorMsg.IncorrectAccountStatus.getErrorCode());
        }
        if(anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode()) &&
                (newStatus.equals(AccountStatus.Inactive) || newStatus.equals(AccountStatus.Terminated))){
            anaAccount.setStatus(newStatus.getCode());
            anaAccountRepository.save(anaAccount);
        }else if(anaAccount.getStatus().equals(AccountStatus.Active.getCode()) &&
                (newStatus.equals(AccountStatus.Inactive) || newStatus.equals(AccountStatus.Terminated))){
            anaAccount.setStatus(newStatus.getCode());
            anaAccountRepository.save(anaAccount);
        }else if(anaAccount.getStatus().equals(AccountStatus.Inactive.getCode()) &&
                (newStatus.equals(AccountStatus.Active) || newStatus.equals(AccountStatus.Terminated))){
            anaAccount.setStatus(newStatus.getCode());
            anaAccountRepository.save(anaAccount);
        }else {
            throw new BusinessException(SystemMsg.ErrorMsg.IncorrectAccountStatus.getErrorCode());
        }

        updateClientStatus(anaAccount, status);

        restResponse.setSuccessStatus();
        restResponse.setData(anaAccount.getId());
        return restResponse;
    }


    /**
     * Update account password
     * 
      * @param ip
     * 			client's ip address
     * 
     * @param passDto
     * 			updated password info
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<String> updatePassword(String ip, AccountUpPassDto passDto)  {
        Account loginAccount = userService.getCurrentAccountInfo();
        if (loginAccount==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_token_account.getErrorCode());
        }
        AnaAccount anaAccount = anaAccountRepository.findOne(loginAccount.getAccountId());
        if(anaAccount == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String originalPassword = passDto.getOriginalPassword();
        String newPassword = passDto.getNewPassword();
        String reNewPassword = passDto.getReNewPassword();
        if (newPassword == null||newPassword.equals("")){
            throw new BusinessException(SystemMsg.ServerErrorMsg.empty_password_error.getErrorCode());
        }
        if (!newPassword.equals(reNewPassword)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.sure_password.getErrorCode());
        }
        String password = DigestUtils.md5DigestAsHex((originalPassword + anaAccount.getAccount()).getBytes());
        if (!password.equals(anaAccount.getPassword())){
            throw new BusinessException(SystemMsg.ServerErrorMsg.original_password_error.getErrorCode());
        }
        password = DigestUtils.md5DigestAsHex((newPassword + anaAccount.getAccount()).getBytes());
        anaAccount.setPassword(password);
        anaAccount.setResetPwdTime(new Date());
        anaAccount.setUpdatedBy(loginAccount.getUsername());
        anaAccount.setUpdatedTime(new Date());
        anaAccount.setIpAddress(ip);
        
        this.logout(anaAccount.getId());
        return RestfulResponse.ofData(anaAccount.getId());
    }

    /**
     * Query current account token info by token
     * 
     * @param token
     * 			token string
     * 
     * @return
     * @
     */
    @Override
    public AnaAccountAccessToken getAccountTokenByToken(String token) {
        return anaAccountAccessTokenRepository.findByToken(token);
    }


    /**
     * Update user password
     * 
     * @param validDto
     * 			include valid code, new password
     * 
     * @return
     * @
     */
    @Override
    public void updatePasswordByValidCode(ValidDto validDto) {
        String validCode = validDto.getValidCode();
        String userAccount = JWTTokenUtil.getUserAccount(validCode);
        if(userAccount==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.VALID_LINK_ERROR.getErrorCode());
        }
        Date generateDate = JWTTokenUtil.getIssuedAt(validCode);
        if(null==generateDate){
            throw new BusinessException(SystemMsg.ServerErrorMsg.VALID_LINK_ERROR.getErrorCode());
        }
        Date restDate = DateUtil.addDate(generateDate, Calendar.MINUTE, 30);
        Date now = new Date();
        if(restDate.before(now)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.VALID_LINK_EEPIRED.getErrorCode());
        }

        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(userAccount, AccountStatus.Terminated.getCode());
        if(anaAccount == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }

        String oldPassword = JWTTokenUtil.getSubject(validCode);
        if(!oldPassword.equals(anaAccount.getPassword())){
            throw new BusinessException(SystemMsg.ServerErrorMsg.VALID_LINK_ERROR.getErrorCode());
        }

        String password = validDto.getPassword();
        String repassword = validDto.getRepassword();
        if(null==password){
            throw new BusinessException(SystemMsg.ServerErrorMsg.empty_password_error.getErrorCode());
        }
        if(!password.equals(repassword)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.password_not_eauals_repassword.getErrorCode());
        }

        String regex = "^[0-9A-Za-z]{8,16}$";
        if(!password.matches(regex)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.sure_password.getErrorCode());
        }

        String md5password = DigestUtils.md5DigestAsHex((password + userAccount).getBytes());
        anaAccount.setPassword(md5password);
    }

    /**
     * Delete account by account id , just mark the user's is_active field to false, but not physical delete.
     * 
      * @param ip
     * 			client's ip address
     * 
     * @param id
     * 			ref. ANA_ACCOUNT.ID
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<String> deleteAccount(String ip, String id)  {
        Account loginAccount = userService.getCurrentAccountInfo();
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findOne(id);
        if (anaAccount == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        anaAccount.setAnaRoles(null);
        AnaAccountAccessToken accountToken = anaAccount.getAccountToken();
        if (accountToken != null){
        	anaAccountAccessTokenRepository.delete(accountToken);
        }
        if (loginAccount != null){
        	anaAccount.setUpdatedBy(loginAccount.getUsername());
        }
        anaAccount.setUpdatedTime(new Date());
        anaAccount.setIpAddress(ip);

        restResponse.setData(id);
        return restResponse;
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
    @Override
    public RestfulResponse<List<AccountDto>> getAccountByRoleName(String roleName,boolean exceptLoginAccount)  {

        RestfulResponse<List<AccountDto>> restResponse = new RestfulResponse<>();
        List<AnaRole> listRoles = roleRepository.findByName(roleName);
        if (listRoles.isEmpty()){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_role.getErrorCode());
        }
        List<AnaAccount> list = anaAccountRepository.findByAnaRoles(listRoles.get(0));

        List<AccountDto> accountDtos = new ArrayList<>();
        for (AnaAccount item:list){
            AccountDto dto = new AccountDto();
            dto.setId(item.getId());
            dto.setAccount(item.getAccount());
            dto.setFirstName(item.getFirstName());
            dto.setLastName(item.getLastName());
            dto.setMobile(item.getMobile());
            dto.setCreatedTime(item.getCreatedTime());
            accountDtos.add(dto);
        }

        restResponse.setData(accountDtos);
        return restResponse;
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
     * @
     */
    @Override
    public RestfulResponse<PageDatas> getAccountByExternalGroup(String externalGroupId, Integer pageNo, Integer pageSize, String sortBy, String isAscending,String searchBy,String search)  {
        if (pageNo>0){
            pageNo -= 1;
        }
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<ExternalAccountDto> pageDatas = new PageDatas<>(pageNo,pageSize);

        //added by Kaster 20170214
        Specifications<AnaAccount> where = Specifications.where((root,criteriaQuery,criteriaBuilder) -> criteriaBuilder.equal(root.get("externalGroupId").as(String.class),externalGroupId));
        where = where.and((root,criteriaQuery,criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isActive")));

        if(null!=search && !"".equals(search)){
            final String finalSearch = search.toLowerCase();
            if("account".equals(searchBy)){
                where = where.and(((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("account")),"%"+ finalSearch +"%")));
            }
            if("name".equals(searchBy)){
                where = where.and(((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),"%"+ finalSearch +"%")));
            }
        }

        List<AnaAccount> list = new ArrayList<>();
        Sort.Direction direction = isAscending.equals("true")?Sort.Direction.ASC:Sort.Direction.DESC;
        if (null == sortBy || sortBy.isEmpty()){
            sortBy = "account";
        }
        Sort sort = new Sort(direction,sortBy);
        PageRequest pageRequest = new PageRequest(pageNo,pageSize,sort);
        if(sortBy.equals("moneyPoll")){
            pageRequest = new PageRequest(pageNo,pageSize);
        }
        Page<AnaAccount> page = anaAccountRepository.findAll(where, pageRequest);
        list = page.getContent();
        pageDatas.initPageParam(page);
        List<ExternalAccountDto> accountDtos = new ArrayList<>();
        for (AnaAccount item:list){
            ExternalAccountDto dto = new ExternalAccountDto();
            dto.setId(item.getId());
            dto.setAccount(item.getAccount());
            dto.setName(item.getFullName());
            accountDtos.add(dto);
        }
        pageDatas.setList(accountDtos);
        restResponse.setData(pageDatas);
        return restResponse;
    }

    /**
     * User logout the ANA system
     * 
     * @param request HttpServletRequest
     * @return
     * @
     */
    @Override
    public String logout(String accountId)  {
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if(anaAccount == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        AnaAccountAccessToken anaAccountToken = anaAccountAccessTokenRepository.findByAnaAccount(anaAccount);
        if (null != anaAccountToken){
            anaAccountAccessTokenRepository.delete(anaAccountToken);
            //删除子模块的token
            String[] modulesArr = new String[]{
					ApplicationContext.Modules.EWP,
					ApplicationContext.Modules.MP,
					ApplicationContext.Modules.APV,
					ApplicationContext.Modules.TRE,
					ApplicationContext.Modules.SRV,
					ApplicationContext.Modules.ORD
					};
            for(String module : modulesArr){
            	Map<String, String> param = new HashMap<>();
            	param.put("account", anaAccount.getAccount());
            	param.put("consumer", PropertiesUtil.getAppValueByKey("service.name"));
            	param.put("apiKey", PropertiesUtil.getAppValueByKey(module.toLowerCase() + ".comment.api.key"));
            	httpClientUtils.httpGet(anaApplicationService.findByCode(module).getInternalEndpoint().concat("/remote/account/delete-ana-token-by-user"), String.class, param);
            	
            }
        }

       /* if(ApplicationContext.Env.integrated.equals(ApplicationContext.get(ApplicationContext.Key.integratedStyle))
                &&ApplicationContext.Communication.MQ.equals(ApplicationContext.get(ApplicationContext.Key.communicationStyle))){
            Packet packet = new Packet();

            Map<String,AnaAccountAccessTokenDto> mapping = new HashMap<>();

            packet.setMethod(AnaIoHandlerAdapter.Method.clearToken);
            packet.setKey(UUID.randomUUID().toString());
            List<String> applications = new ArrayList<>();
            for(AnaAccountApplication anaAccountApplication:anaAccount.getAnaAccountApplications()) {
                AnaApplication anaApplication = anaAccountApplication.getAnaApplication();
                applications.add(anaApplication.getCode());
                AnaAccountAccessTokenDto anaAccountAccessTokenDto = new AnaAccountAccessTokenDto();
                anaAccountAccessTokenDto.setAccountid(anaAccountApplication.getBindingAccountId());
                anaAccountAccessTokenDto.setExpriedtime(DateUtils.formatDate(anaAccountToken.getExpriedTime()));
                anaAccountAccessTokenDto.setRemoteaddr(request.getRemoteAddr());
                anaAccountAccessTokenDto.setToken(anaAccountToken.getToken());
                mapping.put(anaApplication.getCode(),anaAccountAccessTokenDto);
            }
            packet.setData(mapping);
            packet.setApplications(applications);
            ObjectMapper mapper=new ObjectMapper();
            String requestString = null;
            try {
                requestString = mapper.writeValueAsString(packet);
                NioSocketClient.write(requestString,null);
            } catch (Exception e) {
                log.warn("clear token request fail",e);
            }

        }
        writeLoginOrLogoutLog(request, anaAccount, false);*/
        return anaAccount.getId();
    }

    /**
     * Write login Or logout log to database
     * 
     * @param request HttpServletRequest
     * 
     * @param anaAccount
     * 			ANA account info
     * 
     * @param isLogin
     * 			true--login or false--logout
     */
    @Override
    public void writeLoginOrLogoutLog(HttpServletRequest request,AnaAccount anaAccount,boolean isLogin) {
        AnaLoginSession loginSession = new AnaLoginSession();
        String ip = ToolUtil.getRemoteHost(request);
        loginSession.setAccount(anaAccount.getAccount());
        loginSession.setName(anaAccount.getFullName());
        loginSession.setMobile(anaAccount.getMobile());
        loginSession.setEmail(anaAccount.getEmail());
        loginSession.setLanguage(anaAccount.getLanguage());
        loginSession.setIp(ip);
        loginSession.setSessionDateTime(new Date());
        loginSession.setIsLogin(isLogin);
        loginSessionRepository.save(loginSession);
    }
    
    /**
     * Query user's authentication info by token
     * 
     * @param token
     * 			user token string
     * 
     * @return
     * @
     */
    @Override
    public Account getAuthuserInfoByToken(String token) {
        AnaAccountAccessToken accountToken = anaAccountAccessTokenRepository.findByToken(token);
        if(accountToken == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_available_token.getErrorCode());
        }
        AnaAccount anaAccount = accountToken.getAnaAccount();
        if(anaAccount == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        Account account = new Account();
        account.fill(anaAccount);
        return account;
    }
    


    /**
     * check whether the account has provided permission
     * 
     * @param accountId
     * 			ref. ANA_ACCOUNT.ID
     * 
     * @param permessionType
     * 			permission type, such as CREATE, VIEW, EDIT, DELETE
     *
     * @return
     * @
     */
    @Override
    public Boolean hasPermession(String accountId, TopupPermession permessionType) {
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if(null==anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }

        List<AnaPermission> anaPermissions = anaPermissionRepository.findByName(permessionType.name());
        if(anaPermissions.isEmpty()){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_permission.getErrorCode());
        }

        AnaPermission permission = anaPermissions.get(0);
        List<AnaRole> anaRoles = anaAccount.getAnaRoles();
        for(AnaRole role:anaRoles){
            List<AnaRoleFunctionPermission> functionPermissions = role.getAnaRoleFunctions();
            for(AnaRoleFunctionPermission anaRoleFunctionPermission:functionPermissions){
                int  permissionSum = anaRoleFunctionPermission.getPermissionsSum();
                int permissionId = permission.getId();
                if((permissionSum & permissionId)==permissionId){
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Query ANA account list
     * 
     * @param pageNo
	 * 			current page number
	 * 
	 * @param pageSize
	 * 			page size
	 * 
	 * @param accountSearch
	 * 			account search value
	 * 
	 * @param sortBy
	 * 			sort by
	 * 
	 * @param isAscending
	 * 			true--ascend or false--descend
	 * 
     * @return
     */
    @Override
    public RestfulResponse<PageDatas> listAccounts(Integer pageNo, Integer pageSize, String accountSearch, String sortBy,Boolean isAscending,
                                                    String externalGroupIdSearch, String rolesSearch, String statusSearch,String nameSearch) {
        Account account =  userService.getCurrentAccountInfo();
        Boolean internal = account.getInternal();
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        PageDatas<AccountDto> pageDatas = new PageDatas<>(pageNo,pageSize);
        List<AnaAccount> list = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.DESC, "createdTime");
        if(!StringUtil.isEmpty(sortBy) && !ROLES.equals(sortBy)){
            if(NAME.equals(sortBy)){
                Sort.Direction direction = (isAscending != null && isAscending) ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = new Sort(direction, "firstName", "lastName");
            } else {
                sort = pageDatas.pageSort(sortBy, isAscending, "createdTime");
            }
        }
        Specifications<AnaAccount> where = Specifications.where(((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("userType"), AccountType.ROOT.getCode())));
        if(StringUtils.isNotBlank(rolesSearch)) {
            where = where.and((root, criteriaQuery, criteriaBuilder) -> {
                criteriaQuery.distinct(true);
                Join<AnaAccount, AnaRole> join = root.join("anaRoles", JoinType.LEFT);
                return criteriaBuilder.like(join.get("name"), "%".concat(rolesSearch).concat("%"));
            });
        }
        if(ROLES.equals(sortBy)){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> {
                criteriaQuery.distinct(true);
                Join<AnaAccount, AnaRole> join = root.join("anaRoles", JoinType.LEFT);
                if(isAscending){
                    criteriaQuery.orderBy(criteriaBuilder.asc(join.get("name")));
                }else{
                    criteriaQuery.orderBy(criteriaBuilder.desc(join.get("name")));
                }
                return criteriaBuilder.isNotNull(join.get("id"));
            });
        }
        if(StringUtils.isNotBlank(accountSearch)) {
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("account"), "%".concat(accountSearch).concat("%")));
        }
        if(StringUtils.isNotBlank(externalGroupIdSearch)) {
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("externalGroupId"), "%".concat(externalGroupIdSearch).concat("%")));
        }
        if(StringUtils.isNotBlank(statusSearch)) {
            String status = AccountStatus.valueOf(statusSearch.replace(" ", "")).getCode();
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
        }
        if(!internal && StringUtils.isNotBlank(account.getExternalGroupId())){
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("externalGroupId"), account.getExternalGroupId()));
        }
        if(StringUtils.isNotBlank(nameSearch)) {
            where = where.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.function("CONCAT", String.class, root.get("firstName"), root.get("lastName")), "%" + nameSearch.replace(" ", "") + "%"));
        }

        if (pageDatas.isAll()){
            if(ROLES.equals(sortBy)){
                list = anaAccountRepository.findAll(where);
            }else {
                list = anaAccountRepository.findAll(where, sort);
            }
        }else {
            Page<AnaAccount> rolePage;
            if(ROLES.equals(sortBy)){
                PageRequest pageRequest = new PageRequest(pageNo,pageSize);
                rolePage = anaAccountRepository.findAll(where, pageRequest);
            }else {
                rolePage = anaAccountRepository.findAll(where, pageDatas.pageRequest(sort));
            }
            list = rolePage.getContent();
            pageDatas.initPageParam(rolePage);
        }
        //participant name
        Map<String,ParticipantDto> participantMap = new HashMap<>();
        try {
        	List<String> geaIdList = list.stream().map(item -> item.getExternalGroupId()).collect(Collectors.toList());
        	participantMap = ewpCallerService.callGetParticipantByIds(geaIdList, Instance.PRE_PROD).getData();
        } catch (Exception e) {
        	log.error(" Connect ewp fail ", e);
        }

        List<AccountDto> accountDtos = new ArrayList<>();
        for(AnaAccount item:list){
            AccountDto dto = new AccountDto();
            dto.setId(item.getId());
            dto.setAccount(item.getAccount());
            dto.setFirstName(item.getFirstName());
            dto.setLastName(item.getLastName());
            dto.setMobile(item.getMobile());
            dto.setCreatedTime(item.getCreatedTime());
            dto.setUpdatedTime(item.getUpdatedTime());
            dto.setExternalGroupId(item.getExternalGroupId());
            dto.setInternal(item.getInternal());
            List<AnaRole> rl = roleRepository.findByAnaAccounts(item);
            List<Long> rdl = new ArrayList<>();
            for (AnaRole role : rl) {
                RoleDto roleDto = new RoleDto();
                roleDto.setId(role.getId());
                roleDto.setName(role.getName());
                String appCode = PropertiesUtil.getServiceName();
                roleDto.setAnaApplication(appCode);
                roleDto.setDescription(role.getDescription());
                rdl.add(role.getId());
            }
            dto.setRoles(rdl);
            dto.setCreatedBy(item.getCreatedBy());
            dto.setUpdatedBy(item.getUpdatedBy());
            dto.setStatus(AccountStatus.getName(item.getStatus()));
            Optional<String> optional = item.getAnaAccountApplications().stream().map(app -> app.getAnaApplication().getCode()).reduce((o1, o2) -> o1 + "," + o2);
            dto.setApplications(optional.isPresent() ? optional.get() : "");
            dto.setEmail(item.getEmail());
            dto.setEmailSendTo(EmailSendTo.getName(item.getVerifyEmailType()));
            dto.setUserType(AccountType.getName(item.getUserType()));
            if (StringUtils.isNotBlank(item.getExternalGroupId()) && Objects.nonNull(participantMap.get(item.getExternalGroupId()))) {
                dto.setParticipantName(participantMap.get(item.getExternalGroupId()).getParticipantName());
            }
            dto.setDepartment(item.getDepartmentId());

            accountDtos.add(dto);
        }

        pageDatas.setList(accountDtos);
        restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();

        return restResponse;
    }

    /**
     * Query current user's authentication info by userAccount
     * 
     * @param userAccount
     * 			ref.  ANA_ACCOUNT.ACCOUNT
     * 
     * @return
     * @
     */
    @Override
    public Account getAuthUserInfoByUserAccount(String userAccount) {
        Account account = new Account();
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(userAccount, AccountStatus.Terminated.getCode());
        if (null != anaAccount) {
            account.fill(anaAccount);
        }else {
            log.error("auth valid user account:"+userAccount);
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        return account;
    }

    /**
     * Query current user's authentication info by accountId
     *
     * @param accountId
     * 			ref.  ANA_ACCOUNT.ID
     *
     * @return
     * @
     */
    @Override
    public Account getAuthUserInfoByAccountId(String accountId) {
        Account account = new Account();
        AnaAccount anaAccount = anaAccountRepository.findById(accountId);
        if (null != anaAccount) {
            account.fill(anaAccount);
        }else {
            log.error("auth valid user account:"+accountId);
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        return account;
    }
    
    /**
     * Query account list by role name string
     * 
     * @param roleName
     * 			the role name string, separated by commas
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<List<Account>> getClientAccountByRoleName(String roleName) {
        RestfulResponse<List<Account>> restResponse = new RestfulResponse<>();
        String[] names = roleName.split(",");
        List<String> nameList =Arrays.asList(names);
        List<AnaRole> listRoles = roleRepository.findByNameIn(nameList);
        if (listRoles.isEmpty()){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_role.getErrorCode());
        }
        List<AnaAccount> list = anaAccountRepository.findDistinctByAnaRolesIn(listRoles);
        List<Account> accounts = list.stream().map(item -> {
            Account account = new Account();
            account.setAccountId(item.getId());
            account.setUsername(item.getAccount());
            account.setPassword(item.getPassword());
            account.setEmail(item.getEmail());

            return account;
        }).collect(Collectors.toList());
        restResponse.setData(accounts);
        return restResponse;
    }



    /**
     * Query account detail by account id
     * 
     * @param accountId
     * 			ref. ANA_ACCOUNT.ID
     * 
     * @return
     * @
     */
    @Override
    public RestfulResponse<AccountDetailDto> getAccountDetail(String accountId)  {
        RestfulResponse<AccountDetailDto> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if (anaAccount == null) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }

        AccountDetailDto accountDetail = new AccountDetailDto();
        accountDetail.setId(anaAccount.getId());
        accountDetail.setName(anaAccount.getFullName());
        accountDetail.setAccount(anaAccount.getAccount());
        accountDetail.setMobile(anaAccount.getMobile());
        accountDetail.setEmail(anaAccount.getEmail());
        accountDetail.setExternalGroupId(anaAccount.getExternalGroupId());
        accountDetail.setInternal(anaAccount.getInternal());

        List<AnaRole> lr = roleRepository.findByAnaAccounts(anaAccount);
        List<RoleDetailDto> list = new ArrayList<>();
        for (AnaRole anaRole: lr){
            RoleDetailDto roleDetail = new RoleDetailDto();
            roleDetail.setId(anaRole.getId());
            roleDetail.setName(anaRole.getName());
            AnaApplication anaApplication = applicationRepository.findByCode(PropertiesUtil.getServiceName());

            ApplicationDto application = new ApplicationDto();
            application.setCode(anaApplication.getCode());
            application.setName(anaApplication.getName());

            roleDetail.setApplication(application);

            List<AnaRoleFunctionPermission> anaRoleFunctionPermissions = roleFunctionPermissionRepository.findByAnaRole(anaRole);
            List<RoleFunctionPermissionDto> rfpList = anaRoleFunctionPermissions.stream().map(item -> {
                RoleFunctionPermissionDto dto = new RoleFunctionPermissionDto();
                dto.setRoleId(item.getAnaRoleFunctionPk().getRoleId());
                dto.setCode(item.getAnaRoleFunctionPk().getFunctionCode());
                dto.setPermissionSum(item.getPermissionsSum());
                return dto;
            }).collect(Collectors.toList());

            roleDetail.setFunctionList(rfpList);
            list.add(roleDetail);
        }
        accountDetail.setRoles(list);
        restResponse.setData(accountDetail);

        return restResponse;
    }

    /**
     * Query account list by role name
     * @return
     */
    @Override
    public RestfulResponse<List<AccountDto>> queryAllAccounts() {
        Account loginAccount = userService.getCurrentAccountInfo();
        RestfulResponse<List<AccountDto>> restResponse = new RestfulResponse<>();
        List<AnaAccount> list = null;
        if(StringUtils.isBlank(loginAccount.getExternalGroupId())){
            list = anaAccountRepository.findAll();
        }else{
            list = anaAccountRepository.findByExternalGroupId(loginAccount.getExternalGroupId());
        }
        List<AccountDto> accountDtos = new ArrayList<>();
        for (AnaAccount item:list){
            AccountDto dto = new AccountDto();
            dto.setId(item.getId());
            dto.setFirstName(item.getFirstName());
            dto.setLastName(item.getLastName());
            accountDtos.add(dto);
        }
        restResponse.setData(accountDtos);
        return restResponse;
    }

    @Override
    public RestfulResponse<String> editUser(String remoteHost, AccountUpdateDto updateDto) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findOne(updateDto.getId());
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        anaAccount.setMobile(updateDto.getMobile());
        anaAccount.setFirstName(updateDto.getFirstName());
        anaAccount.setLastName(updateDto.getLastName());
        if(StringUtils.isNotBlank(updateDto.getPassword())){
            String password = DigestUtils.md5DigestAsHex((updateDto.getPassword() + anaAccount.getAccount()).getBytes());
            if(!password.equals(anaAccount.getPassword())){
                anaAccount.setPassword(password);
                anaAccount.setResetPwdTime(new Date());
                if(anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode())){
                    anaAccount.setStatus(AccountStatus.Active.getCode());
                    updateClientStatus(anaAccount, AccountStatus.Active.name());
                }
                this.logout(anaAccount.getId());
            }
        }
        if(StringUtils.isNotBlank(updateDto.getUserType())) {
            anaAccount.setUserType(AccountType.valueOf(updateDto.getUserType()).getCode());
        }
        if (StringUtils.isNotBlank(updateDto.getEmail())){
            anaAccount.setEmail(updateDto.getEmail());
        }
        restResponse.setData(anaAccount.getId());
        return restResponse;
    }

    private void updateClientStatus(AnaAccount anaAccount, String newStatus){
        if (ApplicationContext.Modules.ANA.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))) {
            for (AnaAccountApplication anaAccountApplication : anaAccount.getAnaAccountApplications()) {
                AnaApplication anaApplication = anaAccountApplication.getAnaApplication();
                if(anaApplication.getCode().equals(ApplicationContext.Modules.ANA)){
                    continue;
                }
                Map<String, String> params = new HashMap<>();
                params.put("id", anaAccountApplication.getBindingAccountId());
                params.put("status", newStatus);
                params.put("apiKey", PropertiesUtil.getAppValueByKey(anaApplication.getCode().toLowerCase()+".comment.api.key"));
                params.put("consumer", ApplicationContext.Modules.ANA);
                String updateClientStatus = PropertiesUtil.getPropertyValueByKey("remote.account.updateClientStatus");
                try {
                    httpClientUtils.httpGet(anaApplication.getInternalEndpoint() + updateClientStatus, RestfulResponse.class, params);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public RestfulResponse reSendEmail(String accountId)  {
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        /*String passStr = StringUtil.getRandomString(6);
        String password = DigestUtils.md5DigestAsHex((passStr + anaAccount.getAccount()).getBytes());
        anaAccount.setPassword(password);
        anaAccountRepository.save(anaAccount);*/

        sendEmail(EMAIL_TYPE_RESEND_ACTIVATION, anaAccount);

        RestfulResponse response = new RestfulResponse();
        response.setSuccessStatus();
        return response;
    }

	@Override
	public Account getLocalAccountInfo(String applicationCode, String loginId)  {
        AnaApplication anaApplication = applicationRepository.findByCode(applicationCode);
        if (null == anaApplication) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_application.getErrorCode());
        }
        Account localAcc = getClientAccountByUserName(loginId, null, anaApplication.getInternalEndpoint());
        if (null == localAcc /*|| null == localAcc.getExternalGroupId() || localAcc.getExternalGroupId().isEmpty()*/) {
        	throw new BusinessException(SystemMsg.ServerErrorMsg.LOGIN_ID_NOT_EXISTS.getErrorCode());
        }
        List<AnaAccountApplication> anaAccountApplication =  anaAccountApplicationRepository.findByBindingAccountId(localAcc.getAccountId());
        if(null != anaAccountApplication && !anaAccountApplication.isEmpty()){
            if(anaAccountApplication.stream().anyMatch(item->item.getAnaApplication().getCode().equals(applicationCode))) {
                throw new BusinessException(SystemMsg.ServerErrorMsg.ACCOUNT_ALREADY_BE_BOUND.getErrorCode());
            }
        }
        Account account = new Account();
    	account.setUsername(localAcc.getUsername());
    	account.setExternalGroupId(localAcc.getExternalGroupId());
    	account.setStatus(localAcc.getStatus());
        return account;
	}
	
	@Override
	public String getSSOAccountInfo(String accountId)  {
        AnaApplication anaApplication = applicationRepository.findByCode("ANA");
        if (null == anaApplication) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_application.getErrorCode());
        }
        return remoteClientUtils.getSSOAccountByLocalAccountId(accountId, anaApplication.getInternalEndpoint());
	}
	
    @Override
    public String getSSOUserInfoByLocalUserAccountId(String accountId) {
        List<AnaAccountApplication> anaAccountApplication = anaAccountApplicationRepository.findByBindingAccountId(accountId);
        if(null != anaAccountApplication && !anaAccountApplication.isEmpty()){
        	return anaAccountApplication.stream().findFirst().map(item->item.getAnaAccount().getAccount()).orElse("");
        }
        return null;
    }

    @Override
    public RestfulResponse rePassword(String accountId)  {
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }

        sendEmail(EMAIL_TYPE_RESET_ENCRYPTEDPASS, anaAccount);

        RestfulResponse response = new RestfulResponse();
        response.setSuccessStatus();
        return response;
    }
    
    /**
     * Reset password after creating user name password
     * @param request
     * @param newPassword
     * @param reNewPassword
     * @param code
     * @return
     */
	@Override
	public RestfulResponse<AccountDto> activationAccount(String firstName,String lastName,String password,String code) {
        if (code==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.the_code_not_exists.getErrorCode());
        }
        String username = JWTTokenUtil.getUserAccount(code);
        AnaAccount account = anaAccountRepository.findByAccountAndStatusNot(username, AccountStatus.Terminated.getCode());
        if(null ==account){
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_not_exist.getErrorCode());
        }
        if(!account.getFirstName().equals(firstName) || !account.getLastName().equals(lastName)){
        	throw new BusinessException(SystemMsg.ServerErrorMsg.FIRST_NAME_OR_LAST_NAME_NOT_EXIST.getErrorCode());
        }
        
        if(account.getStatus().equals(AccountStatus.NotVerified.getCode())){
        	account.setStatus(AccountStatus.Active.getCode());
        }
        String rePassword = DigestUtils.md5DigestAsHex((password + account.getAccount()).getBytes());
        account.setPassword(rePassword);
        account.setUpdatedTime(new Date());
        anaAccountRepository.save(account);

        AccountDto accountDto = new AccountDto();
        accountDto.setAccount(account.getAccount());
        accountDto.setPassword(password);
        return RestfulResponse.ofData(accountDto);
	}

    @Override
    public RestfulResponse<AccountDto> resetPassword(String token, String newPwd) {
        if(StringUtils.isBlank(token) || StringUtils.isBlank(newPwd)){
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        String account = JWTTokenUtil.getUserAccount(token);
        if(StringUtils.isBlank(account)){
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        List<String> status = Stream.of(AccountStatus.values()).filter(item->item.equals(AccountStatus.NotVerified) || item.equals(AccountStatus.Active)).map(AccountStatus::getCode).collect(Collectors.toList());
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusIn(account, status);
        if(anaAccount==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String password = DigestUtils.md5DigestAsHex((newPwd + anaAccount.getAccount()).getBytes());
        anaAccount.setPassword(password);
        anaAccount.setResetPwdTime(new Date());
        if(AccountStatus.NotVerified.getCode().equals(anaAccount.getStatus())){
            anaAccount.setStatus(AccountStatus.Active.getCode());
        }
        anaAccount.setResetPwdTime(new Date());
        anaAccountRepository.save(anaAccount);
        AccountDto accountDto = new AccountDto();
        accountDto.setAccount(account);
        accountDto.setPassword(newPwd);
        this.logout(anaAccount.getId());
        
        Long emailId = Long.parseLong(JWTTokenUtil.getId(token));
        EmailMessage emailMessage = commonEmailService.findOne(emailId);
        emailMessage.setStatus(2);
        
        return RestfulResponse.ofData(accountDto);
    }

    /**
     * chenk First name and last name
     * @param firstName
     * @param lastName
     * @param code
     * @return
     */
    @Override
    public RestfulResponse<Boolean> checkFirstNameAndLastName(String firstName, String lastName, String code)  {
        RestfulResponse<Boolean> restResponse = new RestfulResponse<>();
        String username = JWTTokenUtil.getUserAccount(code);
        AnaAccount account = anaAccountRepository.findByAccountAndStatusNot(username, AccountStatus.Terminated.getCode());
        if(!account.getFirstName().equals(firstName) || !account.getLastName().equals(lastName)){
        	throw new BusinessException(SystemMsg.ServerErrorMsg.FIRST_NAME_OR_LAST_NAME_NOT_EXIST.getErrorCode());
        }
        return restResponse;
    }

	@Override
	public AnaAccount getAccount(String id) {
		return anaAccountRepository.findById(id);
	}

    @Override
    public AnaAccountAccessTokenDto getToken(String token, String code) {
        AnaAccountAccessToken anaAccountAccessToken = anaAccountAccessTokenRepository.findByToken(token);
        AnaAccountAccessTokenDto anaAccountAccessTokenDto = new AnaAccountAccessTokenDto();
        anaAccountAccessTokenDto.setToken(anaAccountAccessToken.getToken());
        anaAccountAccessTokenDto.setRemoteaddr(anaAccountAccessToken.getRemoteAddr());
        String accountId = "";
        for(AnaAccountApplication anaAccountApplication:anaAccountAccessToken.getAnaAccount().getAnaAccountApplications()){
            if(anaAccountApplication.getAnaApplication().getCode().equals(code)){
                accountId = anaAccountApplication.getBindingAccountId();
            }
        }
        anaAccountAccessTokenDto.setAccountid(accountId);
        anaAccountAccessTokenDto.setExpriedtime(DateUtils.formatDate(anaAccountAccessToken.getExpriedTime()));

        return anaAccountAccessTokenDto;
    }



    /**
     * Click the mail check account
     * @author dong liu
     * @param code
     * 			user name and password Encrypted string
     * @return
     */
    @Override
    public Boolean checkActivation(String code)  {
        String username = JWTTokenUtil.getUserAccount(code);
        AnaAccount account = anaAccountRepository.findByAccountAndStatusNot(username, AccountStatus.Terminated.getCode());
        if(Objects.isNull(account)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_not_exist.getErrorCode());
        }
        return !account.getStatus().equals("ACT");
    }

    @Override
    public Boolean checkResetPwd(String code)  {
        Long id = Long.parseLong(JWTTokenUtil.getId(code));
        EmailMessage emailMessage = commonEmailService.findOne(id);
        return emailMessage.getStatus() != 2;
    }

    /*private AnaAccount checkCode(String code){
        if (code==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.the_code_not_exists.getErrorCode());
        }
        String username = JWTTokenUtil.getUserAccount(code);
        String password = JWTTokenUtil.getPassword(code);
        AnaAccount account = anaAccountRepository.findByAccountAndStatusNot(username, AccountStatus.Terminated.getCode());
        if(Objects.isNull(account)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_not_exist.getErrorCode());
        }
        if(account.getStatus().equals("ACT")){
        	throw new BusinessException(SystemMsg.ServerErrorMsg.ACCOUNT_HAS_BEEN_ACTIVATED.getErrorCode());
        }
        if(account.getPassword().equals(password)){
        	return account;
        }else {
            throw new BusinessException(SystemMsg.ServerErrorMsg.link_failure.getErrorCode());
        }
    }*/
    @Override
    public RestfulResponse<com.tng.portal.common.vo.PageDatas> listAccounts(PageQuery<AnaAccount> pageQuery) {
        RestfulResponse<com.tng.portal.common.vo.PageDatas> restResponse = new RestfulResponse<>();
        com.tng.portal.common.vo.PageDatas<AccountDto> pageDatas = new com.tng.portal.common.vo.PageDatas<>(pageQuery.getPageNo(),pageQuery.getPageSize());
        com.tng.portal.common.vo.PageDatas<AnaAccount> anaAccountPageDatas = commonRepository.query(pageQuery,AnaAccount.class);
        List<AccountDto> accountDtos = anaAccountPageDatas.getList().stream().map(item -> {
            AccountDto dto = new AccountDto();
            dto.setId(item.getId());
            dto.setAccount(item.getAccount());
            dto.setFirstName(item.getFirstName());
            dto.setLastName(item.getLastName());
            dto.setMobile(item.getMobile());
            dto.setCreatedTime(item.getCreatedTime());
            dto.setUpdatedTime(item.getUpdatedTime());
            dto.setExternalGroupId(item.getExternalGroupId());
            dto.setInternal(item.getInternal());
            List<Long> roleIdArray = new ArrayList<>();
            for (AnaRole role : item.getAnaRoles()) {
                roleIdArray.add(role.getId());
            }
            dto.setRoles(roleIdArray);
            dto.setCreatedBy(item.getCreatedBy());
            dto.setUpdatedBy(item.getUpdatedBy());
            dto.setStatus(AccountStatus.getName(item.getStatus()));
            Optional<String> optional = item.getAnaAccountApplications().stream().map(app->app.getAnaApplication().getCode()).reduce((o1, o2)->o1 + "," + o2);
            dto.setApplications(optional.isPresent()?optional.get():"");
            dto.setEmail(item.getEmail());
            dto.setEmailSendTo(EmailSendTo.getName(item.getVerifyEmailType()));
            dto.setUserType(AccountType.getName(item.getUserType()));
            if(ApplicationContext.Env.integrated_client.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
                try {
                    // Obtain ANA Account number 
                    dto.setSsoLoginId(getSSOAccountInfo(item.getId()));
                } catch (Exception e) {
                    log.error("get sso account info error");
                }
            }
            if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
                List<AnaAccountApplicationViewDto> applicationViewList = new ArrayList<>();
                for (AnaAccountApplication accountApplication : item.getAnaAccountApplications()) {
                    try {
                        if(!accountApplication.getAnaApplication().getDisplay()){
                            continue;
                        }
                        Account client = getClientAccountByUserName(null, accountApplication.getBindingAccountId(), accountApplication.getAnaApplication().getInternalEndpoint());
                        if(null != client){
                            AnaAccountApplicationViewDto viewDto = new AnaAccountApplicationViewDto();
                            viewDto.setStatus(AccountApplicationStatus.getName(accountApplication.getStatus()));
                            viewDto.setAccount(client.getUsername());
                            viewDto.setApplicationCode(accountApplication.getAnaApplication().getCode());
                            viewDto.setMid(client.getExternalGroupId());
                            applicationViewList.add(viewDto);
                        }
                    } catch (BusinessException e) {
                        log.error("get client account info error");
                    }
                }
                dto.setBindingAccounts(applicationViewList);
            }
            return dto;
        }).collect(Collectors.toList());

        pageDatas.setList(accountDtos);
        pageDatas.setTotal(anaAccountPageDatas.getTotal());
        pageDatas.setTotalPages((int)((anaAccountPageDatas.getTotal()-1)/pageQuery.getPageSize())+1);
        restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();

        return restResponse;
    }



    ParticipantDto getParticipant(String mid){
        if(StringUtils.isBlank(mid)){
            return null;
        }
        return ewpCallerService.getParticipant(mid);
    }


    @Override
    public String getAccountName(String id) {
        if(StringUtils.isNotBlank(id)){
            AnaAccount account = getAccount(id);
            if(Objects.nonNull(account)){
                return account.getFullName();
            }
        }
        return null;
    }

}
