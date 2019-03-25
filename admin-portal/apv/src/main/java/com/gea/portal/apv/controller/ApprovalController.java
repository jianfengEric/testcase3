package com.gea.portal.apv.controller;

import com.gea.portal.apv.dto.ApprovalRequestDto;
import com.gea.portal.apv.dto.ApprovalResponseDto;
import com.gea.portal.apv.service.*;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.constant.PermissionId;
import com.tng.portal.common.dto.DetailsDto;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owen on 2018/9/3.
 */
@RestController
@RequestMapping("approval/v1")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;
    
    @Autowired
    private MpCallerService mpCallerService;
    
    @Autowired
    private EwpCallerService ewpCallerService;

    @Autowired
    private SrvCallerService srvCallerService;
    
    @Autowired
    private TreCallerService treCallerService;
    
    @Autowired
    private DpyCallerService dpyCallerService;
    
    
    public static final String PERMISSION="ADJUSTMENT,API_GATEWAY_SETTING,CREATE_INDIVIDUAL_MONEY_POOL,CREATE_PARTICIPANT,DEPOSIT,CASH_OUT" +
            ",EDIT_MONEY_POOL_STATUS_AND_ALERT_LINE,EXCHANGE_RATE,FULL_COMPANY_INFORMATION,SERVICE_POOL_ASSIGNMENT," +
            "SERVICE_SETTING,SETTLEMENT_CUT_OFF_TIME_SETTING,VOID_CHARGE_CHARGE_FEE,VOID_CHARGE_FEE,SERVICE_MARKUP,PARTICIPANT_STATUS_CHANGE";

    @GetMapping("get-ewp-approval")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<List<RequestApprovalDto>> getEwpApprovalList(@ApiParam(value="Instance PRE_PROD or PROD")@RequestParam(required = false) Instance instance){
        return ewpCallerService.callGetApproval(instance.toString());
    }

    @GetMapping("get-mp-approval")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<List<RequestApprovalDto>> getMpApprovalList(@ApiParam(value="Instance PRE_PROD or PROD")@RequestParam(required = false) Instance instance){
        return mpCallerService.callGetApproval(instance.toString());
    }
    
    @GetMapping("get-srv-approval")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<ServiceBatchChgReqDto> getSrvList(@ApiParam(value="Instance PRE_PROD or PROD")@RequestParam(required = false) Instance instance){
        return srvCallerService.callGetApproval(instance.toString());
    }
    
    
    @GetMapping("get-tre-approval")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<ExchangeRateFileDto> getTreList(@ApiParam(value="Instance PRE_PROD or PROD")@RequestParam(required = false) Instance instance){
        return treCallerService.callGetApproval(instance.toString());
    }

    @PostMapping("approval")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<Boolean> approval(@RequestParam String id, @RequestParam ApprovalType approvalType,
                                            @RequestParam(required = false) String approvalRemark,
                                            @RequestParam(required = false) String deployScheduleDate){
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        return approvalService.approval(id, approvalType, approvalRemark, deployScheduleDate);
    }


    @GetMapping("get-approval-detail")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<DetailsDto> getApprovalDetail(
            @RequestParam(required = false) Instance instance,
            @RequestParam(required = true) ApprovalType approvalType,
            @RequestParam(required = false) String geaParticipantRefId,
            @RequestParam(required = false) String geaMoneyPoolRefId,
            @RequestParam(required = false) Long serviceBatchId,
            @RequestParam(required = false) Long exchangeRateFileId
            ){
        DetailsDto restResponse = approvalService.getApprovalDetails(instance, approvalType,geaParticipantRefId,geaMoneyPoolRefId,serviceBatchId,exchangeRateFileId);
        return RestfulResponse.ofData(restResponse);
    }


    @GetMapping("get-approval")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<PageDatas<ApprovalResponseDto>> getApprovalList(ApprovalRequestDto pageRequestDto) throws ParseException{
        PageDatas<ApprovalResponseDto> restResponse = approvalService.getApprovalList(pageRequestDto);
        return RestfulResponse.ofData(restResponse);
    }

    @PostMapping("reject")
    @ResponseBody
    @PreAuthorize("hasPermission2('"+PERMISSION+"',"+ PermissionId.APPROVAL+")")
    public RestfulResponse<String> reject(@RequestParam String id, @RequestParam ApprovalType approvalType, @RequestParam(required = false) String approvalRemark){
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        approvalService.reject(id, approvalType, approvalRemark);
        return RestfulResponse.nullData();
    }

    @GetMapping("get-Approval-Status-list")
    @ResponseBody
    public RestfulResponse<List<String>> getApprovalStatusList() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        RequestApprovalStatus[] s = RequestApprovalStatus.values();
        for(int i = 0; i< s.length; i++){
            if(!s[i].equals(RequestApprovalStatus.PENDING_FOR_APPROVAL)){
            	list.add(s[i].name());
            }
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }

    @GetMapping("get-Approval-Type-list")
    @ResponseBody
    public RestfulResponse<List<String>> getApprovalTypeList() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        ApprovalType[] s = ApprovalType.values();
        for(int i = 0; i< s.length; i++){
            if(!s[i].equals(ApprovalType.SERVICE_MARKUP)){
                list.add(s[i].name());
            }
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }

    @GetMapping("get-Deploy-Status-list")
    @ResponseBody
    public RestfulResponse<List<String>> getDeployStatusList() {
        RestfulResponse<List<String>> restfulResponse = new RestfulResponse<>();
        List<String> list = new ArrayList<>();
        DeployStatus[] s = DeployStatus.values();
        for(int i = 0; i< s.length; i++){
            list.add(s[i].getValue());
        }
        restfulResponse.setData(list);
        return restfulResponse;
    }

    
    @GetMapping("is-need-deploy") 
    @ResponseBody
    public RestfulResponse<Boolean> isNeedDeploy(@RequestParam String requestApprovalId, @RequestParam ApprovalType approvalType) {
        return approvalService.isNeedDeploy(requestApprovalId, approvalType);
    }
    
    @GetMapping("has-pending-deploy") 
    @ResponseBody
    public RestfulResponse<Integer> hasPendingDeploy(DeployType deployType, Instance deployEnvir) {
        return dpyCallerService.callHasPending(deployType, deployEnvir);
    }
    
}
