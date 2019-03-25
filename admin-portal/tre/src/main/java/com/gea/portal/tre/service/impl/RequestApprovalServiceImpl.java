package com.gea.portal.tre.service.impl;

import com.gea.portal.tre.entity.ExchangeRateDeployment;
import com.gea.portal.tre.entity.ExchangeRateFile;
import com.gea.portal.tre.entity.RequestApproval;
import com.gea.portal.tre.repository.ExchangeRateDeploymentRepository;
import com.gea.portal.tre.repository.ExchangeRateFileRepository;
import com.gea.portal.tre.repository.RequestApprovalRepository;
import com.gea.portal.tre.service.AnaCallerService;
import com.gea.portal.tre.service.RequestApprovalService;
import com.gea.portal.tre.service.TreasuryService;
import com.tng.portal.ana.bean.Account;
import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.ana.service.UserService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.RequestApprovalStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.vo.PageDatas;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Owen on 2018/9/20.
 */
@Service
@Transactional
public class RequestApprovalServiceImpl implements RequestApprovalService {

    @Qualifier("anaUserService")
    @Autowired
    private UserService userService;

    @Autowired
    private RequestApprovalRepository requestApprovalRepository;
    @Autowired
    private ExchangeRateFileRepository exchangeRateFileRepository;
    @Autowired
    private ExchangeRateDeploymentRepository exchangeRateDeploymentRepository;

    @Autowired
    private TreasuryService treasuryService;

    @Autowired
    private AnaCallerService anaCallerService;
    private String requestApprovalId = "requestApprovalId";

    @Override
    public RequestApproval save(String exchRateFileId, Instance instance,String requestRemark) {
        Account loginAccount = userService.getCurrentAccountInfo();
        RequestApproval requestApproval = new RequestApproval();
        requestApproval.setCreateDate(new Date());
        requestApproval.setCreateBy(loginAccount.getAccountId());
        requestApproval.setCurrentEnvir(instance);
        requestApproval.setExchRateFileId(exchRateFileId);
        requestApproval.setStatus(RequestApprovalStatus.PENDING_FOR_APPROVAL);
        requestApproval.setApprovalType(ApprovalType.EXCHANGE_RATE_SETTING);
        requestApproval.setRequestRemark(requestRemark);
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public PageDatas<RequestApprovalDto> getRequestApproval(String moneyPoolId, String status, Instance instance, Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending) {
        return null;
    }

    @Override
    public RequestApproval approval(String requestApprovalId,String requestUserId) {
        if (StringUtils.isBlank(requestApprovalId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{requestApprovalId});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        if(null == requestApproval){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"requestApproval"});
        }
        if(!requestApproval.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL)){
            throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }
        Date updateDate = new Date();
        requestApproval.setStatus(RequestApprovalStatus.APPROVED);
        requestApproval.setUpdateBy(getRequestAccountId(requestUserId));
        requestApproval.setUpdateDate(updateDate);
        requestApprovalRepository.save(requestApproval);
        ExchangeRateFile exchangeRateFile = exchangeRateFileRepository.findOne(Long.valueOf(requestApproval.getExchRateFileId()));
        exchangeRateFile.setStatus("PROCESSED");
        exchangeRateFileRepository.save(exchangeRateFile);
        return requestApproval;
    }

    @Override
    public ExchangeRateFileDto getRequestApproval(Instance instance) {
        List<RequestApproval> list = requestApprovalRepository.findByStatusAndCurrentEnvir(RequestApprovalStatus.PENDING_FOR_APPROVAL, instance);
        if(CollectionUtils.isEmpty(list)){
        	return null;
        }
        RequestApproval requestApproval = list.get(0);
        ExchangeRateFileDto  response= treasuryService.getDetail(Long.valueOf(requestApproval.getExchRateFileId()),instance);
        response.setRequestApprovalId(requestApproval.getId());
        response.setRequestRemark(requestApproval.getRequestRemark());
        return response;
    }

    @Override
    public RequestApproval reject(String requestApprovalId,String requestUserId) {
        if (StringUtils.isBlank(requestApprovalId)) {
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{requestApprovalId});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        if (requestApproval == null) {
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"RequestApproval"});
        }
        if(!requestApproval.getStatus().equals(RequestApprovalStatus.PENDING_FOR_APPROVAL)){
            throw new BusinessException(SystemMsg.ApvErrorMsg.HAS_BEEN_APPROVED.getErrorCode());
        }
        Date updateDate = new Date();
        requestApproval.setStatus(RequestApprovalStatus.REJECT);
        requestApproval.setUpdateBy(getRequestAccountId(requestUserId));
        requestApproval.setUpdateDate(updateDate);
        requestApprovalRepository.save(requestApproval);
        return requestApproval;
    }

    @Override
    public RequestApprovalDto getApproval(String requestApprovalId) {
        RequestApproval requestApproval = requestApprovalRepository.findOne(Long.valueOf(requestApprovalId));
        RequestApprovalDto dto = new RequestApprovalDto();
        dto.setExchRateFileId(requestApproval.getExchRateFileId());
        dto.setApprovalType(requestApproval.getApprovalType());
        dto.setStatus(requestApproval.getStatus());
        dto.setStatusReason(requestApproval.getStatusReason());
        dto.setCurrentEnvir(requestApproval.getCurrentEnvir());
        return dto;
    }


    @Override
    public ExchangeRateDeployment saveDeployment(Long requestApprovalId) {
        if(null == requestApprovalId){
            throw new BusinessException(SystemMsg.ErrorMsg.CAN_NOT_BE_EMPTY.getErrorCode(), new String[]{"requestApprovalId"});
        }
        RequestApproval requestApproval = requestApprovalRepository.findOne(requestApprovalId);
        if(null == requestApproval){
            throw new BusinessException(SystemMsg.ErrorMsg.IS_NULL.getErrorCode(), new String[]{"RequestApproval"});
        }
        Date createDate = new Date();
        ExchangeRateDeployment ewpDeployment = new ExchangeRateDeployment();
        ewpDeployment.setCreateBy(null);
        ewpDeployment.setCreateDate(createDate);
        ewpDeployment.setStatus(DeployStatus.PENDING_FOR_DEPLOY.getValue());
        ewpDeployment.setRequestApprovalId(requestApproval.getId());
        ewpDeployment.setDeployEnvir(requestApproval.getCurrentEnvir());
        exchangeRateDeploymentRepository.save(ewpDeployment);
        return ewpDeployment;
    }

    @Override
    public void synchDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo) {
        ExchangeRateDeployment exchangeDeployment = exchangeRateDeploymentRepository.findOne(deployRefId);
        exchangeDeployment.setStatus(status.getValue());
        exchangeDeployment.setUpdateDate(new Date());
        exchangeDeployment.setScheduleDeployDate(scheduleDeployDate);
        exchangeDeployment.setDeployVersionNo(deployVersionNo);
        exchangeRateDeploymentRepository.save(exchangeDeployment);
    }

    private String getRequestAccountId(String requestUserId) {
        return anaCallerService.callFindBindingId(requestUserId, ApplicationContext.Modules.APV, ApplicationContext.Modules.TRE).getData();
    }
}
