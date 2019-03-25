package com.gea.portal.tre.controller;

import com.gea.portal.tre.entity.ExchangeRateDeployment;
import com.gea.portal.tre.entity.RequestApproval;
import com.gea.portal.tre.service.RequestApprovalService;
import com.gea.portal.tre.service.TreasuryService;
import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by Dell on 2018/10/11.
 */
@RestController
@RequestMapping("remote/v1")
public class RemoteController {
    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;
    @Autowired
    private RequestApprovalService requestApprovalService;
    @Autowired
    private TreasuryService treasuryService;


    /*@GetMapping("delete-token")
    @ResponseBody
    public void deleteToken(){
        String expiresMinus = StringUtil.getPropertyValueByKey("token.expires.mins");
        int minus = Integer.parseInt(expiresMinus);
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MINUTE,-minus);
        date=calendar.getTime();
        anaAccountAccessTokenRepository.deleteByExpriedTime(date);
    }*/

    @GetMapping("get-approval")
    @ResponseBody
    public RestfulResponse<ExchangeRateFileDto> getRequestApproval(@RequestParam Instance instance){
    	ExchangeRateFileDto participantDtoList = requestApprovalService.getRequestApproval(instance);
        return RestfulResponse.ofData(participantDtoList);
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

    @GetMapping("approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> approval(@RequestParam String approvalRequestId,@RequestParam String requestUserId) {
        RequestApproval requestApproval = requestApprovalService.approval(approvalRequestId,requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setExchRateFileId(requestApproval.getExchRateFileId());
        return RestfulResponse.ofData(dto);
    }

    @GetMapping("save-deployment")
    @ResponseBody
    public RestfulResponse<String> saveDeployment(@RequestParam Long requestApprovalId){
        ExchangeRateDeployment exchangeDeployment=requestApprovalService.saveDeployment(requestApprovalId);
        return RestfulResponse.ofData(exchangeDeployment.getId().toString());
    }

    @GetMapping("get-detail")
    public RestfulResponse<ExchangeRateFileDto> getDetail(@RequestParam String exchRateFileId,
                                                          @RequestParam Instance instance) {
        long exchangeId=-999;
        if(org.apache.commons.lang.StringUtils.isNotBlank(exchRateFileId)){
            exchangeId = Long.parseLong(exchRateFileId);
        }
        ExchangeRateFileDto  response= treasuryService.getDetail(exchangeId,instance);
        return RestfulResponse.ofData(response);
    }


    @GetMapping("reject")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> reject(@RequestParam String approvalRequestId,@RequestParam String requestUserId) {
        RequestApproval requestApproval = requestApprovalService.reject(approvalRequestId,requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setExchRateFileId(requestApproval.getExchRateFileId());
        return RestfulResponse.ofData(dto);
    }

    @GetMapping("synch-deployment")
    @ResponseBody
    public RestfulResponse<String> synchDeployment(@RequestParam Long deployRefId,@RequestParam DeployStatus status,
    		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date scheduleDeployDate,@RequestParam String deployVersionNo){
        requestApprovalService.synchDeployment(deployRefId, status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }
    
    @GetMapping("get-list-data")
    @ResponseBody
    public RestfulResponse<ExchangeRateListDto> getListData(@RequestParam Instance instance) throws IllegalAccessException, InvocationTargetException{
    	ExchangeRateListDto data = treasuryService.getListData(instance);
        return RestfulResponse.ofData(data);
    }

}
