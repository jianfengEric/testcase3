package com.gea.portal.apv.service;


import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.srv.ServiceBatchChgReqDto;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling dpy Interface to other modules 
 */
public interface SrvCallerService {
	
	public RestfulResponse<ServiceBatchChgReqDto> callGetApproval(String instance);

	public RestfulResponse<RequestApprovalDto> callApproval(String requestApprovalId,String requestUserId);
	
	public RestfulResponse<RequestApprovalDto> callGetRequestApproval(String requestApprovalId);
	
	public RestfulResponse<String> callSaveDeployment(String requestApprovalId);
	
	public RestfulResponse<ServiceBatchChgReqDto> callGetDetail(Long batchId);
	
	public RestfulResponse<RequestApprovalDto> callReject(String requestApprovalId,String requestUserId);
}
