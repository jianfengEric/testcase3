package com.gea.portal.ewp.controller;

import com.gea.portal.ewp.entity.EwpDeployment;
import com.gea.portal.ewp.entity.RequestApproval;
import com.gea.portal.ewp.service.EwalletParticipantService;
import com.gea.portal.ewp.service.EwpBaseService;
import com.gea.portal.ewp.service.RequestApprovalService;
import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.vo.rest.RestfulResponse;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Jimmy on 2018/9/7.
 */
@RestController
@RequestMapping("remote/v1")
public class RemoteController {

    @Autowired
    private EwalletParticipantService ewalletParticipantService;

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private EwpBaseService ewpBaseService;
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;


    @GetMapping("get-participant-by-name-or-id-list")
    @ResponseBody
    public RestfulResponse<List<ParticipantDto>> getParticipantByNameOrIdList(@RequestParam Map<String,String> map) {
        RestfulResponse<List<ParticipantDto>> restfulResponse = new RestfulResponse<>();
        List<ParticipantDto> participantDtoList = ewalletParticipantService.getParticipantByNameOrIdList(map.get("geaParticipantRefId"), map.get("participantName"),map.get("serviceId"), Instance.valueOf(map.get("instance")));
        restfulResponse.setData(participantDtoList);
        return restfulResponse;
    }

    @GetMapping("get-detail")
    public RestfulResponse<EwpDetailDto> getDetail(@RequestParam String geaParticipantRefId, @RequestParam Instance instance) {
        return ewalletParticipantService.getDetail(geaParticipantRefId, instance);
    }

    @GetMapping("get-approval")
    @ResponseBody
    public RestfulResponse<List<RequestApprovalDto>> getRequestApproval(@RequestParam String instance){
        List<RequestApprovalDto> participantDtoList = requestApprovalService.getRequestApproval(instance);
        return RestfulResponse.ofData(participantDtoList);
    }

    @GetMapping("get-participant-currency")
    @ResponseBody
    public RestfulResponse<List<String>> getParticipantCurrency(@RequestParam String geaParticipantRefId,
                                                                @ApiParam(value = "instance") @RequestParam(required = false) Instance instance){
        List<String> participantDtoList = ewpBaseService.getParticipantCurrency(geaParticipantRefId, instance);
        return RestfulResponse.ofData(participantDtoList);
    }
    
    @GetMapping("approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> approval(@RequestParam String id,@RequestParam String requestUserId){
    	if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RequestApproval requestApproval = requestApprovalService.approvalStatus(Long.valueOf(id),requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setParticipantId(requestApproval.getEwalletParticipant().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwalletParticipant().getGeaRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        return RestfulResponse.ofData(dto);
    }
    
    @GetMapping("reject")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> reject(@RequestParam String id,@RequestParam String requestUserId){
    	if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RequestApproval requestApproval = requestApprovalService.rejectApproval(Long.valueOf(id), ParticipantStatus.REJECTED,requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setParticipantId(requestApproval.getEwalletParticipant().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwalletParticipant().getGeaRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        return RestfulResponse.ofData(dto);
    }

    @GetMapping("has-pending")
    @ResponseBody
    public RestfulResponse<Boolean> hasPending(@RequestParam String geaParticipantRefId,@RequestParam Instance instance,@RequestParam(required = false) Long requestApprovalId){
        RestfulResponse<Boolean> restfulResponse = new RestfulResponse<>();
        Boolean hasPendingStatus = ewalletParticipantService.hasPending(geaParticipantRefId, instance, requestApprovalId);
        restfulResponse.setData(hasPendingStatus);
        return restfulResponse;
    }
    
    @GetMapping("get-approval-info")
    @ResponseBody
    public RestfulResponse<String> getRequestApprovalInfo(@RequestParam String ewpApvReqId){
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        restfulResponse.setData(requestApprovalService.getRequestApprovalInfo(ewpApvReqId));
        return restfulResponse;
    }

    @GetMapping("get-request-approval")
    @ResponseBody
    public RestfulResponse<RequestApprovalDto> getApproval(@RequestParam String id){
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RequestApproval requestApproval = requestApprovalService.getApproval(Long.valueOf(id));
        RequestApprovalDto dto = new RequestApprovalDto();
        dto.setParticipantId(requestApproval.getEwalletParticipant().getId().toString());
        dto.setGeaParticipantRefId(requestApproval.getEwalletParticipant().getGeaRefId());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        return RestfulResponse.ofData(dto);
    }
    
    @GetMapping("is-need-deploy")
    @ResponseBody
    public RestfulResponse<Boolean> isNeedDeploy(
    		@RequestParam String geaParticipantRefId,
    		@RequestParam Instance instance, 
    		@RequestParam(required = false) Long ewpRequestApprovalId, 
    		@RequestParam(required = false) Long mpRequestApprovalId){
        Boolean result=ewalletParticipantService.isNeedDeploy(geaParticipantRefId, instance, ewpRequestApprovalId, mpRequestApprovalId);
        return RestfulResponse.ofData(result);
    }
    
    @GetMapping("save-deployment")
    @ResponseBody
    public RestfulResponse<String> saveDeployment(@RequestParam String geaParticipantRefId, @RequestParam Instance instance){
        EwpDeployment ewpDeployment=ewalletParticipantService.saveDeployment(geaParticipantRefId, null, instance);
        return RestfulResponse.ofData(ewpDeployment.getId().toString());
    }
    
    @GetMapping("get-participant-name")
    @ResponseBody
    public RestfulResponse<Map<String,String>> getParticipantName(@RequestParam List<String> geaParticipantRefId, @RequestParam Instance instance){
    	Map<String,String> name = ewalletParticipantService.getParticipantName(geaParticipantRefId, instance);
        return RestfulResponse.ofData(name);
    }

    @GetMapping("synch-deployment")
    @ResponseBody
    public RestfulResponse<String> synchDeployment(@RequestParam Long deployRefId,@RequestParam DeployStatus status,
    		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date scheduleDeployDate,@RequestParam String deployVersionNo){
        ewalletParticipantService.synchDeployment(deployRefId,status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }
    

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

    @GetMapping("get-participant-list")
    @ResponseBody
    public RestfulResponse<List<ParticipantDto>> getParticipantList(@RequestParam(required = false) String geaParticipantRefId,
                                                              @RequestParam(required = false) Instance instance,
                                                              @RequestParam(required = false) ParticipantStatus status){
        Instance instances =  instance;
        ParticipantStatus  statuss = status;
        if(Objects.isNull(instances)){
            instances = Instance.PRE_PROD;
        }
        if(Objects.isNull(statuss)){
            statuss = ParticipantStatus.ACTIVE;
        }
        List<ParticipantDto> list = ewalletParticipantService.getParticipantList(geaParticipantRefId, instances, statuss);
        return RestfulResponse.ofData(list);
    }
    
    @GetMapping("get-participant-by-ids")
    @ResponseBody
    public RestfulResponse<Map<String,ParticipantDto>> getParticipantByIds(@RequestParam(required = false) List<String> geaParticipantRefId, @RequestParam(required = false) Instance instance){
    	Map<String,ParticipantDto> map = ewalletParticipantService.getParticipantList(geaParticipantRefId, instance);
        return RestfulResponse.ofData(map);
    }

    @GetMapping("get-related-services-by-mp")
    @ResponseBody
    public RestfulResponse<Map<String,String>> getRelatedServicesByMp(@RequestParam List<String> geaMpRefIds, Instance instance){
    	Map<String,String> name = ewalletParticipantService.getRelatedServicesByMp(geaMpRefIds,instance);
        return RestfulResponse.ofData(name);
    }

}
