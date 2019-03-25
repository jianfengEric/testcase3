package com.gea.portal.ewp.service.impl;

import com.gea.portal.ewp.entity.*;
import com.gea.portal.ewp.repository.*;
import com.gea.portal.ewp.service.AnaCallerService;
import com.gea.portal.ewp.service.EwalletParticipantService;
import com.gea.portal.ewp.service.RequestApprovalService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.ApplicationContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Dell on 2018/8/29.
 */
@Service
@Transactional
public class RequestApprovalServiceImpl implements RequestApprovalService{

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private RequestApprovalRepository requestApprovalRepository;

    @Autowired
    private EwpCompanyFormRepository ewpCompanyFormRepository;
    @Autowired
    private EwpServiceRepository ewpServiceRepository;
    @Autowired
    private EwpServiceMoneyPoolRepository ewpMoneyPoolRepository;
    @Autowired
    private EwpGatewayConfigRepository ewpGatewayConfigRepository;
    @Autowired
    private EwalletParticipantRepository ewalletParticipantRepository;
    @Autowired
    private EwpStatusChangeRepository ewpStatusChangeRepository;

    @Autowired
    private EwalletParticipantService ewalletParticipantService;

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private EwpServiceSettlementRepository ewpServiceSettlementRepository;
    
    @Autowired
    private EwpServiceMoneyPoolRepository ewpServiceMoneyPoolRepository;

    @Autowired
    private AnaCallerService anaCallerService;

