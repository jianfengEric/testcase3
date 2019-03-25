package com.gea.portal.apv.service.impl;

import com.gea.portal.apv.dto.ApprovalRequestDto;
import com.gea.portal.apv.dto.ApprovalResponseDto;
import com.gea.portal.apv.entity.ApprovalCategoryItem;
import com.gea.portal.apv.entity.ApprovalResult;
import com.gea.portal.apv.repository.ApprovalCategoryItemRepository;
import com.gea.portal.apv.repository.ApprovalResultRepository;
import com.gea.portal.apv.repository.ApprovalResultSpecifications;
import com.gea.portal.apv.service.*;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.constant.DateCode;
import com.tng.portal.common.dto.DeployDetailDto;
import com.tng.portal.common.dto.DeployDetailEntry;
import com.tng.portal.common.dto.DeployDetailEntry.Detail;
import com.tng.portal.common.dto.DetailsDto;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.ewp.*;
import com.tng.portal.common.dto.mp.MpDetailDto;
import com.tng.portal.common.dto.mp.PoolAdjustmentDto;
import com.tng.portal.common.dto.mp.PoolDepositCashOutDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.dto.srv.ServiceBatchDto;
import com.tng.portal.common.dto.tre.CorrelationDataDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.enumeration.*;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.DateUtils;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Owen on 2018/9/3.
 */
@Service
@Transactional
public class ApprovalServiceImpl implements ApprovalService{

    private static final Logger logger = LoggerFactory.getLogger(ApprovalServiceImpl.class);

    @Autowired
    private ApprovalResultRepository approvalResultRepository;

    @Autowired
    private ApprovalCategoryItemRepository approvalCategoryItemRepository;

    @Autowired
    private EwpCallerService ewpCallerService;	
    
    @Autowired
    private MpCallerService mpCallerService;
    
    @Autowired
    private TreCallerService treCallerService;

    @Autowired
    private DpyCallerService dpyCallerService;
    
    @Autowired
    private AnaCallerService anaCallerService;
    
    @Autowired
    private SrvCallerService srvCallerService;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    
    private static final String DPY_SCHEDULE_DATE_FIELD_NAME = "scheduleDeployDate";
    private static final String DPY_UPDATE_DATE_FIELD_NAME = "updateDate";
    private static final String DPY_STATUS_FIELD_NAME = "deploymentStatus";

    @Override
    public PageDatas<ApprovalResponseDto> getApprovalList(ApprovalRequestDto pageRequestDto) throws ParseException {
        if(null == pageRequestDto){
            return null;
        }
        if(null == pageRequestDto.getPageNo()){
            pageRequestDto.setPageNo(1);
        }
        if(null == pageRequestDto.getPageSize()){
            pageRequestDto.setPageSize(10);
        }
        PageDatas<ApprovalResponseDto> pageDatas = new PageDatas<>(pageRequestDto.getPageNo(), pageRequestDto.getPageSize());
        Sort.Direction direction;
        if(null == pageRequestDto.getIsAscending()){
            direction = Sort.Direction.DESC;
        }else {
            direction = pageRequestDto.getIsAscending()?Sort.Direction.ASC:Sort.Direction.DESC;
        }
        Sort sort = new Sort(direction, getSort(pageRequestDto.getSortBy()));

        Specifications<ApprovalResult> where = getWhere(pageRequestDto);

        Page<ApprovalResult> page =  approvalResultRepository.findAll(where, pageDatas.pageRequest(sort));
        // ParticipantName dpyInfo
        List<String> geaRefId = page.getContent().stream().filter(item -> StringUtils.isNotBlank(item.getGeaParticipantRefId())).map(item -> item.getGeaParticipantRefId()).collect(Collectors.toList());
        List<Long> dpyId = page.getContent().stream().filter(item -> item.getDeployQueueRefId()!=null).map(item -> item.getDeployQueueRefId()).collect(Collectors.toList());
        Map<String, String> participantNameMap = new HashMap<>();
        Map<Long, Map<String, String>> dpyInfo = new HashMap<>();
        try{
	        participantNameMap = ewpCallerService.callGetParticipantName(geaRefId, pageRequestDto.getInstance().getValue()).getData();
	        dpyInfo = dpyCallerService.callDeploymentInfo(dpyId).getData();
        }catch(Exception e){
        	logger.error("conn ewp/dpy error", e);
        }
        
        List<ApprovalResponseDto> list = new ArrayList<>();
        for(ApprovalResult item : page.getContent()){
        	ApprovalResponseDto dto = new ApprovalResponseDto();
            dto.setRequestUserId(item.getRequestUserId());
            dto.setRequestUserName(item.getRequestUserName());
            dto.setRequestDate(DateUtils.formatDate(item.getRequestDate(), DateCode.dateFormatSs));
            String approvalStatus= item.getApprovalStatus().getApvListView();
            dto.setApprovalStatus(approvalStatus);
            dto.setInstance(item.getCurrentEnvir().getValue());
            if(Objects.nonNull(item.getApprovalBy())){
                dto.setApprovalUserId(item.getApprovalBy().getId());
                dto.setApprovalUserName(item.getApprovalBy().getFullName());
            }
            dto.setParticipantRefId(item.getGeaParticipantRefId());
            dto.setApprovalDate(DateUtils.formatDate(item.getApprovalDate(), DateCode.dateFormatSs));
            dto.setApprovalRemark(item.getApprovalRemark());

            if(dpyInfo.get(item.getDeployQueueRefId())!=null) {
                dto.setDeploymentDate(dpyInfo.get(item.getDeployQueueRefId()).get(DPY_SCHEDULE_DATE_FIELD_NAME));
                dto.setRealDeploymentDate(dpyInfo.get(item.getDeployQueueRefId()).get(DPY_UPDATE_DATE_FIELD_NAME));
                dto.setDeploymentStatus(dpyInfo.get(item.getDeployQueueRefId()).get(DPY_STATUS_FIELD_NAME));
                dto.setDeploymentType(StringUtils.isNotBlank(dpyInfo.get(item.getDeployQueueRefId()).get(DPY_SCHEDULE_DATE_FIELD_NAME))?"Schedule Deployment":"Deploy Now");
            }
            if(Objects.nonNull(item.getApprovalCategoryItem())) {
                dto.setApprovalType(item.getApprovalCategoryItem().getCode().getValue());
            }
            
            dto.setParticipantName(participantNameMap.get(dto.getParticipantRefId()));
            list.add(dto);
        }
        
        pageDatas.initDatas(list, page.getTotalElements(), page.getTotalPages());
        return pageDatas;
    }


