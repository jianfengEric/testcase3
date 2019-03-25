package com.gea.portal.tre.service;

import java.util.Date;

import com.gea.portal.tre.entity.ExchangeRateDeployment;
import com.gea.portal.tre.entity.RequestApproval;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.PageDatas;

/**
 * Created by Owen on 2018/9/3.
 */
public interface RequestApprovalService {

    RequestApproval save(String exchRateFileId, Instance instance,String requestRemark);

    PageDatas<RequestApprovalDto> getRequestApproval(String moneyPoolId, String status, Instance instance,
                                                     Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending);
    RequestApproval approval(String requestApprovalId,String requestUserId);

    ExchangeRateFileDto getRequestApproval(Instance instance);

    RequestApproval reject(String requestApprovalId,String requestUserId);

    RequestApprovalDto getApproval(String requestApprovalId);

    ExchangeRateDeployment saveDeployment(Long requestApprovalId);

    void synchDeployment(Long deployRefId, DeployStatus status,Date scheduleDeployDate,String deployVersionNo);
}
