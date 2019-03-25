package com.gea.portal.apv.service;


import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling tre Interface to other modules 
 */
public interface TreCallerService {
	
	public RestfulResponse<ExchangeRateFileDto> callGetApproval(String instance);
	
	public RestfulResponse<RequestApprovalDto> callApproval(String approvalRequestId,String requestUserId);
	
	public RestfulResponse<RequestApprovalDto> callGetRequestApproval(String requestApprovalId);
	
	public RestfulResponse<String> callSaveDeployment(String requestApprovalId);
	
	public RestfulResponse<ExchangeRateFileDto> callGetDetail(Long exchRateFileId, Instance instance);
	
	public RestfulResponse<RequestApprovalDto> callReject(String id,String requestUserId);
}
