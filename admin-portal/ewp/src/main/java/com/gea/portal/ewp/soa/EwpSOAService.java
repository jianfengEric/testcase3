package com.gea.portal.ewp.soa;

import com.gea.portal.ewp.entity.EwpDeployment;
import com.gea.portal.ewp.entity.RequestApproval;
import com.gea.portal.ewp.service.EwalletParticipantService;
import com.gea.portal.ewp.service.EwpBaseService;
import com.gea.portal.ewp.service.RequestApprovalService;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Owen on 2018/9/3.
 */
@Service("ewpSOAService")
@Transactional
public class EwpSOAService extends AbstractMQBasicService {

    private Logger log = LoggerFactory.getLogger(AbstractSOABasicService.class);

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private EwalletParticipantService ewalletParticipantService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private EwpBaseService ewpBaseService;

    @PostConstruct
    public void initConnectToMQ(){
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            log.info("EwpSOAService connectToMQ start!!");
            startListening();
            log.info("EwpSOAService connectToMQ end!!");
        }else{
            log.info("EwpSOAService will not run!!");
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


    public RestfulResponse<RequestApprovalDto> approval(String id,String requestUserId){
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
    public RestfulResponse<List<ParticipantDto>> getParticipantByNameOrIdList(HashMap<String,String> map){
        RestfulResponse<List<ParticipantDto>> restfulResponse = new RestfulResponse<>();
        List<ParticipantDto> participantDtoList = ewalletParticipantService.getParticipantByNameOrIdList(map.get("geaParticipantRefId"), map.get("participantName"),null, Instance.valueOf(map.get("instance")));
        restfulResponse.setData(participantDtoList);
        return restfulResponse;
    }

    public RestfulResponse<RequestApprovalDto> reject(String id,String requestUserId){
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

    public RestfulResponse<List<ParticipantDto>> getParticipantList(String geaParticipantRefId, Instance instance, ParticipantStatus status){
        Instance instances = instance;
        ParticipantStatus statuss = status;
        if(Objects.isNull(instances)){
            instances = Instance.PRE_PROD;
        }
        if(Objects.isNull(statuss)){
            statuss = ParticipantStatus.ACTIVE;
        }
        List<ParticipantDto> list = ewalletParticipantService.getParticipantList(geaParticipantRefId, instances, statuss);
        return RestfulResponse.ofData(list);
    }

    public RestfulResponse<EwpDetailDto> getDetail(String geaParticipantRefId, Instance instance) {
        return ewalletParticipantService.getDetail(geaParticipantRefId, instance);
    }

    public RestfulResponse<String> getRequestApprovalInfo(String ewpApvReqId){
        RestfulResponse<String> restfulResponse = new RestfulResponse<>();
        restfulResponse.setData(requestApprovalService.getRequestApprovalInfo(ewpApvReqId));
        return restfulResponse;
    }

    public RestfulResponse<List<RequestApprovalDto>> getRequestApproval(String instance){
        List<RequestApprovalDto> participantDtoList = requestApprovalService.getRequestApproval(instance);
        return RestfulResponse.ofData(participantDtoList);
    }

    public RestfulResponse<RequestApprovalDto> getApproval(String id){
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

    public RestfulResponse<Boolean> isNeedDeploy(HashMap<String, String> params){
        Boolean result=ewalletParticipantService.isNeedDeploy(String.valueOf(params.get("geaParticipantRefId")), Instance.valueOf(params.get("instance")), params.get("ewpRequestApprovalId")==null?null:Long.valueOf(params.get("ewpRequestApprovalId")), params.get("mpRequestApprovalId")==null?null:Long.valueOf(params.get("mpRequestApprovalId")));
        return RestfulResponse.ofData(result);
    }

    public RestfulResponse<String> saveDeployment(String geaParticipantRefId, Instance instance){
        EwpDeployment ewpDeployment=ewalletParticipantService.saveDeployment(geaParticipantRefId, null, instance);
        return RestfulResponse.ofData(ewpDeployment.getId().toString());
    }

    public RestfulResponse<Map<String,String>> getParticipantName(ArrayList<String> geaParticipantRefId, Instance instance){
    	Map<String,String> name = ewalletParticipantService.getParticipantName(geaParticipantRefId, instance);
        return RestfulResponse.ofData(name);
    }

    public RestfulResponse<Boolean> hasPending(String geaParticipantRefId,Instance instance,Long requestApprovalId){
        RestfulResponse<Boolean> restfulResponse = new RestfulResponse<>();
        Boolean hasPendingStatus = ewalletParticipantService.hasPending(geaParticipantRefId, instance, requestApprovalId);
        restfulResponse.setData(hasPendingStatus);
        return restfulResponse;
    }

    public RestfulResponse<List<String>> getParticipantCurrency(String geaParticipantRefId, Instance instance){
        List<String> participantDtoList = ewpBaseService.getParticipantCurrency(geaParticipantRefId, instance);
        return RestfulResponse.ofData(participantDtoList);
    }

    public RestfulResponse<String> synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo){
        ewalletParticipantService.synchDeployment(deployRefId, status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }
    
    public RestfulResponse<Map<String,String>> getRelatedServicesByMp(List<String> geaMpRefIds, Instance instance){
    	Map<String,String> name = ewalletParticipantService.getRelatedServicesByMp(geaMpRefIds, instance);
        return RestfulResponse.ofData(name);
    }
}
