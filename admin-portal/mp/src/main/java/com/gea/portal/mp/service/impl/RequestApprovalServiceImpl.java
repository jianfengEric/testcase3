package com.gea.portal.mp.service.impl;

import com.gea.portal.mp.entity.*;
import com.gea.portal.mp.repository.*;
import com.gea.portal.mp.service.AnaCallerService;
import com.gea.portal.mp.service.EwpCallerService;
import com.gea.portal.mp.service.RequestApprovalService;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
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
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Jimmy on 2018/9/3.
 */
@Service
@Transactional
public class RequestApprovalServiceImpl implements RequestApprovalService {
    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private RequestApprovalRepository requestApprovalRepository;
    @Autowired
    private EwpMoneyPoolRepository ewpMoneyPoolRepository;
    @Autowired
    private EwpPoolAdjustmentRepository ewpPoolAdjustmentRepository;
    @Autowired
    private EwpPoolDepositCashOutRepository ewpPoolDepositCashOutRepository;
    @Autowired
    private EwpMpChangeReqRepository ewpMpChangeReqRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private EwpCallerService ewpCallerService;
    @Autowired
    private AnaCallerService anaCallerService;


    @Override
    public RequestApproval saveMoneyPoolRequestApproval(String geaParticipantRefId, EwpMoneyPool ewpMoneyPool, Instance instance,String requestRemark) {
        if(hasPending(ewpMoneyPool, instance)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RequestApproval requestApproval = getRequestApproval(geaParticipantRefId, instance);
        requestApproval.setApprovalType(ApprovalType.CREATE_INDIVIDUAL_MONEY_POOL);
        requestApproval.setEwpMoneyPool(ewpMoneyPool);
        requestApproval.setRequestRemark(requestRemark);
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public RequestApproval savePoolAdjustmentRequestApproval(String geaParticipantRefId, EwpPoolAdjustment ewpPoolAdjustment, Instance instance,String requestRemark) {
        if(hasPending(ewpPoolAdjustment.getEwpMoneyPool(), instance)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RequestApproval requestApproval = getRequestApproval(geaParticipantRefId, instance);
        requestApproval.setApprovalType(ApprovalType.ADJUSTMENT);
        requestApproval.setEwpPoolAdjustment(ewpPoolAdjustment);
        requestApproval.setEwpMoneyPool(ewpPoolAdjustment.getEwpMoneyPool());
        requestApproval.setRequestRemark(requestRemark);
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public RequestApproval savePoolDepositCashOutRequestApproval(String geaParticipantRefId, EwpPoolDepositCashOut ewpPoolDepositCashOut, Instance instance,String requestRemark) {
        if(hasPending(ewpPoolDepositCashOut.getEwpMoneyPool(), instance)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RequestApproval requestApproval = getRequestApproval(geaParticipantRefId, instance);
        if(ewpPoolDepositCashOut.getType()==0){
            requestApproval.setApprovalType(ApprovalType.CASH_OUT);
        }else{
            requestApproval.setApprovalType(ApprovalType.DEPOSIT);
        }
        requestApproval.setEwpPoolDepositCashOut(ewpPoolDepositCashOut);
        requestApproval.setEwpMoneyPool(ewpPoolDepositCashOut.getEwpMoneyPool());
        requestApproval.setRequestRemark(requestRemark);
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public RequestApproval saveMpChangeRequestApproval(String geaParticipantRefId, EwpMpChangeReq ewpMpChangeReq, Instance instance,String requestRemark) {
        if(hasPending(ewpMpChangeReq.getEwpMoneyPool(), instance)){
            throw new BusinessException(SystemMsg.MpErrorMsg.HAS_PENDING_FOR_APPROVAL.getErrorCode());
        }
        RequestApproval requestApproval = getRequestApproval(geaParticipantRefId, instance);
        requestApproval.setApprovalType(ApprovalType.MONEY_POOL_STATUS_ALERT_LINE);
        requestApproval.setEwpMpChangeReq(ewpMpChangeReq);
        requestApproval.setEwpMoneyPool(ewpMpChangeReq.getEwpMoneyPool());
        requestApproval.setRequestRemark(requestRemark);
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    private RequestApproval getRequestApproval(String geaParticipantRefId, Instance instance) {
        RequestApproval requestApproval = new RequestApproval();
        requestApproval.setGeaParticipantRefId(geaParticipantRefId);
        requestApproval.setStatus(RequestApprovalStatus.PENDING_FOR_APPROVAL);
        requestApproval.setCurrentEnvir(instance);
        requestApproval.setCreateBy(userService.getLoginAccountId());
        requestApproval.setCreateDate(new Date());
        return requestApproval;
    }

    @Override
    public List<RequestApprovalDto> getRequestApproval(Instance instance) {
        List<RequestApproval> page = requestApprovalRepository.findRequestApproval(RequestApprovalStatus.PENDING_FOR_APPROVAL, instance);
        List<RequestApprovalDto> participantApprovalDtoList = new ArrayList<>();
        page.stream().forEach(item -> {
        	RequestApprovalDto dto = new RequestApprovalDto();
            BeanUtils.copyProperties(item, dto);
	        dto.setApprovalType(item.getApprovalType());
	        dto.setCurrentEnvir(item.getCurrentEnvir());
	        dto.setId(item.getId().toString());
	        dto.setStatus(item.getStatus()	);
	        dto.setGeaParticipantRefId(item.getGeaParticipantRefId());
	        dto.setCreateBy(item.getCreateBy());
	        dto.setCreateUserName(accountService.getAccountName(item.getCreateBy()));
	        dto.setCreateDate(item.getCreateDate());
	        if (null != item.getEwpMoneyPool()) {
	            dto.setGeaMoneyPoolRefId(item.getEwpMoneyPool().getGeaMoneyPoolRefId());
	        } else if (null != item.getEwpMpChangeReq()) {
	            dto.setGeaMoneyPoolRefId(item.getEwpMpChangeReq().getEwpMoneyPool().getGeaMoneyPoolRefId());
	        } else if (null != item.getEwpPoolAdjustment()) {
	            dto.setGeaMoneyPoolRefId(item.getEwpPoolAdjustment().getEwpMoneyPool().getGeaMoneyPoolRefId());
	        } else if (null != item.getEwpPoolDepositCashOut()) {
	            dto.setGeaMoneyPoolRefId(item.getEwpPoolDepositCashOut().getEwpMoneyPool().getGeaMoneyPoolRefId());
	        }
	        dto.setRequestRemark(item.getRequestRemark());
	        
	        participantApprovalDtoList.add(dto);
	    });
        // ParticipantName
        List<String> geaRefId = participantApprovalDtoList.stream().map(item -> item.getGeaParticipantRefId()).collect(Collectors.toList());
        Map<String, String> map = ewpCallerService.callGetParticipantName(geaRefId, instance).getData();
        for(RequestApprovalDto item : participantApprovalDtoList){
        	item.setParticipantName(map.get(item.getGeaParticipantRefId()));
        }
        return participantApprovalDtoList;
    }


    @Override
    public RequestApproval approval(String requestApprovalId,String requestUserId) {
        if (StringUtils.isBlank(requestApprovalId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        if (requestApproval == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApproval"});
        }
        if(requestApproval.getStatus() != RequestApprovalStatus.PENDING_FOR_APPROVAL){
        	throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }
        String loginAccountId = getRequestAccountId(requestUserId);
        Date updateDate = new Date();
        requestApproval.setStatus(RequestApprovalStatus.APPROVED);
        requestApproval.setUpdateBy(loginAccountId);
        requestApproval.setUpdateDate(updateDate);
        requestApprovalRepository.save(requestApproval);
        ApprovalType approvalType = requestApproval.getApprovalType();
        Instance instance = requestApproval.getCurrentEnvir();
        switch (approvalType) {
            case CREATE_INDIVIDUAL_MONEY_POOL:
                approvalMoneyPool(requestApproval.getEwpMoneyPool().getId(), loginAccountId, updateDate);
                break;
            case ADJUSTMENT:
                approvalAdjustment(requestApproval.getEwpPoolAdjustment().getId(), loginAccountId, updateDate, instance);
                break;
            case DEPOSIT: case CASH_OUT:
                approvalDeposit(requestApproval.getEwpPoolDepositCashOut().getId(), loginAccountId, updateDate, instance);
                break;
            case MONEY_POOL_STATUS_ALERT_LINE:
                approvalMpChangeReq(requestApproval.getEwpMpChangeReq().getId(), loginAccountId, updateDate, instance);
                break;
            default:
                throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        return requestApproval;
    }

    private void approvalMoneyPool(Long id, String loginAccountId, Date updateDate) {
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(id);
        if (ewpMoneyPool == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpMoneyPool"});
        }
        ewpMoneyPool.setStatus(MoneyPoolStatus.SUSPEND);
        ewpMoneyPool.setUpdateBy(loginAccountId);
        ewpMoneyPool.setUpdateDate(updateDate);
        ewpMoneyPoolRepository.save(ewpMoneyPool);
    }

    private void approvalAdjustment(Long id, String loginAccountId, Date updateDate, Instance instance) {
        EwpPoolAdjustment ewpPoolAdjustment = ewpPoolAdjustmentRepository.findOne(id);
        if (ewpPoolAdjustment == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpPoolAdjustment"});
        }
        List<EwpPoolAdjustment> ewpPoolAdjustments = ewpPoolAdjustmentRepository.findByStatusAndCurrentEnvirAndEwpMoneyPool(MoneyPoolStatus.ACTIVE, instance, ewpPoolAdjustment.getEwpMoneyPool());
        if (!ewpPoolAdjustments.isEmpty()) {
            ewpPoolAdjustments.stream().forEach(item -> {
                item.setStatus(MoneyPoolStatus.CLOSED);
                item.setUpdateBy(loginAccountId);
                item.setUpdateDate(updateDate);
            });
            ewpPoolAdjustmentRepository.save(ewpPoolAdjustments);
        }
        ewpPoolAdjustment.setStatus(MoneyPoolStatus.ACTIVE);
        ewpPoolAdjustment.setUpdateBy(loginAccountId);
        ewpPoolAdjustment.setUpdateDate(updateDate);
        ewpPoolAdjustmentRepository.save(ewpPoolAdjustment);
    }

    private void approvalDeposit(Long id, String loginAccountId, Date updateDate, Instance instance) {
        EwpPoolDepositCashOut ewpPoolDepositCashOut = ewpPoolDepositCashOutRepository.findOne(id);
        if (ewpPoolDepositCashOut == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"ewpPoolDepositCashOut"});
        }
        List<EwpPoolDepositCashOut> ewpPoolDepositCashOuts = ewpPoolDepositCashOutRepository.findByStatusAndCurrentEnvirAndEwpMoneyPool(MoneyPoolStatus.ACTIVE, instance, ewpPoolDepositCashOut.getEwpMoneyPool());
        if (!ewpPoolDepositCashOuts.isEmpty()) {
            ewpPoolDepositCashOuts.stream().forEach(item -> {
                item.setStatus(MoneyPoolStatus.CLOSED);
                item.setUpdateBy(loginAccountId);
                item.setUpdateDate(updateDate);
            });
            ewpPoolDepositCashOutRepository.save(ewpPoolDepositCashOuts);
        }
        ewpPoolDepositCashOut.setStatus(MoneyPoolStatus.ACTIVE);
        ewpPoolDepositCashOut.setUpdateBy(loginAccountId);
        ewpPoolDepositCashOut.setUpdateDate(updateDate);
        ewpPoolDepositCashOutRepository.save(ewpPoolDepositCashOut);
    }

    private void approvalMpChangeReq(Long id, String loginAccountId, Date updateDate, Instance instance) {
        EwpMpChangeReq ewpMpChangeReq = ewpMpChangeReqRepository.findOne(id);
        if (ewpMpChangeReq == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpMpChangeReq"});
        }
        EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(ewpMpChangeReq.getEwpMoneyPool().getId());
        ewpMoneyPool.setAlertLine(ewpMpChangeReq.getToAlertLine());
        if("PENDING_FOR_PROCESS".equals(ewpMpChangeReq.getToStatus())){
            ewpMoneyPool.setStatus(MoneyPoolStatus.PENDING_FOR_PROCESS);
        }else if("SUSPEND".equals(ewpMpChangeReq.getToStatus())){
            ewpMoneyPool.setStatus(MoneyPoolStatus.SUSPEND);
        }else if("DORMANT".equals(ewpMpChangeReq.getToStatus())){
            ewpMoneyPool.setStatus(MoneyPoolStatus.DORMANT);
        }else if("ACTIVE".equals(ewpMpChangeReq.getToStatus())){
            ewpMoneyPool.setStatus(MoneyPoolStatus.ACTIVE);
        }else{
            ewpMoneyPool.setStatus(MoneyPoolStatus.CLOSED);
        }

        List<EwpMpChangeReq> ewpMpChangeReqs = ewpMpChangeReqRepository.findByStatusAndCurrentEnvirAndEwpMoneyPool(MoneyPoolStatus.ACTIVE, instance, ewpMpChangeReq.getEwpMoneyPool());
        if (!ewpMpChangeReqs.isEmpty()) {
            ewpMpChangeReqs.stream().forEach(item -> {
                item.setStatus(MoneyPoolStatus.CLOSED);
                item.setUpdateBy(loginAccountId);
                item.setUpdateDate(updateDate);
            });
            ewpMpChangeReqRepository.save(ewpMpChangeReqs);
        }
        ewpMpChangeReq.setStatus(MoneyPoolStatus.ACTIVE);
        ewpMpChangeReq.setUpdateBy(loginAccountId);
        ewpMpChangeReq.setUpdateDate(updateDate);
        ewpMpChangeReqRepository.save(ewpMpChangeReq);
        ewpMoneyPoolRepository.save(ewpMoneyPool);
    }

    @Override
    public RequestApproval reject(String requestApprovalId,String requestUserId) {
        if (StringUtils.isBlank(requestApprovalId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        if (requestApproval == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApproval"});
        }
        if(!requestApproval.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL)){
            throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }
        String loginAccountId = getRequestAccountId(requestUserId);
        Date updateDate = new Date();
        requestApproval.setStatus(RequestApprovalStatus.REJECT);
        requestApproval.setUpdateBy(loginAccountId);
        requestApproval.setUpdateDate(updateDate);
        requestApprovalRepository.save(requestApproval);

        ApprovalType approvalType = requestApproval.getApprovalType();
        switch (approvalType) {
            case CREATE_INDIVIDUAL_MONEY_POOL:
                EwpMoneyPool ewpMoneyPool = ewpMoneyPoolRepository.findOne(requestApproval.getEwpMoneyPool().getId());
                if (ewpMoneyPool == null) {
                    throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpMoneyPool"});
                }
                ewpMoneyPool.setStatus(MoneyPoolStatus.REJECTED);
                ewpMoneyPool.setUpdateBy(loginAccountId);
                ewpMoneyPool.setUpdateDate(updateDate);
                ewpMoneyPoolRepository.save(ewpMoneyPool);
                break;
            case ADJUSTMENT:
                EwpPoolAdjustment ewpPoolAdjustment = ewpPoolAdjustmentRepository.findOne(requestApproval.getEwpPoolAdjustment().getId());
                if (ewpPoolAdjustment == null) {
                    throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpPoolAdjustment"});
                }
                ewpPoolAdjustment.setStatus(MoneyPoolStatus.REJECTED);
                ewpPoolAdjustment.setUpdateBy(loginAccountId);
                ewpPoolAdjustment.setUpdateDate(updateDate);
                ewpPoolAdjustmentRepository.save(ewpPoolAdjustment);
                break;
            case DEPOSIT: case CASH_OUT:
                EwpPoolDepositCashOut ewpPoolDepositCashOut = ewpPoolDepositCashOutRepository.findOne(requestApproval.getEwpPoolDepositCashOut().getId());
                if (ewpPoolDepositCashOut == null) {
                    throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpPoolDepositCashOut"});
                }
                ewpPoolDepositCashOut.setStatus(MoneyPoolStatus.REJECTED);
                ewpPoolDepositCashOut.setUpdateBy(loginAccountId);
                ewpPoolDepositCashOut.setUpdateDate(updateDate);
                ewpPoolDepositCashOutRepository.save(ewpPoolDepositCashOut);
                break;
            case MONEY_POOL_STATUS_ALERT_LINE:
                EwpMpChangeReq ewpMpChangeReq = ewpMpChangeReqRepository.findOne(requestApproval.getEwpMpChangeReq().getId());
                if (ewpMpChangeReq == null) {
                    throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"ewpMpChangeReq"});
                }
                ewpMpChangeReq.setStatus(MoneyPoolStatus.REJECTED);
                ewpMpChangeReq.setUpdateBy(loginAccountId);
                ewpMpChangeReq.setUpdateDate(updateDate);
                ewpMpChangeReqRepository.save(ewpMpChangeReq);
                break;
            default:
                throw new BusinessException(SystemMsg.ServerErrorMsg.SERVER_ERROR.getErrorCode());
        }
        return requestApproval;
    }

    @Override
    public String getRequestApprovalInfo(String mpApvReqId) {
        long mpApvReqID = Long.parseLong(mpApvReqId);
        RequestApproval requestApproval = requestApprovalRepository.findOne(mpApvReqID);
        return requestApproval.getApprovalType().getValue();
    }

    @Override
    public RequestApprovalDto getApproval(String requestApprovalId) {
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        RequestApprovalDto dto = new RequestApprovalDto();
        dto.setGeaParticipantRefId(requestApproval.getGeaParticipantRefId());
        dto.setEwpMoneyPoolId(requestApproval.getEwpMoneyPool().getId().toString());
        dto.setEwpMpChangeReqId(requestApproval.getEwpMpChangeReq()==null?"":requestApproval.getEwpMpChangeReq().getId().toString());
        dto.setEwpPoolAdjustId(requestApproval.getEwpPoolAdjustment()==null?"":requestApproval.getEwpPoolAdjustment().getId().toString());
        dto.setEwpDepositCashoutId(requestApproval.getEwpPoolDepositCashOut()==null?"":requestApproval.getEwpPoolDepositCashOut().getId().toString());
        dto.setApprovalType(requestApproval.getApprovalType());
        dto.setStatus(requestApproval.getStatus());
        dto.setStatusReason(requestApproval.getStatusReason());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setGeaMoneyPoolRefId(requestApproval.getEwpMoneyPool().getGeaMoneyPoolRefId());
        return dto;
    }

    /**
     *  Is there a need for approval? 
     */
    private boolean hasPending(EwpMoneyPool ewpMoneyPool, Instance instance){
        if(CollectionUtils.isNotEmpty(ewpMoneyPool.getRequestApprovals())
                && ewpMoneyPool.getRequestApprovals().stream().anyMatch(item -> item.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL) && instance.equals(item.getCurrentEnvir()))){
            return true;
        }
        return false;
    }

    private String getRequestAccountId(String requestUserId) {
        return anaCallerService.callFindBindingId(requestUserId, ApplicationContext.Modules.APV, ApplicationContext.Modules.MP).getData();
    }
}
