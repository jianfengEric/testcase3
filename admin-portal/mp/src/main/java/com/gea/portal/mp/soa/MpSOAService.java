package com.gea.portal.mp.soa;

import com.gea.portal.mp.entity.EwpPoolDeployment;
import com.gea.portal.mp.entity.RequestApproval;
import com.gea.portal.mp.service.MoneyPoolService;
import com.gea.portal.mp.service.RequestApprovalService;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.mp.*;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.soa.AbstractMQBasicService;
import com.tng.portal.common.soa.AbstractSOABasicService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("mpSOAService")
public class MpSOAService extends AbstractMQBasicService {

    private Logger log = LoggerFactory.getLogger(AbstractSOABasicService.class);

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MoneyPoolService moneyPoolService;

    @PostConstruct
    public void initConnectToMQ(){
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            log.info("MpSOAService connectToMQ start!!");
            startListening();
            log.info("MpSOAService connectToMQ end!!");
        }else{
            log.info("MpSOAService will not run!!");
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

    public RestfulResponse<RequestApprovalDto> approval(String approvalRequestId,String requestUserId) {
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

    public RestfulResponse<RequestApprovalDto> reject(String approvalRequestId,String requestUserId) {
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

    public RestfulResponse<MpDetailDto> getDetail(String geaMoneyPoolRefId,Instance instance) {
        MpDetailDto mpDetailDto = moneyPoolService.getDetail(geaMoneyPoolRefId, instance);
        return RestfulResponse.ofData(mpDetailDto);
    }

    public RestfulResponse<String> getRequestApprovalInfo(String mpApvReqId){
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        String  approvalType=requestApprovalService.getRequestApprovalInfo(mpApvReqId);
        restfulResponse.setData(approvalType);
        return restfulResponse;
    }

    public RestfulResponse<List<RequestApprovalDto>> getRequestApproval(String instance){
        RestfulResponse<List<RequestApprovalDto>> restfulResponse = new RestfulResponse<>();
        List<RequestApprovalDto> participantDtoList = requestApprovalService.getRequestApproval(Instance.valueOf(instance));
        restfulResponse.setData(participantDtoList);
        return restfulResponse;
    }

    public RestfulResponse<RequestApprovalDto> getApproval(String id){
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RequestApprovalDto dto = requestApprovalService.getApproval(id);
        return RestfulResponse.ofData(dto);
    }

    public RestfulResponse<PoolAdjustmentDto> getAdjustment(Long id){
        PoolAdjustmentDto result=moneyPoolService.getAdjustment(id);
        return RestfulResponse.ofData(result);
    }

    public RestfulResponse<PoolDepositCashOutDto> getDepositCashOut(Long id){
        PoolDepositCashOutDto result=moneyPoolService.getDepositCashOut(id);
        return RestfulResponse.ofData(result);
    }

    public RestfulResponse<String> saveDeployment(Long requestApprovalId){
        EwpPoolDeployment ewpDeployment=moneyPoolService.saveDeployment(requestApprovalId);
        return RestfulResponse.ofData(ewpDeployment.getId().toString());
    }

    public RestfulResponse<String> synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo){
        moneyPoolService.synchDeployment(deployRefId, status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }

    public RestfulResponse<List<MoneyPoolDto>> getParticipantMoneyPool(String geaParticipantRefId,Instance instance) {
        List<MoneyPoolDto> ewpMoneyPool = moneyPoolService.getParticipantMoneyPool(geaParticipantRefId,instance);
        return RestfulResponse.ofData(ewpMoneyPool);
    }

    public RestfulResponse<List<MoneyPoolListDto>> getAllMoneyPoolList(String geaParticipantRefId, Instance instance, String status) {
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

    public RestfulResponse<Map<String,String>> deployToProduction(String geaParticipantRefId) {
        return moneyPoolService.deployToProduction(geaParticipantRefId);
    }

    public RestfulResponse<Boolean> hasPending(String geaParticipantRefId, Instance instance,Long requestApprovalId) {
        RestfulResponse<Boolean> restfulResponse = new RestfulResponse<>();
        Boolean hasPendingStatus = moneyPoolService.hasPending(geaParticipantRefId,instance,requestApprovalId);
        restfulResponse.setData(hasPendingStatus);
        return restfulResponse;
    }

    public RestfulResponse<Map<String, Long>> findMoneyPoolCount(ArrayList<String> geaParticipantRefId,ArrayList<String> status, Instance instance){
    	List<MoneyPoolStatus> statusEum = status.stream().map(item -> {
    		return MoneyPoolStatus.findByValue(item);
    	}).collect(Collectors.toList());
    	Map<String, Long> map = moneyPoolService.findMoneyPoolCount(geaParticipantRefId, statusEum, instance);
        return RestfulResponse.ofData(map);
    }

}
