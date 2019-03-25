package com.gea.portal.mp.service;

import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;
import java.util.Map;

/**
 *  Used for calling ewp Interface to other modules 
 */
public interface EwpCallerService {
	
	public RestfulResponse<List<ParticipantDto>> callGetParticipantByNameOrIdList(String geaParticipantRefId, String participantName,String serviceId,Instance instance);
	
	EwpDetailDto callGetDetail(String geaParticipantRefId,Instance instance);
	
	public RestfulResponse<Boolean> callHasPending(String geaParticipantRefId,String instance);
	
	public RestfulResponse<Map<String,String>> callGetParticipantName(List<String> geaParticipantRefId,Instance instance);

	public RestfulResponse<List<String>> callGetParticipantCurrency(String geaParticipantRefId, Instance instance);
	
	public RestfulResponse<Map<String,String>> callGetRelatedServicesByMp(List<String> geaMpRefIds, Instance instance);

}
