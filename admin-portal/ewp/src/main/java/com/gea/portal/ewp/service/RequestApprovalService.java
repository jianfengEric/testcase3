package com.gea.portal.ewp.service;

import com.gea.portal.ewp.entity.EwalletParticipant;
import com.gea.portal.ewp.entity.RequestApproval;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.vo.PageDatas;

import java.util.List;

/**
 * Created by Dell on 2018/8/29.
 */
public interface RequestApprovalService {

	RequestApproval save(EwalletParticipant ewalletParticipant, String ewpCompanyFormId, String ewpServiceId,String ewpServiceSettlementId, String ewpMoneyPoolId, String ewpGatewayConfigId, String ewpStatusChangeId, Instance instance, ApprovalType approvalType,String requestRemark);
    
    RequestApproval approvalStatus(Long requestApprovalId,String requestUserId);
    List<RequestApprovalDto> getRequestApproval(String instance);
    RequestApproval rejectApproval(Long requestApprovalId, ParticipantStatus status,String requestUserId);
    
    /**
     * find By ParticipantId
     */
    public List<RequestApproval> findByParticipantId(Long participantId);

    String getRequestApprovalInfo(String ewpApvReqId);

    RequestApproval getApproval(Long requestApprovalId);

}
