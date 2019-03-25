package com.gea.portal.tre.soa;

import com.gea.portal.tre.entity.ExchangeRateDeployment;
import com.gea.portal.tre.entity.RequestApproval;
import com.gea.portal.tre.service.RequestApprovalService;
import com.gea.portal.tre.service.TreasuryService;
import com.tng.portal.ana.repository.AnaAccountAccessTokenRepository;
import com.tng.portal.ana.service.AccountService;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
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
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service("treSOAService")
@Transactional
public class TreSOAService extends AbstractMQBasicService {

    private Logger log = LoggerFactory.getLogger(AbstractSOABasicService.class);

    @Autowired
    private RequestApprovalService requestApprovalService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AnaAccountAccessTokenRepository anaAccountAccessTokenRepository;

    @Autowired
    private TreasuryService treasuryService;

    @PostConstruct
    public void initConnectToMQ(){
        if(ApplicationContext.Env.integrated.equals(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.integratedStyle))){
            log.info("TreSOAService connectToMQ start!!");
            startListening();
            log.info("TreSOAService connectToMQ end!!");
        }else{
            log.info("TreSOAService will not run!!");
        }

    }
    @Override
    public String getServiceName() {
        log.info("getServiceName "+MessageFormat.format(PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceNameTemplate), PropertiesUtil.getAppValueByKey(ApplicationContext.Key.serviceName)));
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
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setStatus(requestApproval.getStatus());
        return RestfulResponse.ofData(dto);
    }

    public RestfulResponse<RequestApprovalDto> reject(String approvalRequestId,String requestUserId) {
        RequestApproval requestApproval = requestApprovalService.reject(approvalRequestId,requestUserId);
        RequestApprovalDto dto = new RequestApprovalDto();
        BeanUtils.copyProperties(requestApproval, dto);
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        dto.setId(requestApproval.getId().toString());
        dto.setCreateBy(requestApproval.getCreateBy());
        dto.setCreateUserName(accountService.getAccountName(requestApproval.getCreateBy()));
        dto.setCreateDate(requestApproval.getCreateDate());
        dto.setStatus(requestApproval.getStatus());
        return RestfulResponse.ofData(dto);
    }

    public void deleteToken(){
        String expiresMinus = PropertiesUtil.getPropertyValueByKey("token.expires.mins");
        int minus = Integer.parseInt(expiresMinus);
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.MINUTE,-minus);
        date=calendar.getTime();
        anaAccountAccessTokenRepository.deleteByExpriedTime(date);
    }

    public RestfulResponse<ExchangeRateFileDto> getRequestApproval(Instance instance){
        ExchangeRateFileDto participantDtoList = requestApprovalService.getRequestApproval(instance);
        return RestfulResponse.ofData(participantDtoList);
    }

    public RestfulResponse<RequestApprovalDto> getApproval(String id){
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(SystemMsg.ErrorMsg.ErrorUploadingParameter.getErrorCode());
        }
        RequestApprovalDto dto = requestApprovalService.getApproval(id);
        return RestfulResponse.ofData(dto);
    }

    public RestfulResponse<String> saveDeployment(Long requestApprovalId){
        ExchangeRateDeployment exchangeDeployment=requestApprovalService.saveDeployment(requestApprovalId);
        return RestfulResponse.ofData(exchangeDeployment.getId().toString());
    }

    public RestfulResponse<ExchangeRateFileDto> getDetail(String exchRateFileId, Instance instance) {
        long exchangeId=-999;
        if(org.apache.commons.lang.StringUtils.isNotBlank(exchRateFileId)){
            exchangeId = Long.parseLong(exchRateFileId);
        }
        ExchangeRateFileDto  response= treasuryService.getDetail(exchangeId,instance);
        return RestfulResponse.ofData(response);
    }

    public RestfulResponse<String> synchDeployment(Long deployRefId, DeployStatus status,Date scheduleDeployDate,String deployVersionNo){
        requestApprovalService.synchDeployment(deployRefId, status,scheduleDeployDate,deployVersionNo);
        return RestfulResponse.nullData();
    }
    
    public RestfulResponse<ExchangeRateListDto> getListData(Instance instance) throws IllegalAccessException, InvocationTargetException{
    	ExchangeRateListDto data = treasuryService.getListData(instance);
        return RestfulResponse.ofData(data);
    }

}
