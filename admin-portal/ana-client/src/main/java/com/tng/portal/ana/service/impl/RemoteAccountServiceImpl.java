package com.tng.portal.ana.service.impl;

import com.tng.portal.ana.bean.Account;
import com.tng.portal.ana.constant.*;
import com.tng.portal.ana.entity.AnaAccount;
import com.tng.portal.ana.entity.AnaAccountApplication;
import com.tng.portal.ana.entity.AnaRole;
import com.tng.portal.ana.repository.AnaAccountApplicationRepository;
import com.tng.portal.ana.repository.AnaAccountRepository;
import com.tng.portal.ana.repository.AnaRoleRepository;
import com.tng.portal.ana.service.EwpCallerService;
import com.tng.portal.ana.service.RemoteAccountService;
import com.tng.portal.ana.service.RoleService;
import com.tng.portal.ana.util.RemoteClientUtils;
import com.tng.portal.ana.util.StringUtil;
import com.tng.portal.ana.vo.*;
import com.tng.portal.common.constant.ApplicationCode;
import com.tng.portal.common.constant.Status;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.entity.AnaApplication;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.repository.AnaApplicationRepository;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.JsonUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Roger on 2017/12/4.
 */
@Service
@Transactional
public class RemoteAccountServiceImpl implements RemoteAccountService {
    private static final Logger logger = LoggerFactory.getLogger(RemoteAccountServiceImpl.class);

    @Autowired
    private AnaAccountRepository anaAccountRepository;

    @Autowired
    private AnaApplicationRepository applicationRepository;

    @Autowired
    private AnaAccountApplicationRepository anaAccountApplicationRepository;

    @Autowired
    @Qualifier("remoteClientUtils")
    private RemoteClientUtils remoteClientUtils;



    @Autowired
    private AnaRoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("anaEwpCallerService")
    private EwpCallerService ewpCallerService;

