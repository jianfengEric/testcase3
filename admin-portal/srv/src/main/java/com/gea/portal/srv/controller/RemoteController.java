package com.gea.portal.srv.controller;


import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gea.portal.srv.entity.RequestApproval;
import com.gea.portal.srv.entity.ServiceDeployment;
import com.gea.portal.srv.service.RequestApprovalService;
import com.gea.portal.srv.service.SrvBaseService;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.vo.rest.RestfulResponse;


@RestController
@RequestMapping("remote/v1")
public class RemoteController {

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SrvBaseService srvBaseService;

    @GetMapping("get-detail")
    public RestfulResponse<ServiceBatchChgReqDto> getDetail(@RequestParam Long batchId) {
        ServiceBatchChgReqDto serviceBatchChgReqDto = srvBaseService.getDetail(batchId);
        return RestfulResponse.ofData(serviceBatchChgReqDto);
    }

    @GetMapping("get-approval")
    @ResponseBody
    public RestfulResponse<ServiceBatchChgReqDto> getRequestApproval(@RequestParam String instance){
        RestfulResponse restfulResponse=new RestfulResponse();
        ServiceBatchChgReqDto serviceBatchChgReqDto = requestApprovalService.getRequestApproval(instance);
        restfulResponse.setData(serviceBatchChgReqDto);
        return restfulResponse;
    }

    @GetMapping("approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> approval(@RequestParam String requestApprovalId,@RequestParam String requestUserId) {
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

    @GetMapping("reject")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> reject(@RequestParam String requestApprovalId,@RequestParam String requestUserId) {
        RequestApproval requestApproval =requestApprovalService.reject(requestApprovalId, requestUserId);
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

    @GetMapping("save-deployment")
    @ResponseBody
    public RestfulResponse<String> saveDeployment(@RequestParam Long requestApprovalId){
        ServiceDeployment serviceDeployment=requestApprovalService.saveDeployment(requestApprovalId);
        return RestfulResponse.ofData(serviceDeployment.getId().toString());
    }

    @GetMapping("synch-deployment")
    @ResponseBody
    public RestfulResponse<String> synchDeployment(@RequestParam Long deployRefId,@RequestParam DeployStatus status,
    		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date scheduleDeployDate,@RequestParam String deployVersionNo){
        requestApprovalService.synchDeployment(deployRefId, status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }

    @GetMapping("get-request-approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> getApproval(@RequestParam String requestApprovalId){
        RequestApprovalDto dto = requestApprovalService.getApproval(requestApprovalId);
        return RestfulResponse.ofData(dto);
    }

    @GetMapping("get-baseService")
    @ResponseBody
    public RestfulResponse<List<BaseServiceDto>> listBaseServiceAll(){
       return srvBaseService.queryAll();
    }

}
