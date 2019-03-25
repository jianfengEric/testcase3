package com.gea.portal.srv.soa;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gea.portal.srv.entity.RequestApproval;
import com.gea.portal.srv.entity.ServiceDeployment;
import com.gea.portal.srv.repository.RequestApprovalRepository;
import com.gea.portal.srv.service.RequestApprovalService;
import com.gea.portal.srv.service.SrvBaseService;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.soa.AbstractMQBasicService;
import com.tng.portal.common.soa.AbstractSOABasicService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Eric on 2018/11/29.
 */
@Service("srvSOAService")
@Transactional
public class SrvSOAService extends AbstractMQBasicService {
    private Logger log = LoggerFactory.getLogger(AbstractSOABasicService.class);

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SrvBaseService srvBaseService;

    @Autowired
    private RequestApprovalRepository requestApprovalRepository;

    @PostConstruct
    public void initConnectToMQ(){
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            log.info("SrvSOAService connectToMQ start!!");
            startListening();
            log.info("SrvSOAService connectToMQ end!!");
        }else{
            log.info("SrvSOAService will not run!!");
        }

    }

    @Override
    public String getServiceName() {
        return MessageFormat.format(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceNameTemplate), PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName));
    }

    @Override
    public Object getHandleInstance() {
        return this;
    }

    public RestfulResponse<ServiceBatchChgReqDto> getDetail(Long batchId) {
        ServiceBatchChgReqDto serviceBatchChgReqDto = srvBaseService.getDetail(batchId);
        return RestfulResponse.ofData(serviceBatchChgReqDto);
    }

    public RestfulResponse<RequestApprovalDto> approval(String requestApprovalId,String requestUserId) {
        RequestApproval requestApproval =requestApprovalService.approval(requestApprovalId,requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setId(requestApproval.getId().toString());
        dto.setBatchId(requestApproval.getServiceBatchChgReq().getId().toString());
        dto.setApprovalType(ApprovalType.valueOf(requestApproval.getApprovalType()));
        dto.setStatus(requestApproval.getStatus());
        dto.setStatusReason(requestApproval.getStatusReason());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        return RestfulResponse.ofData(dto);
    }

    public RestfulResponse<RequestApprovalDto> reject(String requestApprovalId,String requestUserId) {
        RequestApproval requestApproval =requestApprovalService.reject(requestApprovalId,requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setId(requestApproval.getId().toString());
        dto.setBatchId(requestApproval.getServiceBatchChgReq().getId().toString());
        dto.setApprovalType(ApprovalType.valueOf(requestApproval.getApprovalType()));
        dto.setStatus(requestApproval.getStatus());
        dto.setStatusReason(requestApproval.getStatusReason());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        return RestfulResponse.ofData(dto);
    }

    public RestfulResponse<String> saveDeployment(Long requestApprovalId){
        ServiceDeployment serviceDeployment=requestApprovalService.saveDeployment(requestApprovalId);
        return RestfulResponse.ofData(serviceDeployment.getId().toString());
    }

    public RestfulResponse<String> synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo){
        requestApprovalService.synchDeployment(deployRefId, status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }

    public RestfulResponse<RequestApprovalDto> getApproval(String requestApprovalId){
        RequestApprovalDto dto = requestApprovalService.getApproval(requestApprovalId);
        return RestfulResponse.ofData(dto);
    }
    
    public RestfulResponse<ServiceBatchChgReqDto> getRequestApproval(String instance){
        ServiceBatchChgReqDto serviceBatchChgReqDto = requestApprovalService.getRequestApproval(instance);
        return RestfulResponse.ofData(serviceBatchChgReqDto);
    }

    public RestfulResponse<List<BaseServiceDto>> listBaseServiceAll(){
        return srvBaseService.queryAll();
    }
}