    @Override
    public RestfulResponse active(String id) {
        AnaAccount anaAccount = anaAccountRepository.findById(id);
        if(anaAccount == null){
            logger.info("Account[id] => {} not exist",id);
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        if(Status.NOTVERIFIED.equals(anaAccount.getStatus())){
            anaAccount.setUpdatedTime(new Date());
            anaAccount.setStatus(Status.ACTIVE);
            RestfulResponse restfulResponse = new RestfulResponse();
            restfulResponse.setSuccessStatus();
            logger.info("Active Local Moudles Account");
            return restfulResponse;
        }else{
            logger.info("Account[id] => {} status not verified,account[status] => {}",id,anaAccount.getStatus());
            throw new BusinessException(SystemMsg.ServerErrorMsg.account_status_not_verified.getErrorCode());
        }

    }

    @Override
    public RestfulResponse<Map<String, List<String>>> createAndBindAnaAccount(String remoteHost,MamAddAccountPostDto postDto) {
        /*logger.info("createAndBindAnaAccount() serviceName:{} remoteHost:{} postDto:{}",
                ApplicationContext.getServiceName(), remoteHost, postDto);
        if (null == postDto || null == postDto.getAccount() || postDto.getAccount().isEmpty()) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RestfulResponse<String> createAnaAccountRestfulResponse = createAnaAccount(remoteHost, postDto);
        logger.info("createAndBindAnaAccount() createAnaAccountRestfulResponse:{}", createAnaAccountRestfulResponse);
        if (null != createAnaAccountRestfulResponse && createAnaAccountRestfulResponse.hasSuccessful()) {
            if (StringUtils.isNotBlank(postDto.getUserId())) {
                Map<String, List<String>> bindingAccountResult = bindingAccount(postDto);
                bindingAccountResult.get(MamAccountManagerController.KEY_SUCCESS).add(0, ApplicationContext.Modules.ANA);
                logger.info("createAndBindAnaAccount() bindingAccountResult:{}", bindingAccountResult);
                return getCreateAccountResult(bindingAccountResult);
            }
        }
        Map<String, List<String>> map = new HashMap<>();
        List<String> failList = getBindingDataCode(postDto);
        failList.add(0, ApplicationContext.Modules.ANA);
        map.put(MamAccountManagerController.KEY_FAIL, failList);
        map.put(MamAccountManagerController.KEY_SUCCESS, new ArrayList<>());
        map.put(MamAccountManagerController.KEY_AGENT, new ArrayList<>());
        return getCreateAccountResult(map);*/

        /*if (null == postDto || StringUtils.isBlank(postDto.getAccount())) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RestfulResponse<String> createAnaAccountRestfulResponse = new RestfulResponse<>();
        String account = postDto.getAccount();
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(account, AccountStatus.Terminated.getCode());
        if (null != anaAccount) {
            return i18nMessge.getErrorMessageByErrorCode(SystemMsg.ServerErrorMsg.account_has_been_occupied.getCode());
        }
        if(null != postDto.getBindingData() && !postDto.getBindingData().isEmpty()) {
            Map<String, Long> map = postDto.getBindingData().stream().collect(Collectors.groupingBy(AnaAccountApplicationDto::getApplicationCode, Collectors.counting()));
            if( map.values().stream().max(Comparator.comparing(Long::intValue)).map(item -> item.intValue() > 1).orElse(false)){
                throw new BusinessException(SystemMsg.ServerErrorMsg.ACCOUNT_ALREADY_BE_BOUND.getErrorCode());
            }
        }
        autoCreateRole(postDto.getExternalGroupId());
        String password = postDto.getPassword();
        if(StringUtils.isBlank(password)){
            password = StringUtil.getRandomString(6);
        }
        anaAccount = new AnaAccount();
        MerchantDto merchantDto = null;
        if(StringUtils.isNotBlank(postDto.getExternalGroupId())) {
            merchantDto = RabbitClientUtils.getMerchantByMid(postDto.getExternalGroupId());
        }else{
            merchantDto = RabbitClientUtils.getInternalMerchant();
        }
        if(merchantDto == null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.NOTFINDMERCHANT.getErrorCode());
        }
        anaAccount.setMerchantId(Long.valueOf(CoderUtil.decrypt(merchantDto.getId())));
        anaAccount.setAccount(account);
        anaAccount.setEmail(postDto.getEmail());
        anaAccount.setMobile(postDto.getMobile());
        anaAccount.setName(postDto.getName());
        anaAccount.setPassword(DigestUtils.md5DigestAsHex((postDto.getAccount()+password).getBytes()));
        anaAccount.setCreatedTime(new Date());
        anaAccount.setLanguage("en");
        anaAccount.setIpAddress(remoteHost);
        anaAccount.setStatus(AccountStatus.NotVerified.getCode());
        anaAccount.setInternal(false);
        anaAccount.setExternalGroupId(postDto.getExternalGroupId());
        AccountType userType = postDto.getUserType();
        if (userType == null) {
            userType = AccountType.User;
        }
        anaAccount.setUserType(userType.getCode());
        anaAccount.setVerifyEmailType(null == postDto.getEmailSendTo() ? EmailSendTo.User.getCode() : postDto.getEmailSendTo().getCode());
        List<AnaRole> roleList = null;
        if(StringUtils.isBlank(postDto.getExternalGroupId())){
            roleList = roleRepository.findByNameAndMidIsNull(userType.name().toUpperCase());
        }else{
            roleList = roleRepository.findByNameAndMid(userType.name().toUpperCase(),postDto.getExternalGroupId());
        }
        anaAccount.setAnaRoles(roleList);
        AnaAccount saveAccount = anaAccountRepository.saveAndFlush(anaAccount);

        if(EmailSendTo.Agent.getCode().equals(saveAccount.getVerifyEmailType())){
            List<AnaAccount> accounts = anaAccountRepository.findByExternalGroupIdAndUserType(saveAccount.getExternalGroupId(), AccountType.Agent.getCode());
            if(accounts==null||accounts.size()==0){
                sendEmail(password, saveAccount.getAccount(), saveAccount.getEmail(),saveAccount.getPassword(), saveAccount.getExternalGroupId());
            }else{
                sendEmail(password, saveAccount.getAccount(), accounts.get(0).getEmail(),saveAccount.getPassword(), saveAccount.getExternalGroupId());
            }
        }else{
            sendEmail(password, saveAccount.getAccount(), saveAccount.getEmail(),saveAccount.getPassword(), saveAccount.getExternalGroupId());
        }


        Map<String, List<String>> bindingAccountResult = accountClientService.bindingAccount(postDto);
        bindingAccountResult.get(MamAccountManagerController.KEY_SUCCESS).add(0, ApplicationContext.Modules.ANA);
        logger.info("AddAnaAccount() bindingAccountResult:{}", bindingAccountResult);
        return getCreateAccountResult(bindingAccountResult);

        Map<String, List<String>> map = new HashMap<>();
        List<String> failList = getBindingDataCode(postDto);
        failList.add(0, ApplicationContext.Modules.ANA);
        map.put(MamAccountManagerController.KEY_FAIL, failList);
        map.put(MamAccountManagerController.KEY_SUCCESS, new ArrayList<>());
        map.put(MamAccountManagerController.KEY_AGENT, new ArrayList<>());
        */
        return null;
    }

 /*   private RestfulResponse<String> createAnaAccount(String ip, MamAddAccountPostDto postDto){
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        String account = postDto.getAccount();
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(account, AccountStatus.Terminated.getCode());
        if (null != anaAccount) {
            return i18nMessge.getErrorMessageByErrorCode(SystemMsg.ServerErrorMsg.account_has_been_occupied.getCode());
        }
        if(null != postDto.getBindingData() && !postDto.getBindingData().isEmpty()) {
            Map<String, Long> map = postDto.getBindingData().stream().collect(Collectors.groupingBy(AnaAccountApplicationDto::getApplicationCode, Collectors.counting()));
            if( map.values().stream().max(Comparator.comparing(Long::intValue)).map(item -> item.intValue() > 1).orElse(false)){
                throw new BusinessException(SystemMsg.ServerErrorMsg.ACCOUNT_ALREADY_BE_BOUND.getErrorCode());
            }
        }
        autoCreateRole(postDto.getExternalGroupId());
        String password = getPassword(postDto);
        anaAccount = createAccount(ip,postDto,encryptPassword(postDto.getAccount(),password));
        if(StringUtils.isBlank(postDto.getMamUserId())) {
            sendEmail(anaAccount, password);
        }
        postDto.setUserId(anaAccount.getId());
        postDto.setPassword(password);
        restResponse.setData(anaAccount.getId());
        restResponse.setSuccessStatus();
        return restResponse;
    }*/

    /**
     *  Generate encrypted passwords 
     * @param account  Account number 
     * @param psw  Unencrypted password 
     * @return
     */
    private String encryptPassword(String account,String psw){
        if(StringUtils.isBlank(account) || StringUtils.isBlank(psw)){
            logger.error("isBlank(account) || isBlank(psw) account:{} psw:{}",account,psw);
            return "";
        }
        String text = psw + account;
        return DigestUtils.md5DigestAsHex(text.getBytes());
    }

   /* private Map<String,List<String>> bindingAccount(MamAddAccountPostDto postDto) {
        Map<String,List<String>> map=new HashMap<>();
        List<String> success=new ArrayList<>();
        List<String> fail=new ArrayList<>();
        List<String> agentFail=new ArrayList<>();
        AnaAccount anaAccount = anaAccountRepository.findById(postDto.getUserId());
        if (postDto.getBindingData().stream().noneMatch(item -> item.getApplicationCode().equals(ApplicationCode.MAM.name()))
                && StringUtils.isNotBlank(postDto.getMamUserId())) {
            AnaAccountApplicationDto dto = new AnaAccountApplicationDto();
            dto.setAccount(anaAccount.getAccount());
            dto.setApplicationCode(ApplicationCode.MAM.name());
//            dto.setUserType(AccountType.Agent);
            dto.setUserType(postDto.getUserType());
            postDto.getBindingData().add(dto);
        }
        for (AnaAccountApplicationDto applicationDto : postDto.getBindingData()) {
            AnaApplication anaApplication = applicationRepository.findByCode(applicationDto.getApplicationCode());
            if (null == anaApplication || !anaApplication.getDisplay()) {
                continue;
            }
            try {
                String bindingAccountId = postDto.getMamUserId();
                if  (StringUtils.isBlank(bindingAccountId) || (StringUtils.isNotBlank(bindingAccountId) && !anaApplication.getCode().equals(ApplicationCode.MAM.name()))) {
                    AddAccountPostDto addAccountPostDto = new AddAccountPostDto();
                    addAccountPostDto.setPassword(postDto.getPassword());
                    addAccountPostDto.setAccount(anaAccount.getAccount());
                    addAccountPostDto.setEmail(anaAccount.getEmail());
                    addAccountPostDto.setEmailSendTo(postDto.getEmailSendTo());
                    addAccountPostDto.setExternalGroupId(anaAccount.getExternalGroupId());
                    addAccountPostDto.setUserType(applicationDto.getUserType());
                    addAccountPostDto.setName(anaAccount.getName());
                    addAccountPostDto.setMobile(anaAccount.getMobile());
                    RestfulResponse<String> restfulResponse = remoteClientUtils.addSsoAccount(addAccountPostDto, anaApplication.getUrlEnpoin());
//                    RestfulResponse<String> restfulResponse = remoteClientUtils.addSsoAccount(addAccountPostDto, "http://192.168.1.64:8080/sms");
                    logger.info("bindingAccount() restfulResponse:{}",restfulResponse);
                    if (null == restfulResponse || restfulResponse.hasFailed()) {
                        if(SystemMsg.ServerErrorMsg.exist_merchant_agent.getCode().equals(restfulResponse.getErrorCode())) {
                            agentFail.add(anaApplication.getCode());
                        } else {
                            fail.add(anaApplication.getCode());
                        }
                        continue;
                    }
                    bindingAccountId = restfulResponse.getData();
                    success.add(anaApplication.getCode());
                }
                if(StringUtils.isNotBlank(bindingAccountId)) {
                    createAccountApplication(anaAccount,anaApplication,bindingAccountId);
                }
            } catch (Exception e) {
                fail.add(anaApplication.getCode());
                logger.error("sso create client account error", e);
            }
        }
        map.put(MamAccountManagerController.KEY_SUCCESS, success);
        map.put(MamAccountManagerController.KEY_FAIL, fail);
        map.put(MamAccountManagerController.KEY_AGENT, agentFail);
        return map;
    }*/

    /**
     *  Get multiple SSO Account result description 
     *
     * @param map Key Yes success,fail,agent , value It's the corresponding applicationCode
     * @return
     */
    /*private RestfulResponse<Map<String, List<String>>> getCreateAccountResult(Map<String, List<String>> map) {
        Optional<String> successCode = map.get(MamAccountManagerController.KEY_SUCCESS).stream().reduce((i1, i2) -> i1 + "," + i2);
        Optional<String> failCode = map.get(MamAccountManagerController.KEY_FAIL).stream().reduce((i1, i2) -> i1 + "," + i2);
        Optional<String> agentFailCode = map.get(MamAccountManagerController.KEY_AGENT).stream().reduce((i1, i2) -> i1 + "," + i2);

        RestfulResponse<Map<String, List<String>>> response = new RestfulResponse<>();
        StringBuilder enMessage = new StringBuilder();
        StringBuilder zhCNMessage = new StringBuilder();
        StringBuilder zhHKMessage = new StringBuilder();
        if (successCode.isPresent()) {
            String code = SystemMsg.ErrorMsg.SuccessCreateSsoAccountConnect.getCode();
            String appCode = successCode.get();
            enMessage.append(i18nMessge.getEnMessage(code, appCode)).append("\n");
            zhCNMessage.append(i18nMessge.getZhCNMessage(code, appCode)).append("\n");
            zhHKMessage.append(i18nMessge.getZhHKMessage(code, appCode));
            response.setErrorCode("");
            response.setStatus("success");
        }
        if (failCode.isPresent()) {
            String code = SystemMsg.ErrorMsg.ErrorCreateSsoAccountConnect.getCode();
            String appCode = failCode.get();
            enMessage.append(i18nMessge.getEnMessage(code, appCode)).append("\n");
            zhCNMessage.append(i18nMessge.getZhCNMessage(code, appCode)).append("\n");
            zhHKMessage.append(i18nMessge.getZhHKMessage(code, appCode));
            response.setErrorCode(code);
            response.setStatus("fail");
        }
        if (agentFailCode.isPresent()) {
            String code = SystemMsg.ErrorMsg.AgentAccountExist.getCode();
            String appCode = agentFailCode.get();
            enMessage.append(i18nMessge.getEnMessage(code, appCode)).append("\n");
            zhCNMessage.append(i18nMessge.getZhCNMessage(code, appCode)).append("\n");
            zhHKMessage.append(i18nMessge.getZhHKMessage(code, appCode));
            response.setErrorCode(code);
            response.setStatus("fail");
        }
        response.setMessageEN(enMessage.toString());
        response.setMessageZhCN(zhCNMessage.toString());
        response.setMessageZhHK(zhHKMessage.toString());
        response.setData(map);
        return response;
    }*/

    private List<String> getBindingDataCode(MamAddAccountPostDto dto) {
        List<AnaAccountApplicationDto> bindingData = dto.getBindingData();
        List<String> failList = new ArrayList<>();
        if (bindingData != null && !bindingData.isEmpty()) {
            for (AnaAccountApplicationDto bindingDatum : bindingData) {
                failList.add(bindingDatum.getApplicationCode());
            }
        }
        return failList;
    }

    @Override
    public RestfulResponse<String> getSsoAccount(String id) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        if(StringUtils.isBlank(id)){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        List<AnaAccountApplication> anaAccountApplication = anaAccountApplicationRepository.findByBindingAccountId(id);
        if(null != anaAccountApplication && !anaAccountApplication.isEmpty()){
            restResponse.setData(anaAccountApplication.get(0).getAnaAccount().getAccount());
            restResponse.setSuccessStatus();
        }else{
            restResponse.setFailStatus();
        }
        return  restResponse;
    }

    @Override
    public RestfulResponse bindAccount(MamAddAccountPostDto postDto) {
        RestfulResponse restfulResponse = new RestfulResponse();
        String result = null;
        if(null == postDto
                ||StringUtils.isBlank(postDto.getUserId())
                ||null == postDto.getBindingData()){
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        if(ApplicationContext.Modules.MAM.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))){
            AnaApplication anaApplication = applicationRepository.findByCode(ApplicationCode.ANA.name());
            // The current module is MAM Then the call ANA A bind Method execution execution operation 
            result = remoteClientUtils.bindSso(postDto,anaApplication.getInternalEndpoint());
        }else{
            if(StringUtils.isNotBlank(postDto.getUserId())){
                // The current module is ANA
                result = bindAccountAtAna(postDto);
            }
        }

        if(StringUtils.isNotBlank(result)){
            throw new BusinessException(1000233, "error to create account:"+result);
        }
        restfulResponse.setSuccessStatus();
        return restfulResponse;
    }

