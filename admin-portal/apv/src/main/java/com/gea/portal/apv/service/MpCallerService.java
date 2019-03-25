package com.gea.portal.apv.service;

import java.util.List;

import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.mp.MpDetailDto;
import com.tng.portal.common.dto.mp.PoolAdjustmentDto;
import com.tng.portal.common.dto.mp.PoolDepositCashOutDto;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling mp Interface to other modules 
 */
public interface MpCallerService {

	RestfulResponse<RequestApprovalDto> callApproval(String approvalRequestId,String requestUserId);

	RestfulResponse<RequestApprovalDto> callReject(String approvalRequestId,String requestUserId) ;

	RestfulResponse<MpDetailDto> callGetDetail(String geaMoneyPoolRefId,String instance);

	RestfulResponse<String> callGetApprovalInfo(String mpApvReqId);

	RestfulResponse<List<RequestApprovalDto>> callGetApproval(String instance);

	RestfulResponse<RequestApprovalDto> callGetRequestApproval(String requestApprovalId);

	RestfulResponse<PoolAdjustmentDto> callGetAdjustment(Long id);

	RestfulResponse<PoolDepositCashOutDto> callGetDepositCashOut(Long id);

	RestfulResponse<String> callSaveDeployment(String requestApprovalId);

}
