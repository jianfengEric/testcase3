package com.gea.portal.apv.service;

import java.text.ParseException;

import com.gea.portal.apv.dto.ApprovalRequestDto;
import com.gea.portal.apv.dto.ApprovalResponseDto;
import com.tng.portal.common.dto.DetailsDto;
import com.tng.portal.common.enumeration.ApprovalType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 * Created by Owen on 2018/9/3.
 */
public interface ApprovalService {
    RestfulResponse<Boolean> approval(String requestApprovalId, ApprovalType approvalType, String approvalRemark, String deployDate);
    DetailsDto getApprovalDetails(Instance instance, ApprovalType approvalType, String participantRefId, String moneyPoolRefId,Long serviceBatchId,Long exchangeRateFileId);
    PageDatas<ApprovalResponseDto> getApprovalList(ApprovalRequestDto pageRequestDto) throws ParseException;
    void reject(String requestApprovalId, ApprovalType approvalType, String approvalRemark);

    public RestfulResponse<Boolean> isNeedDeploy(String requestApprovalId, ApprovalType approvalType);
    
}