    private Specifications<ApprovalResult> getWhere(ApprovalRequestDto pageRequestDto) throws ParseException{
        Specifications<ApprovalResult> where = ApprovalResultSpecifications.base();
        if(Objects.isNull(pageRequestDto)){
            return where;
        }
        if(StringUtils.isNotBlank(pageRequestDto.getApprovalUserName())){
            where = where.and(ApprovalResultSpecifications.approvalUserNameLike(pageRequestDto.getApprovalUserName()));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getParticipantRefId())){
            where = where.and(ApprovalResultSpecifications.geaParticipantRefIdLike(pageRequestDto.getParticipantRefId()));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getRequestUserName())){
            where = where.and(ApprovalResultSpecifications.requestUserNameLike(pageRequestDto.getRequestUserName()));
        }
        if(Objects.nonNull(pageRequestDto.getApprovalStatus())){
            where = where.and(ApprovalResultSpecifications.approvalStatusEqual(pageRequestDto.getApprovalStatus()));
        }
        if(Objects.nonNull(pageRequestDto.getInstance())){
            where = where.and(ApprovalResultSpecifications.currentEnvirEqual(pageRequestDto.getInstance()));
        }
        if(Objects.nonNull(pageRequestDto.getApprovalType())){
            where = where.and(ApprovalResultSpecifications.approvalTypeEqual(pageRequestDto.getApprovalType()));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getApprovalRemark())){
            where = where.and(ApprovalResultSpecifications.approvalRemarkLike(pageRequestDto.getApprovalRemark()));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getBeginApprovalDate())){
            where = where.and(ApprovalResultSpecifications.beginApprovalDate(pageRequestDto.getBeginApprovalDate()));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getEndApprovalDate())){
            where = where.and(ApprovalResultSpecifications.endApprovalDate(pageRequestDto.getEndApprovalDate()+":59"));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getBeginRequestDate())){
            where = where.and(ApprovalResultSpecifications.beginRequestDate(pageRequestDto.getBeginRequestDate()));
        }
        if(StringUtils.isNotBlank(pageRequestDto.getEndRequestDate())){
            where = where.and(ApprovalResultSpecifications.endRequestDate(pageRequestDto.getEndRequestDate()+":59"));
        }

        return where;
    }

    private String getSort(String sortBy){
        return StringUtils.isBlank(sortBy)||StringUtils.isBlank(orderMap().get(sortBy))?orderMap().get("default"):orderMap().get(sortBy);
    }

    private static Map<String,String> orderMap(){
        Map<String,String> orderMap = new HashMap<>();
        orderMap.put("default", "requestDate");
        orderMap.put("requestUserName","requestUserName");
        orderMap.put("requestDate","requestDate");
        orderMap.put("participantRefId","geaParticipantRefId");
        orderMap.put("approvalType","approvalCategoryItem.code");
        orderMap.put("approvalStatus","approvalStatus");
        orderMap.put("approvalRemark","approvalRemark");
        orderMap.put("approvalUserName","approvalBy.firstName");
        orderMap.put("approvalDate","approvalDate");
        return orderMap;
    }

    @Override
    public DetailsDto getApprovalDetails(Instance instance, ApprovalType approvalType,String geaParticipantRefId, String moneyPoolRefId,Long serviceBatchId,Long exchangeRateFileId) {
        if(StringUtils.isBlank(geaParticipantRefId) && StringUtils.isBlank(moneyPoolRefId) && serviceBatchId==null && exchangeRateFileId==null){
        	throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        DetailsDto detailsDto = new DetailsDto();
        String module =ApprovalType.getModule(approvalType);
        try{
	        if(module.equals(ApplicationContext.Modules.EWP)){
	            EwpDetailDto ewpDetailDto = ewpCallerService.callGetDetail(geaParticipantRefId, instance.getValue()).getData();
	            detailsDto.setEwpDetailDto(ewpDetailDto);
	        } else if(module.equals(ApplicationContext.Modules.MP)) {
	            MpDetailDto mpDetailDto = mpCallerService.callGetDetail(moneyPoolRefId, instance.toString()).getData();
                String status=mpDetailDto.getMoneyPoolDto().getStatus();
                if(status.equalsIgnoreCase(MoneyPoolStatus.PENDING_FOR_PROCESS.getValue())){
                    mpDetailDto.getMoneyPoolDto().setStatus(MoneyPoolStatus.SUSPEND.getValue());
                }
	            detailsDto.setMpDetailDto(mpDetailDto);
	        } else if(module.equals(ApplicationContext.Modules.SRV)){
	        	ServiceBatchChgReqDto serviceBatchChgReqDto = srvCallerService.callGetDetail(serviceBatchId).getData();
	            detailsDto.setServiceBatchChgReqDto(serviceBatchChgReqDto);
	        } else if(module.equals(ApplicationContext.Modules.TRE)){
	        	ExchangeRateFileDto exchangeRateFileDto = treCallerService.callGetDetail(exchangeRateFileId,instance).getData();
	            detailsDto.setExchangeRateFileDto(exchangeRateFileDto);
	        }
        }catch(Exception e){
        	logger.error("conn to ewp error", e);
        	throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        return detailsDto;
    }

    @Override
    public RestfulResponse<Boolean> approval(String requestApprovalId, ApprovalType approvalType, String approvalRemark, String deployDate) {
        String module = ApprovalType.getModule(approvalType);

        RestfulResponse<RequestApprovalDto> restfulResponse = null;
        try{
        	String loginAccountId = userService.getLoginAccountId();
        	if (module.equals(ApplicationContext.Modules.EWP)) {
        		restfulResponse = ewpCallerService.callApproval(requestApprovalId,loginAccountId);
        	} else if (module.equals(ApplicationContext.Modules.MP)) {
        		restfulResponse = mpCallerService.callApproval(requestApprovalId,loginAccountId);
        	} else if (module.equals(ApplicationContext.Modules.TRE)) {
        		restfulResponse = treCallerService.callApproval(requestApprovalId,loginAccountId);
        	} else if (module.equals(ApplicationContext.Modules.SRV)){
        		restfulResponse = srvCallerService.callApproval(requestApprovalId,loginAccountId);
        	}else{
        		throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        	}
        }catch(Exception e){
        	logger.error("send ewp/mp/tre/srv error",e);
        	throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        if(restfulResponse.hasFailed()){
        	throw new BusinessException(Integer.valueOf(restfulResponse.getErrorCode()));
        }
        RequestApprovalDto approvalDto = restfulResponse.getData();
        ApprovalResult approvalResult = this.saveApprovalResult(approvalType, approvalRemark, approvalDto, module);

        Long deployRefId = null;
        try{
	        if(approvalType==ApprovalType.DEPOSIT || approvalType==ApprovalType.CASH_OUT || approvalType==ApprovalType.ADJUSTMENT){
	            if(dpyCallerService.callIsDeploy(approvalDto.getGeaParticipantRefId(), approvalDto.getCurrentEnvir()).getData()){
	                deployRefId = this.deployMp(requestApprovalId, deployDate);
	            }
	        } else if(approvalType==ApprovalType.SERVICE_MARKUP){
	        	deployRefId = this.deploySrv(requestApprovalId, deployDate);
	        } else if(approvalType==ApprovalType.EXCHANGE_RATE_SETTING){
	        	deployRefId = this.deployTre(requestApprovalId, deployDate);
	        } else{
	            Long ewpRequestApprovalId = null;
	            Long mpRequestApprovalId = null;
	            if(module.equals(ApplicationContext.Modules.EWP)){
	                ewpRequestApprovalId = Long.valueOf(requestApprovalId);
	            }else if(module.equals(ApplicationContext.Modules.MP)){
	                mpRequestApprovalId = Long.valueOf(requestApprovalId);
	            }
	            if(ewpCallerService.callIsNeedDeploy(approvalDto.getGeaParticipantRefId(), approvalDto.getCurrentEnvir(), ewpRequestApprovalId, mpRequestApprovalId).getData()){
	                deployRefId = this.deploy(approvalDto.getGeaParticipantRefId(), approvalDto.getCurrentEnvir(), deployDate);
	            }
	        }
        }catch(Exception e){
        	logger.error("send dpy error",e);
        	throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }

        approvalResult.setDeployQueueRefId(deployRefId);
        
        
        return RestfulResponse.nullData();
    }

    private ApprovalResult saveApprovalResult(ApprovalType approvalType, String approvalRemark, RequestApprovalDto approvalDto, String module) {
        Account loginAccount = userService.getCurrentAccountInfo();
        ApprovalResult approvalResult = new ApprovalResult();
        approvalResult.setApprovalBy(accountService.getAccount(loginAccount.getAccountId()));
        approvalResult.setApprovalDate(new Date());
        approvalResult.setApprovalRemark(approvalRemark);
        approvalResult.setApprovalStatus(approvalDto.getStatus());
        approvalResult.setCurrentEnvir(approvalDto.getCurrentEnvir());
        approvalResult.setGeaParticipantRefId(approvalDto.getGeaParticipantRefId());
        approvalResult.setGeaMoneypoolRefId(approvalDto.getGeaMoneyPoolRefId());
		try {
			String requestUserId = anaCallerService.callFindBindingId(approvalDto.getCreateBy(), module, ApplicationContext.Modules.APV).getData();
			approvalResult.setRequestUserId(requestUserId);
            approvalResult.setRequestUserName(accountService.getAccountName(requestUserId));
		} catch (Exception e) {
			logger.error("remote error",e);
		}

        approvalResult.setRequestDate(approvalDto.getCreateDate());
        if (module.equals(ApplicationContext.Modules.EWP)) {
            approvalResult.setEwpApvReqId(Long.valueOf(approvalDto.getId()));
        } else if (module.equals(ApplicationContext.Modules.MP)) {
            approvalResult.setMpApvReqId(Long.valueOf(approvalDto.getId()));
        } else if(module.equals(ApplicationContext.Modules.TRE)){
            approvalResult.setExchRateApvReqId(Long.valueOf(approvalDto.getId()));
        } else if(module.equals(ApplicationContext.Modules.SRV)){
        	approvalResult.setSrvApvReqId(Long.valueOf(approvalDto.getId()));
        }
        ApprovalCategoryItem approvalCategoryItem = approvalCategoryItemRepository.findByCode(approvalType);
        approvalResult.setApprovalCategoryItem(approvalCategoryItem);
        return approvalResultRepository.save(approvalResult);
    }

    @Override
    public void reject(String requestApprovalId, ApprovalType approvalType, String approvalRemark) {
    	String module =ApprovalType.getModule(approvalType);
    	RestfulResponse<RequestApprovalDto> response = null;
        try {
        	String loginAccountId = userService.getLoginAccountId();
            if(module.equals(ApplicationContext.Modules.EWP)){
            	response = ewpCallerService.callReject(requestApprovalId,loginAccountId);
            } else if(module.equals(ApplicationContext.Modules.MP)) {
            	response = mpCallerService.callReject(requestApprovalId,loginAccountId);
            } else if(module.equals(ApplicationContext.Modules.SRV)){
            	response = srvCallerService.callReject(requestApprovalId,loginAccountId);
            } else if(module.equals(ApplicationContext.Modules.TRE)){
            	response = treCallerService.callReject(requestApprovalId,loginAccountId);
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        	throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }

        if(response!=null){//sonarmodify
            if(response.hasFailed()){
                throw new BusinessException(Integer.valueOf(response.getErrorCode()));
            }
            saveApprovalResult(approvalType, approvalRemark, response.getData(), module);
        }
    }
    
    private DeployDetailDto getParticipantAndProgramAndGateway(String geaRefId, FullCompanyInfoDto fullCompanyInfoDto, GatewaySettingDto gatewaySettingDto, DeployDetailDto deployDetailDto) {
        DeployDetailEntry participantDeployDetailsDto = new DeployDetailEntry(ServiceName.participant);
        DeployDetailEntry ewpProgramSettingDeployDetailsDto = new DeployDetailEntry(ServiceName.ewpProgramSetting);
        DeployDetailEntry ewpApiGatewayConfigDeployDetailsDto = new DeployDetailEntry(ServiceName.ewpApiGatewayConfig);

        // Set up participant Value 
        fullCompanyInfoDto.getOldFullCompanyInformationDto().stream().findFirst().ifPresent(item -> {
            DeployDetailEntry.Detail participantDeployDetailDto= new Detail();
            participantDeployDetailDto.setGeaParticipantRefId(geaRefId);
            participantDeployDetailDto.setGeaSysPid(fullCompanyInfoDto.getGeaSysPid().toString());
            participantDeployDetailDto.setParticipantName(item.getParticipantName());
            participantDeployDetailDto.setParticipantStatus(item.getParticipantStatus().equals(ParticipantStatus.ACTIVE.getValue())?"true":"false");
            participantDeployDetailsDto.addDetail(participantDeployDetailDto);
        });
        // Set up ewpProgramSetting Value 
        gatewaySettingDto.getOldApiGatewaySettingDto().stream().findFirst().ifPresent(item -> {
            DeployDetailEntry.Detail ewpProgramSettingDeployDetailDto = new Detail();
            ewpProgramSettingDeployDetailDto.setGeaParticipantRefId(geaRefId);
            ewpProgramSettingDeployDetailDto.setServerApiKey(item.getServerApiKey());
            ewpProgramSettingDeployDetailDto.setServerSecretKey(item.getServerSecretKey() == null ? "" : item.getServerSecretKey());
            ewpProgramSettingDeployDetailDto.setApiGatewayUrl(item.getApiGatewayUrl() == null ? "" : item.getApiGatewayUrl());
            ewpProgramSettingDeployDetailsDto.addDetail(ewpProgramSettingDeployDetailDto);
        });
        // Set up ewpApiGatewayConfig Value 
        gatewaySettingDto.getOldApiGatewaySettingDto().stream().findFirst().ifPresent(item -> {
            DeployDetailEntry.Detail ewpApiGatewayConfigDeployDetailDto = new Detail();
            ewpApiGatewayConfigDeployDetailDto.setGeaParticipantRefId(geaRefId);
            ewpApiGatewayConfigDeployDetailDto.setServerApiKey(item.getServerApiKey());
            ewpApiGatewayConfigDeployDetailDto.setServerLogEncryptionKey(item.getServerLogEncryptionKey());
            ewpApiGatewayConfigDeployDetailDto.setSecurityBasicEnable(item.getSecurityBasicEnable() == null ? "0" : item.getSecurityBasicEnable().toString());
            ewpApiGatewayConfigDeployDetailDto.setSecurityBasicPath(item.getSecurityBasicPath() == null ? "" : item.getSecurityBasicPath());
            ewpApiGatewayConfigDeployDetailDto.setSecurityUserName(item.getSecurityUserName());
            ewpApiGatewayConfigDeployDetailDto.setSecurityUserPwd(item.getSecurityUserPwd());
            ewpApiGatewayConfigDeployDetailDto.setMgtSecurityEnable(item.getMgtHealthRefreshEnable() == null ? "0" : item.getMgtHealthRefreshEnable().toString());
            ewpApiGatewayConfigDeployDetailDto.setMgtContextPath(item.getMgtContextPath());
            ewpApiGatewayConfigDeployDetailDto.setMgtHealthRefreshEnable(item.getMgtHealthRefreshEnable() == null ? "0" : item.getMgtHealthRefreshEnable().toString());
            ewpApiGatewayConfigDeployDetailDto.setEndpointsInfoSensitive(item.getEndpointsInfoSensitive() == null ? "0" : item.getEndpointsInfoSensitive().toString());
            ewpApiGatewayConfigDeployDetailDto.setEndpointsHealthSensitive(item.getEndpointsHealthSensitive() == null ? "0" : item.getEndpointsHealthSensitive().toString());
            ewpApiGatewayConfigDeployDetailDto.setEndpointsRestartEnable(item.getEndpointsInfoSensitive() == null ? "0" : item.getEndpointsInfoSensitive().toString());
            ewpApiGatewayConfigDeployDetailDto.setServerPort(item.getServerPort());
            ewpApiGatewayConfigDeployDetailDto.setServerLogPath(item.getServerLogPath());
            ewpApiGatewayConfigDeployDetailDto.setServerLogTotalSize(item.getServerLogTotalSize() == null ? "0" : item.getServerLogTotalSize().toString());
            ewpApiGatewayConfigDeployDetailDto.setSizeUnit(item.getSizeUnit());
            ewpApiGatewayConfigDeployDetailDto.setServerLogMaxHistory(item.getServerLogMaxHistory() == null ? "0" : item.getServerLogMaxHistory().toString());
            ewpApiGatewayConfigDeployDetailDto.setServerLogPattern(item.getServerLogPattern());
            ewpApiGatewayConfigDeployDetailDto.setServerHealthThreshold(item.getServerHealthThreshold());
            ewpApiGatewayConfigDeployDetailDto.setServerZipKey(item.getServerZipKey());
            ewpApiGatewayConfigDeployDetailDto.setServerZipCompressionLevel(item.getServerZipCompressionLevel() == null ? "0" : item.getServerZipCompressionLevel().toString());
            ewpApiGatewayConfigDeployDetailDto.setServerRoutesMth(item.getServerRoutesMth());
            ewpApiGatewayConfigDeployDetailDto.setServerRoutesSr(item.getServerRoutesSr());
            ewpApiGatewayConfigDeployDetailDto.setServerRoutesMeta(item.getServerRoutesMeta());
            ewpApiGatewayConfigDeployDetailDto.setServerConnectionTimeout(item.getServerConnectionTimeout() == null ? "0" : item.getServerConnectionTimeout().toString());
            ewpApiGatewayConfigDeployDetailDto.setServerRequestTimeout(item.getServerRequestTimeout() == null ? "0" : item.getServerRequestTimeout().toString());
            ewpApiGatewayConfigDeployDetailDto.setServerRetryTimes(item.getServerRetryTimes() == null ? "0" : item.getServerRetryTimes().toString());
            ewpApiGatewayConfigDeployDetailDto.setServerHealcheckEndpoint(item.getServerHealcheckEndpoint());
            ewpApiGatewayConfigDeployDetailDto.setServerMessageEndpoint(item.getServerMessageEndpoint());
            ewpApiGatewayConfigDeployDetailDto.setServerLogEnableEncrypt(item.getServerLogEnableEncrypt().toString());
            ewpApiGatewayConfigDeployDetailsDto.addDetail(ewpApiGatewayConfigDeployDetailDto);
        });
        deployDetailDto.addDeployDetailEntry(participantDeployDetailsDto);
        deployDetailDto.addDeployDetailEntry(ewpProgramSettingDeployDetailsDto);
        deployDetailDto.addDeployDetailEntry(ewpApiGatewayConfigDeployDetailsDto);
        return deployDetailDto;
    }

    
    private DeployDetailDto getServiceAndCurrency(ServiceSettingRequestDto serviceSettingRequestDto, DeployDetailDto deployDetailDto){
        DeployDetailEntry serviceDeployDetailsDto = new DeployDetailEntry(ServiceName.ewpServiceMarkupSetting);
        DeployDetailEntry currencyDeployDetailsDto = new DeployDetailEntry(ServiceName.ewpServiceCurrencySetting);

        serviceSettingRequestDto.getOldServiceSettingDtoList().forEach(item -> {
            DeployDetailEntry.Detail serviceDeployDetailDto = new Detail();
            serviceDeployDetailDto.setGeaServiceRefId(item.getServiceId());
            serviceDeployDetailDto.setServiceCode(item.getServiceCode());
            serviceDeployDetailDto.setMarkup(item.getMarkup().toString());
            serviceDeployDetailsDto.addDetail(serviceDeployDetailDto);

            item.getFromCurrencyDto().forEach(i -> {
                i.getToCurrencyDto().forEach(o -> {
                    DeployDetailEntry.Detail currencyDeployDetailDto = new Detail();
                    currencyDeployDetailDto.setGeaServiceRefId(item.getServiceId());
                    currencyDeployDetailDto.setCurrencyFrom(i.getCurrency());
                    currencyDeployDetailDto.setCurrencyTo(o.getCurrency());
                    if ("ACTIVE".equalsIgnoreCase(item.getServiceStatus()) && o.getEnable()) {
                        currencyDeployDetailDto.setEnable("true");
                    } else {
                        currencyDeployDetailDto.setEnable("false");
                    }
                    currencyDeployDetailDto.setAdminFee(o.getAdminFee()==null ? "0" : o.getAdminFee().toString());
                    currencyDeployDetailDto.setChangeNameAdminFee(o.getChangeNameAdminFee() == null ? "0" : o.getChangeNameAdminFee().toString());
                    currencyDeployDetailDto.setCancelAdminFee(o.getCancelAdminFee() == null ? "0" : o.getCancelAdminFee().toString());
                    currencyDeployDetailsDto.addDetail(currencyDeployDetailDto);
                });
            });
        });

        deployDetailDto.addDeployDetailEntry(serviceDeployDetailsDto);
        deployDetailDto.addDeployDetailEntry(currencyDeployDetailsDto);

        return deployDetailDto;
    }

    private DeployDetailDto getMoneyPoolAndMoneyPoolServiceMap(String geaRefId,ServiceAssignmentDto serviceAssignmentDto, DeployDetailDto deployDetailDto){
        DeployDetailEntry moneyPoolDeployDetailsDto = new DeployDetailEntry(ServiceName.ewpMoneypool);

        DeployDetailEntry moneyPoolServiceMapDeployDetailsDto = new DeployDetailEntry(ServiceName.ewpMoneypoolServiceMap);

        serviceAssignmentDto.getOldMoneyPoolDtoList().forEach(item -> {
            DeployDetailEntry.Detail moneyPoolDeployDetailDto = new Detail();
            moneyPoolDeployDetailDto.setGeaParticipantRefId(geaRefId);
            moneyPoolDeployDetailDto.setGeaMoneyPoolRefId(item.getGeaMoneyPoolRefId());
            moneyPoolDeployDetailDto.setMoneyPoolStatus(item.getStatus().equals("ACTIVE") ? "ACTIVE" : "INACTIVE");
            moneyPoolDeployDetailDto.setCurrency(item.getCurrency());
            moneyPoolDeployDetailDto.setAlertLine(item.getAlertLevel());
            moneyPoolDeployDetailsDto.addDetail(moneyPoolDeployDetailDto);

            item.getServiceSettingDtoList().forEach(i -> {
                DeployDetailEntry.Detail moneyPoolServiceMapDeployDetailDto = new Detail();
                moneyPoolServiceMapDeployDetailDto.setGeaMoneyPoolRefId(item.getGeaMoneyPoolRefId());
                moneyPoolServiceMapDeployDetailDto.setGeaServiceRefId(i.getServiceId());
                moneyPoolServiceMapDeployDetailDto.setServiceCode(i.getServiceCode());
                moneyPoolServiceMapDeployDetailsDto.addDetail(moneyPoolServiceMapDeployDetailDto);
            });
        });

        deployDetailDto.addDeployDetailEntry(moneyPoolDeployDetailsDto);
        deployDetailDto.addDeployDetailEntry(moneyPoolServiceMapDeployDetailsDto);

        return deployDetailDto;
    }

    private DeployDetailDto getCutOffTime(String geaRefId, CutOffTimeDto cutOffTimeDto, DeployDetailDto deployDetailDto) {
        DeployDetailEntry cotOffTimeDetailsDto = new DeployDetailEntry(ServiceName.settlementCutOffTimeSetting);

        // Set up cotOffTime Value 
        DeployDetailEntry.Detail detailDto= new Detail();
        detailDto.setGeaParticipantRefId(geaRefId);
        detailDto.setDailySettlementCutOffTime(cutOffTimeDto.getOldCutOffTime());
        cotOffTimeDetailsDto.addDetail(detailDto);
        deployDetailDto.addDeployDetailEntry(cotOffTimeDetailsDto);
        return deployDetailDto;
    }

    private Long deploy(String geaParticipantRefId,Instance instance,String deployDate) {
    	try {
	        EwpDetailDto ewpDetailDto = ewpCallerService.callGetDetail(geaParticipantRefId, instance.toString()).getData();
	        DeployDetailDto deployDetailDto = new DeployDetailDto(instance.getValue(), deployDate);

            deployDetailDto = getParticipantAndProgramAndGateway(geaParticipantRefId, ewpDetailDto.getFullCompanyInfoDto(), ewpDetailDto.getGatewaySettingDto(), deployDetailDto);

            deployDetailDto = getServiceAndCurrency(ewpDetailDto.getServiceSettingRequestDto(), deployDetailDto);

	        deployDetailDto = getMoneyPoolAndMoneyPoolServiceMap(geaParticipantRefId,ewpDetailDto.getServiceAssignmentDto(), deployDetailDto);

	        deployDetailDto = getCutOffTime(geaParticipantRefId,ewpDetailDto.getCutOffTimeDto(), deployDetailDto);
	        
	        RestfulResponse<String> rsp = ewpCallerService.callSaveDeployment(geaParticipantRefId, instance);
	        deployDetailDto.setDeployRefId(Long.valueOf(rsp.getData()));
	        String o = dpyCallerService.callDeploy(deployDetailDto).getData();
            return Long.valueOf(o);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
		}
    }

    /**
     *  In the light of Adjustment/Deposit/Cashout  A  deploy
     */
	private Long deployMp(String requestApprovalId,String deployDate) {
		try {
			// Check data 
			RequestApprovalDto approvel = mpCallerService.callGetRequestApproval(requestApprovalId).getData();
			ServiceName serviceName = null;
			if(approvel.getApprovalType() == ApprovalType.ADJUSTMENT){
				serviceName = ServiceName.adjustment;
			}else if(approvel.getApprovalType() == ApprovalType.DEPOSIT){
				serviceName = ServiceName.deposit;
			}else if(approvel.getApprovalType() == ApprovalType.CASH_OUT){
				serviceName = ServiceName.cashout;
			}else{
				throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(),"approvalType is not correct");
			}
			// Is the master data synchronized? 
			if(!dpyCallerService.callIsDeploy(approvel.getGeaParticipantRefId(), approvel.getCurrentEnvir()).getData()){
				throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(),"Participant can't sync");
			}
            RestfulResponse<String> rsp = mpCallerService.callSaveDeployment(requestApprovalId);
			// generate JSON
			DeployDetailEntry deployDetailEntry = new DeployDetailEntry(serviceName);
			if(serviceName == ServiceName.adjustment){
				PoolAdjustmentDto adj = mpCallerService.callGetAdjustment(Long.valueOf(approvel.getEwpPoolAdjustId())).getData();
				DeployDetailEntry.Detail detail = new Detail();
				detail.setGeaMoneyPoolRefId(approvel.getGeaMoneyPoolRefId());
                detail.setAdjustType(adj.getAdjustType()==0?"deduct" : "add");
                detail.setAmount(adj.getAmount().toString());
                detail.setGeaTxRefNo(adj.getGeaTxRefNo());
				deployDetailEntry.getDetails().add(detail);
			}else if(serviceName == ServiceName.deposit || serviceName == ServiceName.cashout){
				PoolDepositCashOutDto dto = mpCallerService.callGetDepositCashOut(Long.valueOf(approvel.getEwpDepositCashoutId())).getData();
				DeployDetailEntry.Detail detail = new Detail();
				detail.setGeaMoneyPoolRefId(approvel.getGeaMoneyPoolRefId());
                detail.setAmount(dto.getAmount().toString());
                detail.setGeaTxRefNo(dto.getGeaTxRefNo());
				deployDetailEntry.getDetails().add(detail);
			}else{
				throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(),"serviceName is not correct");
			}
			DeployDetailDto deployDetailDto = new DeployDetailDto();
			deployDetailDto.setDeployEnvir(approvel.getCurrentEnvir().toString());
			deployDetailDto.setDeployScheduleDate(deployDate);
			deployDetailDto.setDeployDetail(new ArrayList<>());
			deployDetailDto.setGeaParticipantRefId(approvel.getGeaParticipantRefId());
			deployDetailDto.getDeployDetail().add(deployDetailEntry);
            deployDetailDto.setDeployRefId(Long.valueOf(rsp.getData()));
			// Send out dpy
            String o = dpyCallerService.callDeployMp(deployDetailDto).getData();
            return Long.valueOf(o);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
		}
			
	}
	
    /**
     *  markup的dpy  
     */
	private Long deploySrv(String requestApprovalId,String deployDate) {
		try {
			// Check data 
			RequestApprovalDto approvelDto = srvCallerService.callGetRequestApproval(requestApprovalId).getData();
			if(approvelDto.getApprovalType() != ApprovalType.SERVICE_MARKUP){
				throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(),"approvalType is not correct");
			}
            RestfulResponse<String> rsp = srvCallerService.callSaveDeployment(requestApprovalId);
			// generate JSON
			DeployDetailEntry deployDetailEntry = new DeployDetailEntry(ServiceName.serviceMarkup);
			ServiceBatchChgReqDto srvData = srvCallerService.callGetDetail(Long.valueOf(approvelDto.getBatchId())).getData();
			for(ServiceBatchDto item : srvData.getServiceBatchDtoList()){
				DeployDetailEntry.Detail detail = new Detail();
				detail.setGeaServiceRefId(item.getServiceId().toString());
	            detail.setMarkup(item.getMarkup());
				deployDetailEntry.getDetails().add(detail);
			}
			
			DeployDetailDto deployDetailDto = new DeployDetailDto();
			deployDetailDto.setDeployEnvir(approvelDto.getCurrentEnvir().toString());
			deployDetailDto.setDeployScheduleDate(deployDate);
			deployDetailDto.setDeployDetail(new ArrayList<>());
			deployDetailDto.getDeployDetail().add(deployDetailEntry);
            deployDetailDto.setDeployRefId(Long.valueOf(rsp.getData()));
			// Send out dpy
            String o = dpyCallerService.callDeploySrv(deployDetailDto).getData();
            return Long.valueOf(o);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
		}
	}
	
    /**
     *  汇率的dpy  
     */
	private Long deployTre(String requestApprovalId,String deployDate) {
		try {
			// Check data 
			RequestApprovalDto approvelDto = treCallerService.callGetRequestApproval(requestApprovalId).getData();
			if(approvelDto.getApprovalType() != ApprovalType.EXCHANGE_RATE_SETTING){
				throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode(),"approvalType is not correct");
			}
            RestfulResponse<String> rsp = treCallerService.callSaveDeployment(requestApprovalId);
			// generate JSON
			DeployDetailEntry deployDetailEntry = new DeployDetailEntry(ServiceName.exchangeRate);
			ExchangeRateFileDto excData = treCallerService.callGetDetail(Long.valueOf(approvelDto.getExchRateFileId()),approvelDto.getCurrentEnvir()).getData();
			for(CorrelationDataDto item : excData.getNewData()){
				DeployDetailEntry.Detail detail = new Detail();
				detail.setCurrencyFrom(item.getCurrFrom());
	            detail.setCurrencyTo(item.getCurrTo());
	            detail.setRate(item.getCurrencyExchangeRate());
				deployDetailEntry.getDetails().add(detail);
			}
			
			DeployDetailDto deployDetailDto = new DeployDetailDto();
			deployDetailDto.setDeployEnvir(approvelDto.getCurrentEnvir().toString());
			deployDetailDto.setDeployScheduleDate(deployDate);
			deployDetailDto.setDeployDetail(new ArrayList<>());
			deployDetailDto.setGeaParticipantRefId(approvelDto.getGeaParticipantRefId());
			deployDetailDto.getDeployDetail().add(deployDetailEntry);
            deployDetailDto.setDeployRefId(Long.valueOf(rsp.getData()));
			// Send out dpy
            String o = dpyCallerService.callDeployTre(deployDetailDto).getData();
            return Long.valueOf(o);
		} catch (Exception e) {
			logger.error("error",e);
			throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
		}
	}

	@Override
	public RestfulResponse<Boolean> isNeedDeploy(String requestApprovalId, ApprovalType approvalType) {
		try {
            RestfulResponse<Boolean> isNeedDeploy = RestfulResponse.ofData(Boolean.FALSE);
			if(approvalType==ApprovalType.DEPOSIT || approvalType==ApprovalType.CASH_OUT || approvalType==ApprovalType.ADJUSTMENT){
				RequestApprovalDto raDto = mpCallerService.callGetRequestApproval(requestApprovalId).getData();
				isNeedDeploy = dpyCallerService.callIsDeploy(raDto.getGeaParticipantRefId(), raDto.getCurrentEnvir());
				
			}else if(approvalType==ApprovalType.SERVICE_MARKUP){
				isNeedDeploy = RestfulResponse.ofData(Boolean.TRUE);
				
			}else if(approvalType==ApprovalType.EXCHANGE_RATE_SETTING){
				isNeedDeploy = RestfulResponse.ofData(Boolean.TRUE);
				
			}else{
				String module = ApprovalType.getModule(approvalType);
				if(module.equals(ApplicationContext.Modules.EWP)){
					RequestApprovalDto app = ewpCallerService.callRequestApproval(requestApprovalId).getData();
					boolean res1 = ewpCallerService.callIsNeedDeploy(app.getGeaParticipantRefId(), app.getCurrentEnvir(), Long.valueOf(requestApprovalId), null).getData();
					boolean res2 = dpyCallerService.callIsDeploy(app.getGeaParticipantRefId(), app.getCurrentEnvir()).getData();
					isNeedDeploy = RestfulResponse.ofData(res1);
					//PROD  Environment for the first time approval Do not pop up deploy window 
					if(app.getCurrentEnvir().equals(Instance.PROD)){
						isNeedDeploy = RestfulResponse.ofData(res1 && res2 ); 
					}
				} else if(module.equals(ApplicationContext.Modules.MP)) {
		        	RequestApprovalDto app = mpCallerService.callGetRequestApproval(requestApprovalId).getData();
		        	isNeedDeploy = ewpCallerService.callIsNeedDeploy(app.getGeaParticipantRefId(), app.getCurrentEnvir(), null, Long.valueOf(requestApprovalId));
		        }
				
			}
			return isNeedDeploy;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
		}
		
	}

}
