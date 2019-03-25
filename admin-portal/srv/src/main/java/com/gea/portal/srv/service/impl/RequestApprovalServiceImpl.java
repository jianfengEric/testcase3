package com.gea.portal.srv.service.impl;

import com.gea.portal.srv.entity.*;
import com.gea.portal.srv.repository.RequestApprovalRepository;
import com.gea.portal.srv.repository.ServiceChangeDetailRepository;
import com.gea.portal.srv.repository.ServiceConfigRepository;
import com.gea.portal.srv.repository.ServiceDeploymentRepository;
import com.gea.portal.srv.service.AnaCallerService;
import com.gea.portal.srv.service.RequestApprovalService;
import com.gea.portal.srv.service.SrvBaseService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.ApplicationContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class RequestApprovalServiceImpl implements RequestApprovalService{

    @Autowired
    private RequestApprovalRepository requestApprovalRepository;

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ServiceDeploymentRepository serviceDeploymentRepository;

    @Autowired
    private ServiceChangeDetailRepository serviceChangeDetailRepository;

    @Autowired
    private ServiceConfigRepository serviceConfigRepository;
    
    @Autowired
    private SrvBaseService srvBaseService;

    @Autowired
    private AnaCallerService anaCallerService;
    private String requestApprovalId="requestApprovalId";

    @Override
    public void saveServiceChangeRequestApproval(ServiceBatchChgReq serviceBatchChgReq, Instance instance,String requestRemark) {
        if(null == serviceBatchChgReq || null == instance){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"serviceBatchChgReq or instance"});
        }
        RequestApproval requestApproval=new RequestApproval();
        requestApproval.setServiceBatchChgReq(serviceBatchChgReq);
        requestApproval.setCurrentEnvir(instance);
        requestApproval.setStatus(RequestApprovalStatus.PENDING_FOR_APPROVAL);
        requestApproval.setApprovalType(ApprovalType.SERVICE_MARKUP.getValue());
        requestApproval.setCreateBy(userService.getLoginAccountId());
        requestApproval.setCreateDate(new Date());
        requestApproval.setRequestRemark(requestRemark);
        requestApprovalRepository.save(requestApproval);
    }

    @Override
    public RequestApproval approval(String requestApprovalId,String requestUserId) {
        if (requestApprovalId == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{requestApprovalId});
        }
        RequestApproval requestApproval =requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        if (requestApproval == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApproval"});
        }
        if(requestApproval.getStatus() != RequestApprovalStatus.PENDING_FOR_APPROVAL){
        	throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }
        requestApproval.setStatus(RequestApprovalStatus.APPROVED);
        requestApproval.setUpdateBy(getRequestAccountId(requestUserId));
        requestApproval.setUpdateDate(new Date());
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public RequestApproval reject(String requestApprovalId,String requestUserId) {
        if (requestApprovalId == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{requestApprovalId});
        }
        RequestApproval requestApproval =requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        if (requestApproval == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApproval"});
        }
        if(requestApproval.getStatus() != RequestApprovalStatus.PENDING_FOR_APPROVAL){
        	throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }
        requestApproval.setStatus(RequestApprovalStatus.REJECT);
        requestApproval.setUpdateBy(getRequestAccountId(requestUserId));
        requestApproval.setUpdateDate(new Date());
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public ServiceBatchChgReqDto getRequestApproval(String instance) {
        if (StringUtils.isBlank(instance)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"instance"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findRequestApproval(RequestApprovalStatus.PENDING_FOR_APPROVAL, Instance.valueOf(instance));
        if(requestApproval == null){
        	return null;
        }
        ServiceBatchChgReqDto serviceBatchChgReqDto = srvBaseService.getDetail(requestApproval.getServiceBatchChgReq().getId());
        serviceBatchChgReqDto.setRequestApprovalId(requestApproval.getId());
        return serviceBatchChgReqDto;
    }

    @Override
    public Boolean hasPendingStatus(Instance instance) {
        final Boolean[] status = new Boolean[1];
        List<RequestApproval> requestApprovals=requestApprovalRepository.findByCurrentEnvir(instance);
        if(requestApprovals.isEmpty()){
            return false;
        }
        requestApprovals.stream().findFirst().ifPresent(item -> {
            if(item.getCurrentEnvir()==instance && item.getStatus()==RequestApprovalStatus.PENDING_FOR_APPROVAL){
                status[0] =true;
            }else {
                status[0] =false;
            }
        });
        return status[0];
    }

    @Override
    public ServiceDeployment saveDeployment(Long requestApprovalId) {
        if(null == requestApprovalId){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(requestApprovalId);
        if(null == requestApproval){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"RequestApproval"});
        }
        Date createDate = new Date();
        ServiceDeployment serviceDeployment = new ServiceDeployment();
        serviceDeployment.setCreateBy(userService.getLoginAccountId());
        serviceDeployment.setCreateDate(createDate);
        serviceDeployment.setStatus(DeployStatus.PENDING_FOR_DEPLOY);
        serviceDeployment.setRequestApproval(requestApproval);
        serviceDeployment.setDeployEnvir(requestApproval.getCurrentEnvir().toString());
        serviceDeploymentRepository.save(serviceDeployment);
        return serviceDeployment;
    }

    @Override
    public void synchDeployment(Long deployRefId, DeployStatus deployStatus,Date scheduleDeployDate,String deployVersionNo) {
        String loginAccountId=userService.getLoginAccountId();
        Date updateDate=new Date();
        ServiceDeployment serviceDeployment = serviceDeploymentRepository.findOne(deployRefId);
        serviceDeployment.setStatus(deployStatus);
        serviceDeployment.setUpdateDate(updateDate);
        serviceDeployment.setUpdateBy(loginAccountId);
        serviceDeployment.setScheduleDeployDate(scheduleDeployDate);
        serviceDeployment.setDeployVersionNo(deployVersionNo);
        serviceDeploymentRepository.save(serviceDeployment);
        //synchor service markup
        if(deployStatus.equals(DeployStatus.DEPLOYED)){
            ServiceBatchChgReq serviceBatchChgReq = serviceDeployment.getRequestApproval().getServiceBatchChgReq();
            List<ServiceChangeDetail> serviceBatchs=serviceChangeDetailRepository.findByServiceBatchChgReq(serviceBatchChgReq);
            serviceBatchs.stream().forEach(item -> {
                ServiceConfig serviceConfig=serviceConfigRepository.findByBaseService(item.getBaseService(),Instance.valueOf(serviceDeployment.getDeployEnvir()));
                serviceConfig.setMarkup(item.getToMarkUp());
                serviceConfig.setUpdateBy(loginAccountId);
                serviceConfig.setUpdateDate(updateDate);
                BaseService baseService=serviceConfig.getBaseService();
                baseService.setUpdateBy(loginAccountId);
                baseService.setUpdateDate(updateDate);
                serviceConfigRepository.save(serviceConfig);
            });
        }
    }

    @Override
    public RequestApprovalDto getApproval(String requestApprovalId) {
        if(StringUtils.isBlank(requestApprovalId)){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{requestApprovalId});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        RequestApprovalDto dto = new RequestApprovalDto();
        if(Objects.nonNull(requestApproval)){
            dto.setBatchId(requestApproval.getServiceBatchChgReq().getId().toString());
            dto.setApprovalType(ApprovalType.valueOf(requestApproval.getApprovalType()));
            dto.setStatus(requestApproval.getStatus());
            dto.setStatusReason(requestApproval.getStatusReason());
            dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        }
        return dto;
    }

    @Override
    public RequestApproval findByCurrentEnvir(String instance) {
        if (StringUtils.isBlank(instance)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"instance"});
        }
        return requestApprovalRepository.findRequestApproval(RequestApprovalStatus.PENDING_FOR_APPROVAL, Instance.valueOf(instance));
    }

    private String getRequestAccountId(String requestUserId) {
        return anaCallerService.callFindBindingId(requestUserId, ApplicationContext.Modules.APV, ApplicationContext.Modules.SRV).getData();
    }
}