    @Override
    public RestfulResponse<Account> getAccountInfoByAccount(String account) {
        if (StringUtils.isBlank(account)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RestfulResponse<Account> restResponse = new RestfulResponse<>();
        Account accountVo = new Account();
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(account, AccountStatus.Terminated.getCode());
        if (null != anaAccount) {
            accountVo.fill(anaAccount);
        }else {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        restResponse.setData(accountVo);
        restResponse.setSuccessStatus();
        return restResponse;
    }
    private int getFlag(List<AnaAccount> anaAccounts, String account) {
        OptionalInt optionalInt =  anaAccounts.stream().mapToInt(item->{
            if (item.getAccount().equals(account)) {
                return 1;
            } else if (item.getAccount().startsWith(account + "_")) {
                String str = item.getAccount().substring((account+"_").length(), item.getAccount().length());
                if (StringUtils.isNumeric(str)) {
                    return Integer.valueOf(str) + 1;
                }
            }
            return 0;
        }).max();
        return optionalInt.isPresent()?optionalInt.getAsInt():0;
    }
    private String getPassword(AddAccountPostDto postDto){
        if(postDto == null){
            logger.warn("getPassword() postDto == null");
            return "";
        }
        String password = postDto.getPassword();
        if(StringUtils.isBlank(password)){
            password = StringUtil.getRandomString(6);
        }
        return password;
    }
    @Override
    public RestfulResponse<String> addSsoAccount(String remoteHost, AddAccountPostDto postDto) {
        if (postDto == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        String account = postDto.getAccount();
        List<AnaAccount> anaAccounts = anaAccountRepository.findByAccountLikeAndStatusNot(account + "%", AccountStatus.Terminated.getCode());
        int flag = getFlag(anaAccounts, postDto.getAccount());
        if(flag > 0){
            account += "_"+flag;
        }
        String passStr = getPassword(postDto);
        postDto.setAccount(account);
        AnaAccount anaAccount = new AnaAccount();
        anaAccount.setAccount(postDto.getAccount());
        anaAccount.setEmail(postDto.getEmail());
        anaAccount.setMobile(postDto.getMobile());
        anaAccount.setFirstName(postDto.getFirstName());
        anaAccount.setLastName(postDto.getLastName());
        anaAccount.setPassword(DigestUtils.md5DigestAsHex((passStr + postDto.getAccount()).getBytes()));
        anaAccount.setCreatedTime(new Date());
        anaAccount.setLanguage("en");
        anaAccount.setIpAddress(remoteHost);
        anaAccount.setStatus(AccountStatus.NotVerified.getCode());
        anaAccount.setVerifyEmailType(EmailSendTo.User.getCode());
        anaAccount.setUserType(postDto.getUserType().getCode());

        anaAccount.setVerifyEmailType(null == postDto.getEmailSendTo() ? EmailSendTo.User.getCode() : postDto.getEmailSendTo().getCode());
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
            if (postDto.getUserType() == AccountType.Admin) {
                List<AnaRole> list = roleRepository.findByRoleType(Arrays.asList(RoleType.GEA_ADMIN.name()));
                anaAccount.setAnaRoles(list);
            } else {
                List<AnaRole> list = roleRepository.findByRoleType(RoleType.findByDepartmentAndUserType(postDto.getDepartment().name(), postDto.getUserType().name()));
                anaAccount.setAnaRoles(list);
            }
        }

        anaAccount = anaAccountRepository.saveAndFlush(anaAccount);

        restfulResponse.setData(anaAccount.getId());
        restfulResponse.setSuccessStatus();
        return restfulResponse;
    }


    ParticipantDto getParticipant(String mid){
        if(StringUtils.isBlank(mid)){
            return null;
        }
        return ewpCallerService.getParticipant(mid);
    }

    @Override
    public RestfulResponse<String> updateLocalAccountInfo(String remoteHost, AccountUpdateDto updateDto) {
        if(ApplicationContext.Modules.MAM.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))){
            RestfulResponse<String> restfulResponse = new RestfulResponse<>();
            if(null == updateDto || StringUtils.isBlank(updateDto.getSystemCode())){
                restfulResponse.setFailStatus();
                return restfulResponse;
            }
            AnaApplication application = applicationRepository.findByCode(updateDto.getSystemCode());
            return remoteClientUtils.updateLocalAccountInfo(updateDto,application.getInternalEndpoint(),application.getCode());

        }else {
            RestfulResponse<String> restResponse = new RestfulResponse<>();
            AnaAccount anaAccount = anaAccountRepository.findOne(updateDto.getId());
            if(null == anaAccount){
                throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
            }
            anaAccount.setMobile(updateDto.getMobile());
            anaAccount.setFirstName(updateDto.getFirstName());
            anaAccount.setLastName(updateDto.getLastName());
            if(null != updateDto.getDepartmentId()){
                anaAccount.setDepartmentId(updateDto.getDepartmentId());
            }
            if(StringUtils.isNotBlank(updateDto.getPassword())){
                String password = DigestUtils.md5DigestAsHex((updateDto.getPassword() + anaAccount.getAccount()).getBytes());
                if(!password.equals(anaAccount.getPassword())){
                    anaAccount.setPassword(password);
                    if(anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode())){
                        anaAccount.setStatus(AccountStatus.Active.getCode());
                    }
                }
            }
            if(StringUtils.isNotBlank(updateDto.getUserType())) {
                anaAccount.setUserType(AccountType.valueOf(updateDto.getUserType()).getCode());
            }
            if(StringUtils.isNotBlank(updateDto.getEmailSendTo())){
                anaAccount.setVerifyEmailType(EmailSendTo.valueOf(updateDto.getEmailSendTo()).getCode());
                if(updateDto.getEmailSendTo().equalsIgnoreCase(EmailSendTo.User.name())){
                    anaAccount.setEmail(updateDto.getEmail());
                }
            }
            anaAccountRepository.saveAndFlush(anaAccount);
            restResponse.setData(anaAccount.getId());
            return restResponse;
        }
    }
    private RestfulResponse<String> updateStatus(String ip, String id, String status)  {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findById(id);
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        AccountStatus newStatus = AccountStatus.valueOf(status);
        anaAccount.setUpdatedTime(new Date());
        if(anaAccount.getStatus().equals(AccountStatus.Terminated.getCode()) || newStatus.equals(AccountStatus.NotVerified)){
            throw new BusinessException(SystemMsg.ErrorMsg.IncorrectAccountStatus.getErrorCode());
        }
        if(anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode()) &&
                (newStatus.equals(AccountStatus.Inactive) || newStatus.equals(AccountStatus.Terminated)
                        || newStatus.equals(AccountStatus.Active))){
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
        restResponse.setSuccessStatus();
        restResponse.setData(anaAccount.getId());
        return restResponse;
    }
    @Override
    public RestfulResponse<String> updateClientStatus(String remoteHost, String id, String status, String code) {
        if(ApplicationContext.Modules.MAM.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))){
            RestfulResponse<String> restfulResponse = new RestfulResponse<>();
            try {
                if(StringUtils.isBlank(id) || StringUtils.isBlank(status)){
                    restfulResponse.setFailStatus();
                    return restfulResponse;
                }
                if(StringUtils.isBlank(code)){
                    updateStatus(remoteHost, id, status);
                } else {
                    AnaApplication application = applicationRepository.findByCode(code);
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("status", status);
                    restfulResponse = remoteClientUtils.updateClientStatus(params,application.getInternalEndpoint());
                }
            }catch (Exception e){
                restfulResponse.setFailStatus();
                logger.error("update client account status error", e);
            }
            return restfulResponse;
        }else {
            return updateStatus(remoteHost, id, status);
        }
    }
    private RestfulResponse<String> editUser(String remoteHost, AccountUpdateDto updateDto) {
        RestfulResponse<String> restResponse = new RestfulResponse<>();
        AnaAccount anaAccount = anaAccountRepository.findOne(updateDto.getId());
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        anaAccount.setMobile(updateDto.getMobile());
        anaAccount.setEmail(updateDto.getEmail());
        anaAccount.setFirstName(updateDto.getFirstName());
        anaAccount.setLastName(updateDto.getLastName());
        if(null != updateDto.getDepartmentId()){
            anaAccount.setDepartmentId(updateDto.getDepartmentId());
        }
        if(StringUtils.isNotBlank(updateDto.getPassword())){
            String password = DigestUtils.md5DigestAsHex((updateDto.getPassword() + anaAccount.getAccount()).getBytes());
            if(!password.equals(anaAccount.getPassword())){
                anaAccount.setPassword(password);
                if(anaAccount.getStatus().equals(AccountStatus.NotVerified.getCode())){
                    anaAccount.setStatus(AccountStatus.Active.getCode());
                }
            }
        }
        if(StringUtils.isNotBlank(updateDto.getUserType())) {
            anaAccount.setUserType(AccountType.valueOf(updateDto.getUserType()).getCode());
        }
        if(StringUtils.isNotBlank(updateDto.getEmailSendTo())){
            anaAccount.setVerifyEmailType(EmailSendTo.valueOf(updateDto.getEmailSendTo()).getCode());
        }
        anaAccountRepository.saveAndFlush(anaAccount);
        restResponse.setData(anaAccount.getId());
        return restResponse;
    }
    @Override
    public RestfulResponse<String> updateClientAccountInfo(String remoteHost, AccountUpdateDto updateDto) {
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            if(null != updateDto.getBindingData() && !updateDto.getBindingData().isEmpty() ){
                MamAddAccountPostDto postDto = new MamAddAccountPostDto();
                postDto.setUserId(updateDto.getId());
                postDto.setBindingData(updateDto.getBindingData());
                updateBindingAccount(postDto);
            }
        }else {
            RestfulResponse<String> response = editUser(remoteHost, updateDto);
            try {
                updateDto.setId(updateDto.getId());
                AnaApplication anaApplication = applicationRepository.findByCode(ApplicationCode.ANA.name());
                remoteClientUtils.updateClientAccountInfo(updateDto,anaApplication.getInternalEndpoint(),anaApplication.getCode());
            }catch (Exception e){
                response.setFailStatus();
                logger.error("Exception",e);
            }
            return response;
        }
        return null;
    }

    @Override
    public RestfulResponse resendClientEmail(String accountId, String code) {
        if(ApplicationContext.Modules.MAM.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))){
            RestfulResponse<String> restfulResponse = new RestfulResponse<>();
            try {
                if(StringUtils.isBlank(code)){
                    resendEmail(accountId);
                } else {
                    AnaApplication application = applicationRepository.findByCode(code);
                    Map<String, String> params = new HashMap<>();
                    params.put("accountId", accountId);
                    restfulResponse = remoteClientUtils.resendClientEmail(params,application.getInternalEndpoint());
                }
            }catch (Exception e){
                restfulResponse.setFailStatus();
                logger.error("resend email error", e);
            }
            return restfulResponse;
        }else {
            return resendEmail(accountId);
        }
    }

    @Override
    public RestfulResponse resetPassword(String id, String password, String code) {
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        if(StringUtils.isBlank(id)){
            restfulResponse.setFailStatus();
            return restfulResponse;
        }
        if(ApplicationContext.Modules.MAM.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName))){
            if(StringUtils.isBlank(code)){
                restfulResponse.setFailStatus();
                return restfulResponse;
            }
            try {
                AnaApplication application = applicationRepository.findByCode(code);
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                restfulResponse = remoteClientUtils.resetPassword(params,application.getInternalEndpoint());
            }catch (Exception e){
                restfulResponse.setFailStatus();
                logger.error("reset password error", e);
            }
            return restfulResponse;
        }else {
            return rePassword(id, password);
        }
    }

    @Override
    public RestfulResponse<List<AnaAccountApplicationViewDto>> getBindAccounts(String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getCode());
        }
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            RestfulResponse<List<AnaAccountApplicationViewDto>> restfulResponse = new RestfulResponse<>();
            List<AnaAccountApplicationViewDto> bindingAccounts = new ArrayList<>();
            List<AnaAccountApplication> list = anaAccountApplicationRepository.findByBindingAccountId(id);
            list.stream().findFirst().ifPresent(item -> {
                List<AnaAccountApplication> ll = anaAccountApplicationRepository.findByAnaAccount(item.getAnaAccount());
                ll.stream().forEach(app -> {
                    Account client = getClientAccountByBindAccountId(app.getBindingAccountId(), app.getAnaApplication().getInternalEndpoint());
                    if (null != client) {
                        AnaAccountApplicationViewDto viewDto = new AnaAccountApplicationViewDto();
                        viewDto.setStatus(AccountApplicationStatus.getName(app.getStatus()));
                        viewDto.setAccount(client.getUsername());
                        viewDto.setApplicationCode(app.getAnaApplication().getCode());
                        viewDto.setMid(client.getExternalGroupId());
                        viewDto.setUserType(client.getUserType());
                        bindingAccounts.add(viewDto);
                    }
                });
            });

            restfulResponse.setData(bindingAccounts);
            restfulResponse.setSuccessStatus();
            return restfulResponse;
        }else {
            AnaApplication application = applicationRepository.findByCode(ApplicationCode.ANA.name());
            Map<String, String> params = new HashMap<>();
            params.put("id", id);
            RestfulResponse restfulResponse = new RestfulResponse();
            try {
                restfulResponse = remoteClientUtils.getBindAccounts(params,application.getInternalEndpoint());
            } catch (Exception e) {
                restfulResponse.setFailStatus();
                logger.error("query account sso client account error", e);
            }
            return restfulResponse;
        }
    }

    @Override
    public RestfulResponse<Account> getAccountInfoByBindAccountId(String id) {
        Account account = new Account();
        AnaAccount anaAccount = anaAccountRepository.findById(id);
        if (null != anaAccount) {
            account.fill(anaAccount);
        }else {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        RestfulResponse<Account> restResponse = new RestfulResponse<>();
        restResponse.setData(account);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public RestfulResponse<List<AccountDto>> queryAccountList(AccountQueryPostDto postDto) {
        logger.info("queryAccountList() getServiceName:{} postDto:{} ", PropertiesUtil.getServiceName(), postDto);
        if (postDto == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RestfulResponse<List<AccountDto>> restResponse = new RestfulResponse<>();
        Sort sort = new Sort(Sort.Direction.ASC, "createdTime");
        List<String> status =  Arrays.asList(AccountStatus.values()).stream().map(AccountStatus::getCode).collect(Collectors.toList());
        Specification<AnaAccount> specification=new Specification<AnaAccount>() {
            @Override
            public Predicate toPredicate(Root<AnaAccount> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String search = postDto.getSearch();
                Predicate account = criteriaBuilder.like(root.get("account").as(String.class), search);
                Predicate name = criteriaBuilder.like(root.get("name").as(String.class), search);
                Predicate mobile = criteriaBuilder.like(root.get("mobile").as(String.class), search);
                Predicate searchOr = criteriaBuilder.or(account, name, mobile);

                Predicate inStatus = root.get("status").in(status);
                Predicate userType = criteriaBuilder.notEqual(root.get("userType").as(String.class), "RT");
                Predicate predicate = criteriaBuilder.and(searchOr, inStatus, userType);
                List<String> accounts = postDto.getAccounts();
                if(accounts!=null && !accounts.isEmpty()) {
                    Predicate inId = root.get("id").in(accounts);
                    if (!postDto.isInAccounts()) {
                        inId = criteriaBuilder.not(inId);
                    }
                    predicate = criteriaBuilder.and(predicate, inId);
                }
                List<String> mids = postDto.getMids();
                if(mids !=null && !mids.isEmpty()){
                    Predicate inMid = root.get("externalGroupId").in(mids);
                    predicate = criteriaBuilder.and(predicate,inMid);
                }
                return predicate;
            }
        };
        List<AnaAccount> list = anaAccountRepository.findAll(specification, sort);
        logger.info("getClientAccountList() list.size() " + list.size());
        List<AccountDto> accountList = list.stream().map(item -> {
            return parse(item);
        }).collect(Collectors.toList());
        restResponse.setSuccessStatus();
        restResponse.setData(accountList);
        return restResponse;
    }

    @Override
    public RestfulResponse<PageDatas> listAccountsWithoutSso(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending, String mid, String accountId, String applicationCode) {
       /* logger.info("listAccountsWithoutSso() pageNo:{} pageSize:{} search:{} sortBy:{} isAscending:{} mid:{} accountId:{} applicationCode:{}",
                pageNo, pageSize, search, sortBy, isAscending, mid, accountId, applicationCode);
        pageNo = getPageNumber(pageNo,1);
        pageSize = getPageSize(pageSize,10);
        search = getSearch(search);
        if(StringUtils.isBlank(sortBy)){
            sortBy = "createdTime";
        }
        if(isAscending == null){
            isAscending = false;
        }
        PageDatas<AccountDto> pageDatas = new PageDatas<>(pageNo,pageSize);
        Sort sort = pageDatas.pageSort("createdTime", false, "createdTime");
        AccountQueryPostDto postDto = new AccountQueryPostDto();
        postDto.setSearch(search);
        postDto.setInAccounts(false);
        List<AnaAccount> list=new ArrayList<>();
        try {
            List<String> status =  Arrays.asList(AccountStatus.values()).stream().map(AccountStatus::getCode).collect(Collectors.toList());
            if (StringUtils.isBlank(mid)) {
                AnaApplication application = applicationRepository.findByCode(ApplicationCode.MAM.name());
                String mamUrl = application.getUrlEnpoin();
//                String mamUrl = "http://192.168.1.65:8080/mam";
                Account account = getClientAccountByBindAccountId(accountId, mamUrl);
                boolean viewAllMerchant = remoteClientUtils.isViewAllMerchant(account,FunctionCode.ACCOUNTS_WITHOUT_SSO,mamUrl);
                boolean isRoot = AccountType.ROOT.name().equals(account.getUserType())||AccountType.ROOT.getCode().equals(account.getUserType());
                logger.info("listAccountsWithoutSso() getAccountId:{} getUserType():{} getExternalGroupId:{} viewAllMerchant:{}",
                        account.getAccountId(),account.getUserType(),account.getExternalGroupId(),viewAllMerchant);
                if(isRoot || viewAllMerchant){
                    list = anaAccountRepository.findByFilter(search, status, sort);
                }else {
                    Map<String, String> param = new HashMap<>();
                    param.put("accountId", accountId);
                    RestfulResponse<String> restful = remoteClientUtils.salesPersonMerchantId(param,mamUrl);
                    String response = restful.getData();
                    if (response != null && response.length() != 0 && !"Empty".equals(response)) {
                        if (!"Root".equals(response)) {
                            String[] split = response.split(",");
                            List<String> midList = Arrays.asList(split);
                            if (!midList.isEmpty()) {
                                list = anaAccountRepository.findByFilterAndAccountAndExternalGroupId(search, status, midList, sort);
                            }
                            postDto.setMids(midList);
                        } else {
                            list = anaAccountRepository.findByFilter(search, status, sort);
                        }
                    }
                }
            }else {
                list = anaAccountRepository.findByFilterAndAccountAndExternalGroupId(search, status, mid, sort);
                List<String> mids=new ArrayList<>();
                mids.add(mid);
                postDto.setMids(mids);
            }
        } catch (Exception e) {
            logger.error("listAccountsWithoutSso",e);
        }

        List<String> appAccountId=new ArrayList<>();
        for (AnaAccount anaAccount : list) {
            for (AnaAccountApplication anaAccountApplication : anaAccount.getAnaAccountApplications()) {
                if(anaAccountApplication.getAnaApplication().getCode().equals(applicationCode)){
                    appAccountId.add(anaAccountApplication.getBindingAccountId());
                }
            }
        }
        postDto.setAccounts(appAccountId);
        AnaApplication anaApplication = applicationRepository.findByCode(applicationCode);

        RestfulResponse restfulResponse = remoteClientUtils.queryAccountList(postDto, anaApplication.getUrlEnpoin());
//        RestfulResponse restfulResponse = remoteClientUtils.queryAccountList(postDto,"http://192.168.1.65:8080/sms");
        List<AccountDto> result = new ArrayList<>();
        if (Objects.nonNull(restfulResponse) && Objects.nonNull(restfulResponse.getData())) {
            if (restfulResponse.getData() instanceof ArrayList) {
                List li = (List) restfulResponse.getData();
                li.stream().forEach(rs -> {
                    AccountDto dto = JsonUtils.readValue(JsonUtils.toJSon(rs), AccountDto.class);
//                    List<AnaAccountApplication> anaAccountApplication = anaAccountApplicationRepository.findByBindingAccountId(dto.getId());
//                    Optional<AnaAccountApplication> optional = anaAccountApplication.stream().filter(account -> account.getAnaApplication().getUrlEnpoin().equals(anaApplication.getUrlEnpoin())).findFirst();
//                    dto.setSsoLoginId(optional.isPresent() ? optional.get().getAnaAccount().getAccount() : "");
                    dto.setApplications(applicationCode);
                    result.add(dto);
                });
            }
        }
        List<AccountDto> newResult = sortData(result, sortBy, isAscending);// sort 
        if(result.size() > (pageNo-1)*pageSize) {
            newResult = newResult.stream().skip(pageSize*(pageNo-1)).limit(pageSize).collect(Collectors.toList());// paging 
        }
        pageDatas.setList(newResult);
        pageDatas.setTotal(Long.valueOf(result.size()));
        pageDatas.setTotalPages((result.size() + pageSize - 1) / pageSize);*/
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public RestfulResponse<PageDatas> listAccounts(Integer pageNo, Integer pageSize, String search, String sortBy, Boolean isAscending, String mid, String accountId) {
        RestfulResponse<PageDatas> restResponse = new RestfulResponse<>();
        /*PageDatas<AccountDto> pageDatas = new PageDatas<>(pageNo,pageSize);

        Sort sort = pageDatas.pageSort("createdTime", isAscending, "createdTime");
        if(checkSort(AnaAccount.class, sortBy)){
            sort = pageDatas.pageSort(sortBy, isAscending, "createdTime");
        }
        List<AnaAccount> list=new ArrayList<>();

        try {
            Specifications<AnaAccount> where = getWhere(search, null, null, true);
            if (StringUtils.isBlank(mid)) {
                AnaApplication application = applicationRepository.findByCode(ApplicationCode.MAM.name());
                String mamUrl = application.getUrlEnpoin();
//                String mamUrl = "http://192.168.1.65:8080/mam";
                Account account = remoteClientUtils.getAccountInfoByBindAccountId(accountId, mamUrl);
                boolean viewAllMerchant = remoteClientUtils.isViewAllMerchant(account,FunctionCode.MERCHENT_ACCOUNT,mamUrl);
                boolean isRoot = AccountType.ROOT.name().equals(account.getUserType())||AccountType.ROOT.getCode().equals(account.getUserType());
                logger.info("listAccounts() getAccountId:{} getUserType():{} getExternalGroupId:{} viewAllMerchant:{}",
                        account.getAccountId(),account.getUserType(),account.getExternalGroupId(),viewAllMerchant);
                if(isRoot || viewAllMerchant){
                    where = getWhere(search, null, null, false);
                }else {
                    Map<String, String> param = new HashMap<>();
                    param.put("accountId", accountId);
                    RestfulResponse<String> restful = remoteClientUtils.salesPersonMerchantId(param,mamUrl);
                    logger.info("listAccounts() Url.MAM_SALES_PERSION_MERCHANT_ID:{} restful:{}",mamUrl,restful);
                    String response = restful.getData();
                    if (response != null && response.length() != 0 && !"Empty".equals(response)) {
                        if (!"Root".equals(response)) {
                            String[] split = response.split(",");
                            List<String> midList = Arrays.asList(split);
                            if (!midList.isEmpty()) {
                                where =  getWhere(search, null, midList, false);
                            }
                        } else {
                            where = getWhere(search, null, null, true);
                        }
                    }
                }
            }else {
                where =  getWhere(search, mid, null, false);
            }

            Page<AnaAccount> page = anaAccountRepository.findAll(where, pageDatas.pageRequest(sort));
            list = page.getContent();
            pageDatas.initPageParam(page);
            System.out.println(list.size());
        } catch (Exception e) {
            logger.error("listAccounts",e);
        }

        List<AccountDto> result = list.stream().map(item->{
            return parse(item);
        }).collect(Collectors.toList());*/

//        pageDatas.setList(result);
//        restResponse.setData(pageDatas);
        restResponse.setSuccessStatus();
        return restResponse;
    }



    @Override
    public RestfulResponse<String> merchantManagementBindingSso(String remoteHost, MamAddAccountPostDto postDto) {
        /*logger.info("addBindingAccountSso() serviceName:{} postDto:{}", ApplicationContext.getServiceName(), postDto);
        //postDto.getAccount() User input. login id
        if (null == postDto || postDto.getAccount() == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RestfulResponse<String> restfulResponse=new RestfulResponse<>();
//        AnaAccount anaAccount = anaAccountRepository.findByAccount(postDto.getAccount());
        List<String> status = Stream.of(AccountStatus.values()).filter(item->item.equals(AccountStatus.NotVerified) || item.equals(AccountStatus.Active)).map(AccountStatus::getCode).collect(Collectors.toList());
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusIn(postDto.getAccount(), status);
        if(anaAccount==null){
            if(null != postDto.getBindingData() && !postDto.getBindingData().isEmpty()) {
                Map<String, Long> map = postDto.getBindingData().stream().collect(Collectors.groupingBy(AnaAccountApplicationDto::getApplicationCode, Collectors.counting()));
                if( map.values().stream().max(Comparator.comparing(Long::intValue)).map(item -> item.intValue() > 1).orElse(false)){
                    logger.error("createLocalAccount() ACCOUNT_ALREADY_BE_BOUND ip:{} postDto:{}",remoteHost,postDto);
                    throw new BusinessException(SystemMsg.ServerErrorMsg.ACCOUNT_ALREADY_BE_BOUND.getErrorCode());
                }
            }
//            postDto.setUserType(AccountType.User);// Fixed as User
            String password = getPassword(postDto);
//            String encryptPassword = DigestUtils.md5DigestAsHex((password + postDto.getAccount()).getBytes());
            anaAccount = createAccount(remoteHost, postDto, DigestUtils.md5DigestAsHex((postDto.getAccount()+""+password).getBytes()));
            sendEmail(anaAccount,password);

            AnaApplication anaApplication = applicationRepository.findByCode(postDto.getApplicationCode());
            //postDto.getUserId() The account number is selected. id
            createAccountApplication(anaAccount,anaApplication,postDto.getUserId());
            restfulResponse.setSuccessStatus();
        }else{
            String externalGroupId = anaAccount.getExternalGroupId();
            logger.info("anaAccount.getExternalGroupId():{} postDto.getExternalGroupId():{}",externalGroupId,postDto.getExternalGroupId());
            if(externalGroupId!=null){
                if(!externalGroupId.equals(postDto.getExternalGroupId())) {
                    throw new BusinessException(SystemMsg.ErrorMsg.EnterSameMidAccountSso.getErrorCode());
                }
            }else{
                if(!StringUtils.isEmpty(postDto.getExternalGroupId())){
                    throw new BusinessException(SystemMsg.ErrorMsg.EnterSameMidAccountSso.getErrorCode());
                }
            }
            List<AnaAccountApplication> anaAccountApplications = anaAccount.getAnaAccountApplications();
            if(anaAccountApplications!=null){// Check whether there is a link to this module. 
                for (AnaAccountApplication anaAccountApplication : anaAccountApplications) {
//                    logger.info("anaAccountApplication.getAnaApplication().getCode() "+anaAccountApplication.getAnaApplication().getCode()
//                            +" postDto.getApplicationCode() "+postDto.getApplicationCode());
                    if(anaAccountApplication.getAnaApplication().getCode().equals(postDto.getApplicationCode())){
                        throw new BusinessException(SystemMsg.ErrorMsg.HasSsoLinkAccount.getErrorCode());
                    }
                }
            }
            AnaApplication anaApplication = applicationRepository.findByCode(postDto.getApplicationCode());
            //postDto.getUserId() The account number is selected. id
            createAccountApplication(anaAccount,anaApplication,postDto.getUserId());
            restfulResponse.setSuccessStatus();
        }*/
        return null;
    }

    @Override
    public RestfulResponse<Account> getAccountInfoByNameOrId(AccountUsernameDto accountDto) {
        logger.info("getAccountInfoByNameOrId() getServiceName:{} Accountto:{}", PropertiesUtil.getServiceName(), accountDto);
        if (null == accountDto) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        String username = accountDto.getUsername();
        String accountId = accountDto.getAccountId();
        RestfulResponse<Account> restResponse = new RestfulResponse<>();
        Account account = null;
        if (null != username && !username.isEmpty()) {
            account = getAuthUserInfoByUserAccount(username);
        } else if (null != accountId && !accountId.isEmpty()) {
            account = getAuthUserInfoByAccountId(accountId);
        }
        restResponse.setData(account);
        restResponse.setSuccessStatus();
        return restResponse;
    }

    @Override
    public RestfulResponse inactive(String mid) {
        anaAccountRepository.updateByMid(mid);
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            List<AnaApplication> apps = applicationRepository.findAll();
            for(AnaApplication app: apps){
                if(!app.getCode().equalsIgnoreCase(ApplicationContext.Modules.ANA)){
                    remoteClientUtils.inactive(app.getInternalEndpoint(),mid);
                }
            }
        }
        RestfulResponse response = new RestfulResponse();
        response.setSuccessStatus();
        return response;
    }

    private Account getAuthUserInfoByUserAccount(String userAccount) {
        Account account = new Account();
        AnaAccount anaAccount = anaAccountRepository.findByAccountAndStatusNot(userAccount, AccountStatus.Terminated.getCode());
        if (null != anaAccount) {
            account.fill(anaAccount);
        }else {
            logger.error("auth valid user account name:{}",userAccount);
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        return account;
    }

    private Account getAuthUserInfoByAccountId(String accountId) {
        Account account = new Account();
        AnaAccount anaAccount = anaAccountRepository.findById(accountId);
        if (null != anaAccount) {
            account.fill(anaAccount);
        }else {
            logger.error("auth valid user account id:{}",accountId);
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        return account;
    }


    private AccountDto parse(AnaAccount anaAccount) {
        AccountDto dto = new AccountDto();
        dto.setId(anaAccount.getId());
        dto.setAccount(anaAccount.getAccount());
        dto.setFirstName(anaAccount.getFirstName());
        dto.setLastName(anaAccount.getLastName());
        dto.setMobile(anaAccount.getMobile());
        dto.setCreatedTime(anaAccount.getCreatedTime());
        dto.setUpdatedTime(anaAccount.getUpdatedTime());
        dto.setExternalGroupId(anaAccount.getExternalGroupId());
        dto.setInternal(anaAccount.getInternal());
        List<AnaRole> rl = roleRepository.findByAnaAccounts(anaAccount);
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
        dto.setCreatedBy(anaAccount.getCreatedBy());
        dto.setUpdatedBy(anaAccount.getUpdatedBy());
        dto.setStatus(AccountStatus.getName(anaAccount.getStatus()));
        dto.setEmail(anaAccount.getEmail());
        dto.setEmailSendTo(EmailSendTo.getName(anaAccount.getVerifyEmailType()));
        dto.setUserType(AccountType.getName(anaAccount.getUserType()));
        return dto;
    }
    private Account getClientAccountByBindAccountId(String accountId, String url)  {
        try {
            return remoteClientUtils.getAccountInfoByBindAccountId(accountId,url);
        } catch (Exception e) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorAccessingAccountInformation.getErrorCode());
        }
    }
    private RestfulResponse rePassword(String accountId, String newPwd)  {
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String passStr = StringUtils.isNotBlank(newPwd) ? newPwd : StringUtil.getRandomString(6);
        String password = DigestUtils.md5DigestAsHex((passStr + anaAccount.getAccount()).getBytes());
        anaAccount.setPassword(password);
        if(AccountStatus.NotVerified.getCode().equals(anaAccount.getStatus())){
            anaAccount.setStatus(AccountStatus.Active.getCode());
        }
        anaAccountRepository.save(anaAccount);
        if(anaAccount.getVerifyEmailType().equals(EmailSendTo.Agent.getCode())){
          /*  List<AnaAccount> accounts = anaAccountRepository.findByExternalGroupIdAndUserType(anaAccount.getExternalGroupId(), AccountType.Agent.getCode());
            if(accounts==null||accounts.size()==0){
                throw new BusinessException(SystemMsg.ServerErrorMsg.AGENT_NOT_FOUND.getErrorCode());
            }else{
                remoteClientUtils.sendEmail(passStr, anaAccount.getAccount(), findAgentEmail(anaAccount.getExternalGroupId()),password, anaAccount.getExternalGroupId());
            }*/

        }else{
            remoteClientUtils.sendEmail(passStr, anaAccount.getAccount(), anaAccount.getEmail(),password, anaAccount.getExternalGroupId());
        }
        RestfulResponse response = new RestfulResponse();
        response.setSuccessStatus();
        response.setData(anaAccount.getId());
        return response;
    }
    private RestfulResponse resendEmail(String accountId)  {
        AnaAccount anaAccount = anaAccountRepository.findOne(accountId);
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        String passStr = StringUtil.getRandomString(6);
        String password = DigestUtils.md5DigestAsHex((passStr + anaAccount.getAccount()).getBytes());
        anaAccount.setPassword(password);
        anaAccountRepository.save(anaAccount);
        if(anaAccount.getVerifyEmailType().equals(EmailSendTo.Agent.getCode())){
          /*  List<AnaAccount> accounts = anaAccountRepository.findByExternalGroupIdAndUserType(anaAccount.getExternalGroupId(), AccountType.Agent.getCode());
            if(accounts==null||accounts.size()==0){
                throw new BusinessException(SystemMsg.ServerErrorMsg.AGENT_NOT_FOUND.getErrorCode());
            }
            remoteClientUtils.sendEmail(passStr, anaAccount.getAccount(), findAgentEmail(anaAccount.getExternalGroupId()),password, anaAccount.getExternalGroupId());
*/

        }else{
            remoteClientUtils.sendEmail(passStr, anaAccount.getAccount(), anaAccount.getEmail(),password, anaAccount.getExternalGroupId());
        }
        RestfulResponse response = new RestfulResponse();
        response.setSuccessStatus();
        response.setData(passStr);
        return response;
    }
    private void updateBindingAccount(MamAddAccountPostDto postDto) {
        List<AnaAccountApplication> ll = anaAccountApplicationRepository.findByBindingAccountId(postDto.getUserId());
        AnaAccount anaAccount = ll.stream().findFirst().map(AnaAccountApplication::getAnaAccount).orElse(null);
        List<AnaAccountApplication> list = anaAccountApplicationRepository.findByAnaAccount(anaAccount);
        for(AnaAccountApplication app : list){
            Optional<AnaAccountApplicationDto> optional = postDto.getBindingData().stream().filter(item->item.getApplicationCode().equals(app.getAnaApplication().getCode())).findFirst();
            if(optional.isPresent()){
                postDto.getBindingData().remove(optional.get());
            }else {
                anaAccountApplicationRepository.delete(app.getId());
                Map<String, String> params = new HashMap<>();
                params.put("id", app.getBindingAccountId());
                params.put("status", AccountStatus.Inactive.name());
                try {
                    remoteClientUtils.updateClientStatus(params,app.getAnaApplication().getInternalEndpoint());
                }catch (Exception e){
                    logger.error("sso update client account status error", e);
                }
            }
        }
        for (AnaAccountApplicationDto applicationDto : postDto.getBindingData()) {
            AnaApplication anaApplication = applicationRepository.findByCode(applicationDto.getApplicationCode());
            if (null == anaApplication || !anaApplication.getDisplay()) {
                throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_application.getErrorCode());
            }
            AddAccountPostDto addAccountPostDto = new AddAccountPostDto();
            addAccountPostDto.setPassword(postDto.getPassword());
            addAccountPostDto.setAccount(anaAccount.getAccount());
            addAccountPostDto.setEmail(anaAccount.getEmail());
            addAccountPostDto.setEmailSendTo(postDto.getEmailSendTo());
            addAccountPostDto.setExternalGroupId(anaAccount.getExternalGroupId());
            addAccountPostDto.setUserType(applicationDto.getUserType());
            addAccountPostDto.setFirstName(anaAccount.getFirstName());
            addAccountPostDto.setLastName(anaAccount.getLastName());
            addAccountPostDto.setMobile(anaAccount.getMobile());
            try {
                RestfulResponse<String> restfulResponse = remoteClientUtils.addSsoAccount(addAccountPostDto,anaApplication.getInternalEndpoint(),anaApplication.getCode());
                if (null == restfulResponse || null == restfulResponse.getData()) {
                    continue;
                }
                AnaAccountApplication anaAccountApplication = new AnaAccountApplication();
                anaAccountApplication.setAnaAccount(anaAccount);
                anaAccountApplication.setAnaApplication(anaApplication);
                anaAccountApplication.setCreatedTime(new Date());
                anaAccountApplication.setCreatedBy(anaAccount.getId());
                anaAccountApplication.setBindingAccountId(restfulResponse.getData());
                anaAccountApplication.setStatus(AccountApplicationStatus.Temporary.getCode());
                anaAccountApplicationRepository.saveAndFlush(anaAccountApplication);
            }catch (Exception e){
                logger.error("sso binding error", e);
            }
        }
    }
    /*private void autoCreateRole(String mid){
        //create role
        if(StringUtils.isBlank(mid)){
            return;
        }
        List<AnaRole> roles = roleRepository.findByMid(mid);
        if(null==roles||roles.size()==0){
            //String[] types = new String[]{"AGENT","ADMIN","USER"};
            roles = roleRepository.findByTypeAndIsdefaultAndMidIsNull("E", "Y");
            for(AnaRole role:roles){
                AnaRole newRole = new AnaRole();
                BeanUtils.copyProperties(role,newRole);
                newRole.setId(null);
                newRole.setAnaAccounts(null);
                newRole.setMid(mid);
                newRole.setType("E");
                newRole.setAnaRoleFunctions(null);
                newRole.setIsdefault("N");
                roleRepository.saveAndFlush(newRole);
                List<AnaRoleFunctionPermission> anaRoleFunctionPermissions = new ArrayList<>();
                for(AnaRoleFunctionPermission arfp:role.getAnaRoleFunctions()){
                    AnaRoleFunctionPermission anaRoleFunctionPermission = new AnaRoleFunctionPermission();
                    BeanUtils.copyProperties(arfp,anaRoleFunctionPermission);
                    anaRoleFunctionPermission.setAnaRoleFunctionPk(new AnaRoleFunctionPk(newRole.getId(), arfp.getAnaFunction().getCode()));
                    anaRoleFunctionPermission.setAnaRole(newRole);
                    anaRoleFunctionPermissions.add(anaRoleFunctionPermission);
                }
                newRole.setAnaRoleFunctions(anaRoleFunctionPermissions);
                List<AnaRoleFunction> roleFunctions = roleFunctionRepository.findByRoleId(role.getId());
                List<AnaRolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());
                for(AnaRoleFunction function:roleFunctions){
                    AnaRoleFunction anaRoleFunction = new AnaRoleFunction();
                    BeanUtils.copyProperties(function,anaRoleFunction);
                    anaRoleFunction.setRoleId(newRole.getId());
                    roleFunctionRepository.save(anaRoleFunction);
                }
                for(AnaRolePermission permission:rolePermissions){
                    AnaRolePermission anaRolePermission = new AnaRolePermission();
                    BeanUtils.copyProperties(permission,anaRolePermission);
                    anaRolePermission.setRoleId(newRole.getId());
                    rolePermissionRepository.save(anaRolePermission);
                }
            }
        }
    }*/
    private String bindAccountAtAna(MamAddAccountPostDto postDto) {
        StringBuffer sb = new StringBuffer();
        AnaAccount anaAccount = anaAccountRepository.findById(postDto.getUserId());
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getErrorCode());
        }
        postDto.setFirstName(anaAccount.getFirstName());
        postDto.setLastName(anaAccount.getLastName());
        postDto.setUserType(postDto.getUserType());
        postDto.setAccount(anaAccount.getAccount());
        postDto.setEmail(anaAccount.getEmail());
        postDto.setExternalGroupId(anaAccount.getExternalGroupId());
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
                continue;
            }
            String bindingAccountId = "";
            if("add".equals(applicationDto.getActionType())){
                AddAccountPostDto addAccountPostDto = new AddAccountPostDto();
                addAccountPostDto.setPassword(postDto.getPassword());
                addAccountPostDto.setAccount(anaAccount.getAccount());
                addAccountPostDto.setEmail(anaAccount.getEmail());
                addAccountPostDto.setEmailSendTo(postDto.getEmailSendTo());
                addAccountPostDto.setExternalGroupId(anaAccount.getExternalGroupId());
                addAccountPostDto.setUserType(applicationDto.getUserType());
                addAccountPostDto.setFirstName(anaAccount.getFirstName());
                addAccountPostDto.setLastName(anaAccount.getLastName());
                addAccountPostDto.setMobile(anaAccount.getMobile());
                RestfulResponse<String> restfulResponse = remoteClientUtils.addSsoAccount(addAccountPostDto,anaApplication.getInternalEndpoint(),anaApplication.getCode());
                if (null == restfulResponse || null == restfulResponse.getData()) {
                    if (sb.indexOf(anaApplication.getCode() + ",") < 0) {
                        sb.append(anaApplication.getCode());
                        sb.append(",");
                    }
                    continue;
                }
                bindingAccountId = restfulResponse.getData();
            }else{
                // Perform binding operations only. 
                Account client = remoteClientUtils.getAccountInfoByAccount(applicationDto.getAccount(),anaApplication.getInternalEndpoint());
                if (null == client || null == client.getExternalGroupId() || client.getExternalGroupId().isEmpty()
                        || !client.getExternalGroupId().equals(anaAccount.getExternalGroupId())) {
                    if (sb.indexOf(anaApplication.getCode() + ",") < 0) {
                        sb.append(anaApplication.getCode());
                        sb.append(",");
                    }
                    continue;
                }
                // Judging user status 
                if(client.getStatus().equals(AccountStatus.Terminated) || client.getStatus().equals(AccountStatus.Inactive)){
                    if (sb.indexOf(anaApplication.getCode() + ",") < 0) {
                        sb.append(anaApplication.getCode());
                        sb.append(",");
                    }
                    continue;
                }
                List<AnaAccountApplication> list = anaAccountApplicationRepository.findByAnaApplication_CodeAndBindingAccountId(anaApplication.getCode(), client.getAccountId());
                if(null != list && !list.isEmpty()){
                    if (sb.indexOf(anaApplication.getCode() + ",") < 0) {
                        sb.append(anaApplication.getCode());
                        sb.append(",");
                    }
                    continue;
                }
                bindingAccountId = client.getAccountId();
            }
            try {
                if(StringUtils.isNotBlank(bindingAccountId)) {
                    // Execution bindings 
                    AnaAccountApplication anaAccountApplication = new AnaAccountApplication();
                    anaAccountApplication.setAnaAccount(anaAccount);
                    anaAccountApplication.setAnaApplication(anaApplication);
                    anaAccountApplication.setCreatedTime(new Date());
                    anaAccountApplication.setCreatedBy(anaAccount.getId());
                    anaAccountApplication.setBindingAccountId(bindingAccountId);
                    anaAccountApplication.setStatus(AccountApplicationStatus.Temporary.getCode());
                    anaAccountApplicationRepository.saveAndFlush(anaAccountApplication);
                }
            } catch (Exception e) {
                if (sb.indexOf(anaApplication.getCode() + ",") < 0) {
                    sb.append(anaApplication.getCode());
                    sb.append(",");
                }
                logger.error("sso create client account error", e);
            }
        }
        return StringUtils.isNotBlank(sb.toString()) ? sb.substring(0, sb.lastIndexOf(",")) : "";
    }

    @Override
    public List<AccountDto> getClientAccounts(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getCode());
        }
        AnaAccount anaAccount = anaAccountRepository.findById(accountId);
        if(null == anaAccount){
            throw new BusinessException(SystemMsg.ServerErrorMsg.not_exist_account.getCode());
        }
        List<AccountDto> result = new ArrayList<>();
        for (AnaAccountApplication anaAccountApplication : anaAccount.getAnaAccountApplications()) {
            if (anaAccountApplication.getAnaApplication().getCode().equals(ApplicationCode.ANA.name())) {
                continue;
            }
            AccountQueryPostDto postDto = new AccountQueryPostDto();
            postDto.setSearch("%%");
            List<String> params = new ArrayList<>();
            params.add(anaAccountApplication.getBindingAccountId());
            postDto.setAccounts(params);
            RestfulResponse restfulResponse = remoteClientUtils.queryAccountList(postDto, anaAccountApplication.getAnaApplication().getInternalEndpoint(),anaAccountApplication.getAnaApplication().getCode());
            if (Objects.nonNull(restfulResponse) && Objects.nonNull(restfulResponse.getData()) && restfulResponse.getData() instanceof ArrayList) {
                List li = (List) restfulResponse.getData();
                li.stream().forEach(rs -> {
                    AccountDto dto = JsonUtils.readValue(JsonUtils.toJSon(rs), AccountDto.class);
                    dto.setSsoLoginId(anaAccount.getAccount());
                    dto.setApplications(anaAccountApplication.getAnaApplication().getCode());
                    result.add(dto);
                });
            }
        }
        return result;
    }

	@Override
	public String findBindingId(String bindingAccountId, String srcApplicationCode, String trgApplicationCode) {
		AnaAccountApplication src = anaAccountApplicationRepository.findByBinding(srcApplicationCode, bindingAccountId);
		List<AnaAccountApplication> list = anaAccountApplicationRepository.findByAnaAccount(src.getAnaAccount());
		for(AnaAccountApplication item : list){
			if(trgApplicationCode.equals(item.getAnaApplication().getCode())){
				return item.getBindingAccountId();
			}
		}
		return "";
	}
    
}
