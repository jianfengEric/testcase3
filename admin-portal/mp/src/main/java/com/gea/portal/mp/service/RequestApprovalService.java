package com.gea.portal.mp.service;

import com.gea.portal.mp.entity.*;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.PageDatas;

import java.util.List;

/**
 * Created by Jimmy on 2018/9/3.
 */
public interface RequestApprovalService {

    RequestApproval saveMoneyPoolRequestApproval(String geaParticipantRefId, EwpMoneyPool ewpMoneyPool, Instance instance,String requestRemark);
    RequestApproval savePoolAdjustmentRequestApproval(String geaParticipantRefId, EwpPoolAdjustment ewpPoolAdjustment, Instance instance,String requestRemark);
    RequestApproval savePoolDepositCashOutRequestApproval(String geaParticipantRefId, EwpPoolDepositCashOut ewpPoolDepositCashOut, Instance instance,String requestRemark);
    RequestApproval saveMpChangeRequestApproval(String geaParticipantRefId, EwpMpChangeReq ewpMpChangeReq, Instance instance,String requestRemark);

    RequestApproval approval(String requestApprovalId,String requestUserId);

    List<RequestApprovalDto> getRequestApproval(Instance instance);

    RequestApproval reject(String requestApprovalId,String requestUserId);

    String getRequestApprovalInfo(String mpApvReqId);

    RequestApprovalDto getApproval(String requestApprovalId);

}