    @Override
    public RequestApproval save(EwalletParticipant ewalletParticipant, String ewpCompanyFormId, String ewpServiceId,String ewpServiceSettlementId, String ewpMoneyPoolId, String ewpGatewayConfigId,
                                String ewpStatusChangeId, Instance instance, ApprovalType approvalType,String requestRemark) {

        boolean allNull = Stream.of(ewpCompanyFormId, ewpServiceId,ewpServiceSettlementId, ewpMoneyPoolId, ewpGatewayConfigId, ewpStatusChangeId).allMatch(item->StringUtils.isBlank(item));
        if(null == ewalletParticipant || allNull){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewalletParticipant or allNull"});
        }

        /*if(CollectionUtils.isNotEmpty(ewalletParticipant.getRequestApproval())
                && ewalletParticipant.getRequestApproval().stream().anyMatch(
                		item -> item.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL) && item.getCurrentEnvir().equals(instance))){
            throw new BusinessException(SystemMsg.EwpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }*/

        RequestApproval requestApproval = new RequestApproval();
        requestApproval.setCreateDate(new Date());
        requestApproval.setCreateBy(userService.getLoginAccountId());
        requestApproval.setCurrentEnvir(instance);
        requestApproval.setEwalletParticipant(ewalletParticipant);
        requestApproval.setEwpCompanyFormId(ewpCompanyFormId);
        requestApproval.setEwpServiceId(ewpServiceId);
        requestApproval.setEwpServiceSettlementId(ewpServiceSettlementId);
        requestApproval.setEwpMoneyPoolId(ewpMoneyPoolId);
        requestApproval.setEwpGatewayConfigId(ewpGatewayConfigId);
        requestApproval.setEwpStatusChangeId(ewpStatusChangeId);
        requestApproval.setStatus(RequestApprovalStatus.PENDING_FOR_APPROVAL);
        requestApproval.setApprovalType(approvalType);
        requestApproval.setRequestRemark(requestRemark);

        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public RequestApproval approvalStatus(Long requestApprovalId,String requestUserId) {
        if (requestApprovalId == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(requestApprovalId);
        if(requestApproval==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        /*if(requestApproval.getStatus() != RequestApprovalStatus.PENDING_FOR_APPROVAL){
        	throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }*/

        String accountId = getRequestAccountId(requestUserId);
        Date updateDate = new Date();
        requestApproval.setStatus(RequestApprovalStatus.APPROVED);
        requestApproval.setUpdateBy(accountId);
        requestApproval.setUpdateDate(updateDate);
        requestApprovalRepository.save(requestApproval);

        EwalletParticipant ewalletParticipant = requestApproval.getEwalletParticipant();
        Instance instance = requestApproval.getCurrentEnvir();
        // First approval, put Participant State change to SUSPEND
        if(instance == Instance.PRE_PROD && ewalletParticipant.getPreStatus() == ParticipantStatus.PENDING_FOR_PROCESS){
        	ewalletParticipant.setPreStatus(ParticipantStatus.SUSPEND);
        }else if (instance == Instance.PROD && ewalletParticipant.getProdStatus() == ParticipantStatus.PENDING_FOR_PROCESS){
        	ewalletParticipant.setProdStatus(ParticipantStatus.SUSPEND);
        }
        ewalletParticipantRepository.save(ewalletParticipant);
        
        String ewpCompanyFormId = requestApproval.getEwpCompanyFormId();
        if (StringUtils.isNotBlank(ewpCompanyFormId)) {
            List<EwpCompanyForm> ewpCompanyForm =ewalletParticipant.getEwpCompanyForms().stream().filter(item->item.getStatus().equals(ParticipantStatus.ACTIVE) && item.getCurrentEnvir().equals(instance)).collect(Collectors.toList());
            if(ewpCompanyForm!=null && !ewpCompanyForm.isEmpty()){
                ewpCompanyForm.stream().forEach(item -> {
                    item.setStatus(ParticipantStatus.CLOSED);
                    item.setUpdateBy(accountId);
                    item.setUpdateDate(updateDate);
                });
                ewpCompanyFormRepository.save(ewpCompanyForm);
            }
            ewalletParticipantService.updateCompanyInfoStatus(ewpCompanyFormId, ParticipantStatus.ACTIVE);
        }
        String ewpServiceId = requestApproval.getEwpServiceId();
        if (StringUtils.isNotBlank(ewpServiceId)) {
            List<EwpService> ewpServiceList = ewalletParticipant.getEwpServices().stream().filter(item->item.getStatus().equals(ParticipantStatus.ACTIVE) && item.getCurrentEnvir().equals(instance)).collect(Collectors.toList());
            if(ewpServiceList!=null && !ewpServiceList.isEmpty()){
                ewpServiceList.stream().forEach(item->{
                    item.setStatus(ParticipantStatus.CLOSED);
                    item.setUpdateBy(accountId);
                    item.setUpdateDate(updateDate);
                });
                ewpServiceRepository.save(ewpServiceList);
            }
            List<EwpService> newServiceList = ewalletParticipantService.updateServiceSettingStatus(ewpServiceId, ParticipantStatus.ACTIVE);
            // Copy a new copy mp mapping  
            for(EwpService oldService : ewpServiceList){
            	EwpService newService = newServiceList.stream().filter(temp->temp.getBaseServiceId().equals(oldService.getBaseServiceId())).findFirst().orElse(null);
    			//mp
            	if(newService != null){
	    			for(EwpServiceMoneyPool oldSmp :oldService.getEwpMoneyPool()){
	    				if(oldSmp.getStatus() == ParticipantStatus.ACTIVE){
	    					EwpServiceMoneyPool newSmp = new EwpServiceMoneyPool();
	    					BeanUtils.copyProperties(oldSmp, newSmp,new String[]{"id","status","ewpService"});
	    					newSmp.setStatus(ParticipantStatus.ACTIVE);
	    					newSmp.setEwpService(newService);
	    					ewpServiceMoneyPoolRepository.save(newSmp);
	    				}
	    			}
            	}
            			
            }
            // Copy a new copy cutoff_time data 
            if(CollectionUtils.isNotEmpty(ewpServiceList)){
	            List<EwpServiceSettlement> oldSsList = ewpServiceSettlementRepository.findByEwpServiceId(ewpServiceList.get(0).getId());
	            if(CollectionUtils.isNotEmpty(oldSsList)){
	            	for(EwpService newService : newServiceList){
	            		EwpServiceSettlement newSs = new EwpServiceSettlement();
	            		BeanUtils.copyProperties(oldSsList.get(0), newSs,new String[]{"id","status","ewpServiceId"});
	            		newSs.setStatus(ParticipantStatus.ACTIVE);
	            		newSs.setEwpServiceId(newService.getId());
	            		ewpServiceSettlementRepository.save(newSs);
	            	}
	            }
            }
            // Turn off the old ones. service  Corresponding subdata  ->mp mapping   and cutoff_time
            for(EwpService oldService : ewpServiceList){
            	for(EwpServiceMoneyPool oldSmp :oldService.getEwpMoneyPool()){
            		oldSmp.setStatus(ParticipantStatus.CLOSED);
            		ewpServiceMoneyPoolRepository.save(oldSmp);
            	}
            	List<EwpServiceSettlement> oldSsList = ewpServiceSettlementRepository.findByEwpServiceId(oldService.getId());
            	for(EwpServiceSettlement oldSs : oldSsList){
            		oldSs.setStatus(ParticipantStatus.CLOSED);
            		ewpServiceSettlementRepository.save(oldSs);
            	}
            }
        }
        
        String ewpServiceSettlementId = requestApproval.getEwpServiceSettlementId();
        if(StringUtils.isNotBlank(ewpServiceSettlementId)){
        	List<EwpServiceSettlement> essList = ewpServiceSettlementRepository.findByParticipantId(ewalletParticipant.getId());
        	for(EwpServiceSettlement ess : essList){
        		if(ess.getStatus()==ParticipantStatus.ACTIVE && ess.getCurrentEnvir()==instance){
	        		ess.setStatus(ParticipantStatus.CLOSED);
	        		ess.setUpdateBy(accountId);
	        		ess.setUpdateDate(updateDate);
	        		ewpServiceSettlementRepository.save(ess);
        		}
        	}
        	ewalletParticipantService.updateServiceSettlementStatus(ewpServiceSettlementId, ParticipantStatus.ACTIVE);
        }
        
        String ewpMoneyPoolId = requestApproval.getEwpMoneyPoolId();
        if (StringUtils.isNotBlank(ewpMoneyPoolId)) {
            List<EwpServiceMoneyPool> serviceMoneyPool = ewalletParticipant.getEwpServiceMoneyPool().stream().filter(item->item.getStatus().equals(ParticipantStatus.ACTIVE) && item.getCurrentEnvir().equals(instance)).collect(Collectors.toList());
            if(serviceMoneyPool!=null && !serviceMoneyPool.isEmpty()){
                serviceMoneyPool.stream().forEach(item->{
                    item.setStatus(ParticipantStatus.CLOSED);
                    item.setUpdateBy(accountId);
                    item.setUpdateDate(updateDate);
                });
                ewpMoneyPoolRepository.save(serviceMoneyPool);
            }
            ewalletParticipantService.updateServiceAssignmentStatus(ewpMoneyPoolId, ParticipantStatus.ACTIVE);
        }
        String ewpGatewayConfigId = requestApproval.getEwpGatewayConfigId();
        if (StringUtils.isNotBlank(ewpGatewayConfigId)) {
            List<EwpGatewayConfig> ewpGatewayConfig = ewalletParticipant.getEwpGatewayConfigs().stream().filter(item->item.getStatus().equals(ParticipantStatus.ACTIVE) && item.getCurrentEnvir().equals(instance)).collect(Collectors.toList());
            if(ewpGatewayConfig!=null && !ewpGatewayConfig.isEmpty()){
                ewpGatewayConfig.stream().forEach(item->{
                    item.setStatus(ParticipantStatus.CLOSED);
                    item.setUpdateBy(accountId);
                    item.setUpdateDate(updateDate);
                });
                ewpGatewayConfigRepository.save(ewpGatewayConfig);
            }
            ewalletParticipantService.updateApiGatewaySettingStatus(ewpGatewayConfigId,ParticipantStatus.ACTIVE);
        }
        String ewpStatusChangeId = requestApproval.getEwpStatusChangeId();
        if(StringUtils.isNotBlank(ewpStatusChangeId)){
            List<EwpStatusChange> ewpStatusChange = ewalletParticipant.getEwpStatusChanges().stream().filter(item->item.getStatus().equals(ParticipantStatus.ACTIVE) && item.getCurrentEnvir().equals(instance)).collect(Collectors.toList());
            if(ewpStatusChange != null && !ewpStatusChange.isEmpty()){
                ewpStatusChange.stream().forEach(item->{
                    item.setStatus(ParticipantStatus.CLOSED);
                    item.setUpdateBy(accountId);
                    item.setUpdateDate(updateDate);
                });
                ewpStatusChangeRepository.save(ewpStatusChange);
            }
            List<EwpStatusChange> escList = ewalletParticipantService.updateStatusChangeStatus(ewpStatusChangeId, ParticipantStatus.ACTIVE);
            // Amendment after approval Participant state 
            if(instance == Instance.PRE_PROD){
            	ewalletParticipant.setPreStatus(ParticipantStatus.findByValue(escList.get(0).getToStatus()));
            }else if (instance == Instance.PROD){
            	ewalletParticipant.setProdStatus(ParticipantStatus.findByValue(escList.get(0).getToStatus()));
            }
            ewalletParticipant.setUpdateDate(new Date());
            ewalletParticipant.setUpdateBy(accountId);
            ewalletParticipantRepository.save(ewalletParticipant);
        }

        if (StringUtils.isBlank(ewpCompanyFormId) && StringUtils.isBlank(ewpGatewayConfigId)
                && StringUtils.isBlank(ewpMoneyPoolId) && StringUtils.isBlank(ewpServiceId)
                && StringUtils.isBlank(ewpStatusChangeId) &&StringUtils.isBlank(ewpServiceSettlementId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpCompanyFormId and ewpServiceId and ewpServiceSettlementId"});
        }

        return requestApproval;
    }


    @Override
    public List<RequestApprovalDto> getRequestApproval(String instance) {
        List<RequestApproval> list = requestApprovalRepository.findRequestApproval(RequestApprovalStatus.PENDING_FOR_APPROVAL,Instance.valueOf(instance));
        List<RequestApprovalDto> participantApprovalDtoList = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return participantApprovalDtoList;
        }
        list.forEach(item -> {
        	RequestApprovalDto dto = new RequestApprovalDto();
            BeanUtils.copyProperties(item, dto);
            dto.setApprovalType(item.getApprovalType());
            dto.setCurrentEnvir(item.getCurrentEnvir());
            dto.setId(item.getId().toString());
            dto.setStatus(item.getStatus());
            dto.setParticipantId(item.getEwalletParticipant().getId().toString());
            dto.setGeaParticipantRefId(item.getEwalletParticipant().getGeaRefId());
            dto.setCreateBy(item.getCreateBy());
            dto.setCreateUserName(accountService.getAccountName(item.getCreateBy()));
            dto.setCreateDate(item.getCreateDate());
            dto.setParticipantName(ewalletParticipantService.getParticipantName(item.getEwalletParticipant().getGeaRefId(), Instance.valueOf(instance)));
            dto.setRequestRemark(item.getRequestRemark());
            
            participantApprovalDtoList.add(dto);
        });
        return participantApprovalDtoList;
    }


    @Override
    public RequestApproval rejectApproval(Long requestApprovalId, ParticipantStatus status,String requestUserId) {
        if (requestApprovalId == null || status == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId or status"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(requestApprovalId);
        if(requestApproval==null){
            throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        /*if(!requestApproval.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL)){
            throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }*/

        requestApproval.setStatus(RequestApprovalStatus.REJECT);
        requestApproval.setUpdateBy(getRequestAccountId(requestUserId));
        requestApproval.setUpdateDate(new Date());
        //首次reject 主数据也reject掉
        if(requestApproval.getApprovalType() == ApprovalType.CREATE_PARTICIPANT && requestApproval.getCurrentEnvir()==Instance.PRE_PROD){
        	requestApproval.getEwalletParticipant().setPreStatus(ParticipantStatus.REJECTED);
        }
        requestApprovalRepository.save(requestApproval);

        String ewpCompanyFormId = requestApproval.getEwpCompanyFormId();
        if (StringUtils.isNotBlank(ewpCompanyFormId)) {
            ewalletParticipantService.updateCompanyInfoStatus(ewpCompanyFormId, status);
        }
        String ewpServiceId = requestApproval.getEwpServiceId();
        if (StringUtils.isNotBlank(ewpServiceId)) {
            ewalletParticipantService.updateServiceSettingStatus(ewpServiceId, status);
        }
        String ewpServiceSettlementId = requestApproval.getEwpServiceSettlementId();
        if(StringUtils.isNotBlank(ewpServiceSettlementId)){
        	ewalletParticipantService.updateServiceSettlementStatus(ewpServiceSettlementId, status);
        	
        }
        String ewpMoneyPoolId = requestApproval.getEwpMoneyPoolId();
        if (StringUtils.isNotBlank(ewpMoneyPoolId)) {
            ewalletParticipantService.updateServiceAssignmentStatus(ewpMoneyPoolId, status);
        }
        String ewpGatewayConfigId = requestApproval.getEwpGatewayConfigId();
        if (StringUtils.isNotBlank(ewpGatewayConfigId)) {
            ewalletParticipantService.updateApiGatewaySettingStatus(ewpGatewayConfigId,status);
        }
        String ewpStatusChangeId = requestApproval.getEwpStatusChangeId();
        if(StringUtils.isNotBlank(ewpStatusChangeId)){
            ewalletParticipantService.updateStatusChangeStatus(ewpStatusChangeId, status);
        }

        return requestApproval;
    }

    /**
     * find By ParticipantId
     */
	@Override
	public List<RequestApproval> findByParticipantId(Long participantId) {
		return requestApprovalRepository.findByParticipantId(participantId);
	}

    @Override
    public String getRequestApprovalInfo(String ewpApvReqId) {
        long ewpApvReqID = Long.parseLong(ewpApvReqId);
        RequestApproval requestApproval = requestApprovalRepository.findOne(ewpApvReqID);
        return requestApproval.getApprovalType().getValue();
    }

    @Override
    public RequestApproval getApproval(Long requestApprovalId) {
        if (requestApprovalId == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        return requestApprovalRepository.findOne(requestApprovalId);
    }

    private String getRequestAccountId(String requestUserId) {
        return anaCallerService.callFindBindingId(requestUserId, ApplicationContext.Modules.APV, ApplicationContext.Modules.EWP).getData();
    }

}
