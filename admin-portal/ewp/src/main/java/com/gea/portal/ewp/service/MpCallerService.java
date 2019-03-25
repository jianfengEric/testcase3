package com.gea.portal.ewp.service;

import com.tng.portal.common.dto.mp.MoneyPoolDto;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Used for calling mp Interface to other modules 
 */
public interface MpCallerService {
	
	RestfulResponse<List<MoneyPoolDto>> callGetParticipantMoneyPool(String geaParticipantRefId, Instance instance);
	
	RestfulResponse<List<MoneyPoolListDto>> callGetAllMoneyPoolList(String geaParticipantRefId, Instance instance);
	
	RestfulResponse<Map<String,String>> callDeployToProd(String geaParticipantRefId);
	
	RestfulResponse<Boolean> callHasPending(String geaParticipantRefId, Instance instance, Long requestApprovalId);
	
	public RestfulResponse<Map<String, Long>> callFindMoneyPoolCount(List<String> geaParticipantRefId, List<String> status,Instance instance);
}
