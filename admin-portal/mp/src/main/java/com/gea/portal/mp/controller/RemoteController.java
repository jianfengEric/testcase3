package com.gea.portal.mp.controller;

import com.gea.portal.mp.entity.EwpPoolDeployment;
import com.gea.portal.mp.entity.RequestApproval;
import com.gea.portal.mp.service.MoneyPoolService;
import com.gea.portal.mp.service.RequestApprovalService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.mp.*;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by Jimmy on 2018/9/7.
 */
@RestController
@RequestMapping("remote/v1")
public class RemoteController {

    @Autowired
    private MoneyPoolService moneyPoolService;
    @Autowired
    private RequestApprovalService requestApprovalService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;

    @GetMapping("get-detail")
    public RestfulResponse<MpDetailDto> getDetail(@RequestParam String geaMoneyPoolRefId, @RequestParam Instance instance) {
        MpDetailDto mpDetailDto = moneyPoolService.getDetail(geaMoneyPoolRefId, instance);
        return RestfulResponse.ofData(mpDetailDto);
    }

    @GetMapping("approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> approval(@RequestParam String approvalRequestId,@RequestParam String requestUserId) {
    	RequestApproval requestApproval = requestApprovalService.approval(approvalRequestId,requestUserId);
    	RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setEwpMoneyPoolId(requestApproval.getEwpMoneyPool().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwpMoneyPool().getGeaParticipantRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setGeaMoneyPoolRefId(requestApproval.getEwpMoneyPool().getGeaMoneyPoolRefId());
        return RestfulResponse.ofData(dto);
    }
    
    @GetMapping("reject")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> reject(@RequestParam String approvalRequestId,@RequestParam String requestUserId) {
    	RequestApproval requestApproval = requestApprovalService.reject(approvalRequestId,requestUserId);
    	RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setEwpMoneyPoolId(requestApproval.getEwpMoneyPool().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwpMoneyPool().getGeaParticipantRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        return RestfulResponse.ofData(dto);
    }

    @GetMapping("get-approval")
    @ResponseBody
    public RestfulResponse<List<RequestApprovalDto>> getRequestApproval(@RequestParam Instance instance){
        RestfulResponse<List<RequestApprovalDto>> restfulResponse = new RestfulResponse<>();
        List<RequestApprovalDto> participantDtoList = requestApprovalService.getRequestApproval(instance);
        restfulResponse.setData(participantDtoList);
        return restfulResponse;
    }

    @GetMapping("get-all-money-pool-list")
    @ResponseBody
    public RestfulResponse<List<MoneyPoolListDto>> getAllMoneyPoolList(@ApiParam(value = "geaParticipantRefId") @RequestParam String geaParticipantRefId,
                                                                       @ApiParam(value = "instance") @RequestParam(required = false) Instance instance,
                                                                       @ApiParam(value = "status") @RequestParam(required = false) String status) {
        List<MoneyPoolStatus> statusList = new ArrayList<>();
    	for(String item : Arrays.asList(status.split(","))){
    		statusList.add(MoneyPoolStatus.findByValue(item));
    	}
    	List<MoneyPoolListDto> pageData = moneyPoolService.getAllMoneyPool(geaParticipantRefId, statusList, instance);
        
        
        RestfulResponse<List<MoneyPoolListDto>> restResponse = new RestfulResponse<>();
        if(null != pageData){
            restResponse.setData(pageData);
        }
        return restResponse;
    }

    @GetMapping("get-money-pool")
    @ResponseBody
    public RestfulResponse<MoneyPoolDto> getMoneyPoolByRefId(@RequestParam String geaMoneyPoolRefId, Instance currentEnvir) {
        MoneyPoolDto ewpMoneyPool = moneyPoolService.getMoneyPoolByRefId(geaMoneyPoolRefId,currentEnvir);
        return RestfulResponse.ofData(ewpMoneyPool);
    }

    @GetMapping("has-pending")
    @ResponseBody
    public RestfulResponse<Boolean> hasPending(@RequestParam String geaParticipantRefId, @RequestParam Instance instance,@RequestParam(required = false) Long requestApprovalId) {
    	RestfulResponse<Boolean> restfulResponse = new RestfulResponse<>();
    	Boolean hasPendingStatus = moneyPoolService.hasPending(geaParticipantRefId,instance,requestApprovalId);
    	restfulResponse.setData(hasPendingStatus);
        return restfulResponse;
    }
    
    @GetMapping("get-participant-money-pool")
    @ResponseBody
    public RestfulResponse<List<MoneyPoolDto>> getParticipantMoneyPool(@RequestParam String geaParticipantRefId, @RequestParam Instance instance) {
        List<MoneyPoolDto> ewpMoneyPool = moneyPoolService.getParticipantMoneyPool(geaParticipantRefId,instance);
        return RestfulResponse.ofData(ewpMoneyPool);
    }

    @GetMapping("get-approval-info")
    @ResponseBody
    public RestfulResponse<String> getRequestApprovalInfo(@RequestParam String mpApvReqId){
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        String  approvalType=requestApprovalService.getRequestApprovalInfo(mpApvReqId);
        restfulResponse.setData(approvalType);
        return restfulResponse;
    }
    

    @GetMapping("deploy-to-prod")
    @ResponseBody
    public RestfulResponse<Map<String,String>> deployToProduction(@RequestParam String geaParticipantRefId) {
        return moneyPoolService.deployToProduction(geaParticipantRefId);
    }
    

    @GetMapping("get-request-approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> getApproval(@RequestParam String id){
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RequestApprovalDto dto = requestApprovalService.getApproval(id);
        return RestfulResponse.ofData(dto);
    }

    
    @GetMapping("get-adjustment")
    @ResponseBody
    public RestfulResponse<PoolAdjustmentDto> getAdjustment(@RequestParam Long id){
        PoolAdjustmentDto result=moneyPoolService.getAdjustment(id);
        return RestfulResponse.ofData(result);
    }
    
    @GetMapping("get-deposit-cash-out")
    @ResponseBody
    public RestfulResponse<PoolDepositCashOutDto> getDepositCashOut(@RequestParam Long id){
        PoolDepositCashOutDto result=moneyPoolService.getDepositCashOut(id);
        return RestfulResponse.ofData(result);
    }
    
    /*@GetMapping("delete-token")
    @ResponseBody
    public void deleteToken(){
        String expiresMinus = StringUtil.getPropertyValueByKey("token.expires.mins");
        int minus = Integer.parseInt(expiresMinus);
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,-minus);
        date=calendar.getTime();
        anaAccountAccessTokenRepository.deleteByExpriedTime(date);
    }*/

    @GetMapping("synch-deployment")
    @ResponseBody
    public RestfulResponse<String> synchDeployment(@RequestParam Long deployRefId,@RequestParam DeployStatus status,
    		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date scheduleDeployDate,@RequestParam String deployVersionNo){
        moneyPoolService.synchDeployment(deployRefId,status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }
    
    @GetMapping("save-deployment")
    @ResponseBody
    public RestfulResponse<String> saveDeployment(@RequestParam Long requestApprovalId){
        EwpPoolDeployment ewpDeployment=moneyPoolService.saveDeployment(requestApprovalId);
        return RestfulResponse.ofData(ewpDeployment.getId().toString());
    }
    
    @GetMapping("find-money-pool-count")
    @ResponseBody
    public RestfulResponse<Map<String, Long>> findMoneyPoolCount(@RequestParam List<String> geaParticipantRefId, @RequestParam List<MoneyPoolStatus> status, @RequestParam Instance instance){
        Map<String, Long> map = moneyPoolService.findMoneyPoolCount(geaParticipantRefId, status, instance);
        return RestfulResponse.ofData(map);
    }
}
