package com.gea.portal.apv.service;

import java.util.List;
import java.util.Map;

import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling ewp Interface to other modules 
 */
public interface EwpCallerService {
	
	RestfulResponse<RequestApprovalDto> callApproval(String id,String requestUserId);
	
	RestfulResponse<RequestApprovalDto> callReject(String id,String requestUserId);
	
	RestfulResponse<EwpDetailDto> callGetDetail(String geaParticipantRefId,String instance);
	
	RestfulResponse<String> callGetApprovalInfo(String ewpApvReqId) ;
	
	RestfulResponse<List<RequestApprovalDto>> callGetApproval(String instance);

	RestfulResponse<RequestApprovalDto> callRequestApproval(String requestApprovalId);
	
	RestfulResponse<Boolean> callIsNeedDeploy(String geaParticipantRefId,Instance instance, Long ewpRequestApprovalId, Long mpRequestApprovalId);
	
	RestfulResponse<String> callSaveDeployment(String geaParticipantRefId, Instance instance);

	RestfulResponse<Map<String,String>> callGetParticipantName(List<String> geaParticipantRefId,String instance);

	List<ParticipantDto> callGetParticipantByNameOrIdList(String geaParticipantRefId, String participantName, Instance instance);
}
