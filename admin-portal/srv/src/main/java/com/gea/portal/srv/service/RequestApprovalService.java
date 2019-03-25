package com.gea.portal.srv.service;

import java.util.Date;

import com.gea.portal.srv.entity.RequestApproval;
import com.gea.portal.srv.entity.ServiceBatchChgReq;
import com.gea.portal.srv.entity.ServiceDeployment;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.enumeration.Instance;

public interface RequestApprovalService {

    void saveServiceChangeRequestApproval(ServiceBatchChgReq serviceBatchChgReq, Instance instance,String requestRemark);

    RequestApproval approval(String requestApprovalId,String requestUserId);

    RequestApproval reject(String requestApprovalId,String requestUserId);

    ServiceBatchChgReqDto getRequestApproval(String instance);

    Boolean hasPendingStatus(Instance instance);

    ServiceDeployment saveDeployment(Long requestApprovalId);

    void synchDeployment(Long deployRefId, DeployStatus deployStatus,Date scheduleDeployDate,String deployVersionNo);

    RequestApprovalDto getApproval(String requestApprovalId);

    RequestApproval findByCurrentEnvir(String instance);
}
