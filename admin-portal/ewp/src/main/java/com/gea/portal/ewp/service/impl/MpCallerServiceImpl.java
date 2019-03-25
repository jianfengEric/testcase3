package com.gea.portal.ewp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gea.portal.ewp.service.MpCallerService;
import com.tng.portal.common.dto.mp.MoneyPoolDto;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.MoneyPoolStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling mp Interface to other modules 
 */
@Service
@Transactional
public class MpCallerServiceImpl extends BaseCallerService implements MpCallerService{

    @Autowired
    private HttpClientUtils httpClientUtils;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.MP;
	}
	private String GEA_PARTICIPANT_REF_ID = "geaParticipantRefId";
    
	@Override
	public RestfulResponse<List<MoneyPoolDto>> callGetParticipantMoneyPool(String geaParticipantRefId, Instance instance) {
        try {
            String url = this.getTargetUrl().concat("/remote/v1/get-participant-money-pool");
            Map<String, String> params = new HashMap<>();
            params.put(GEA_PARTICIPANT_REF_ID, geaParticipantRefId);
            params.put("instance", instance == null ? null : instance.toString());
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getParticipantMoneyPool", MoneyPoolDto.class, MqParam.gen(geaParticipantRefId), MqParam.gen(instance));
            } else  {
                setApiKeyParams(params);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<MoneyPoolDto>>>() {}, params);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
	}


	@Override
	public RestfulResponse<List<MoneyPoolListDto>> callGetAllMoneyPoolList(String geaParticipantRefId, Instance instance) {
        try {
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getAllMoneyPoolList", MoneyPoolListDto.class, MqParam.gen(geaParticipantRefId), MqParam.gen(instance), MqParam.gen(MoneyPoolStatus.ACTIVE.getValue() + "," + MoneyPoolStatus.DORMANT + "," + MoneyPoolStatus.SUSPEND + "," + MoneyPoolStatus.CLOSED));
            } else  {
            	String url = this.getTargetUrl().concat("/remote/v1/get-all-money-pool-list");
            	Map<String, String> params = new HashMap<>();
            	params.put(GEA_PARTICIPANT_REF_ID, geaParticipantRefId);
            	String status = MoneyPoolStatus.findValidStatus().stream().map(MoneyPoolStatus::getValue).collect(Collectors.joining(","));
            	params.put("status", status);
            	params.put("instance", instance == null ? null : instance.toString());
                setApiKeyParams(params);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<MoneyPoolListDto>>>() {}, params);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
	}


	@Override
	public RestfulResponse<Map<String,String>> callDeployToProd(String geaParticipantRefId) {
        try {
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "deployToProduction", Map.class, MqParam.gen(geaParticipantRefId));
            } else  {
            	String url = this.getTargetUrl().concat("/remote/v1/deploy-to-prod");
            	Map<String, String> params = new HashMap<>();
            	params.put("geaParticipantRefId", geaParticipantRefId);
                setApiKeyParams(params);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<Map<String, String>>>() {}, params);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }


	@Override
	public RestfulResponse<Boolean> callHasPending(String geaParticipantRefId, Instance instance, Long requestApprovalId) {
        if(super.isMq()){
            try {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "hasPending", Boolean.class, MqParam.gen(geaParticipantRefId),MqParam.gen(instance),requestApprovalId==null?MqParam.gen(Long.class):MqParam.gen(requestApprovalId));
            } catch (Exception e) {
                logger.error("has-pending", e);
            }
        }else {
        	Map<String,String> map = new HashMap<>();
        	map.put("geaParticipantRefId",geaParticipantRefId);
        	map.put("instance",instance.toString());
        	if(requestApprovalId != null){
        		map.put("requestApprovalId",requestApprovalId.toString());
        	}
            setApiKeyParams(map);
            String url = this.getTargetUrl().concat("/remote/v1/has-pending");
            return httpClientUtils.httpGet(url, RestfulResponse.class, map);
        }
        return null;
	}

	@Override
	public RestfulResponse<Map<String, Long>> callFindMoneyPoolCount(List<String> geaParticipantRefId, List<String> status, Instance instance) {
		try {
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "findMoneyPoolCount", Map.class, MqParam.gen(geaParticipantRefId), MqParam.gen(status), MqParam.gen(instance));
            } else  {
            	String url = this.getTargetUrl().concat("/remote/v1/find-money-pool-count");
            	Map<String, String> params = new HashMap<>();
            	params.put("geaParticipantRefId", String.join(",",geaParticipantRefId));
            	params.put("status", String.join(",",status));
            	params.put("instance", instance == null ? null : instance.toString());
                setApiKeyParams(params);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<Map<String, Long>>>() {}, params);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
	}


}
