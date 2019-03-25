package com.gea.portal.mp.service.impl;

import com.gea.portal.mp.service.EwpCallerService;
import com.tng.portal.common.dto.ewp.EwpDetailDto;
import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *  Used for calling ewp Interface to other modules 
 */
@Service
@Transactional
public class EwpCallerServiceImpl extends BaseCallerService implements EwpCallerService{


    @Autowired
    private HttpClientUtils httpClientUtils;

    @Value("${ewp.method.getEwpData}")
    private String getEwpMethod;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.EWP;
	}
    

	@Override
	public RestfulResponse<List<ParticipantDto>> callGetParticipantByNameOrIdList(String geaParticipantRefId, String participantName, String serviceId, Instance instance) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("geaParticipantRefId", geaParticipantRefId);
            map.put("participantName", participantName);
            map.put("serviceId", serviceId);
            map.put("instance", instance.name());
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), getEwpMethod, ParticipantDto.class, MqParam.gen(map));
            } else  {
                String url = this.getTargetUrl().concat("/remote/v1/get-participant-by-name-or-id-list");
                setApiKeyParams(map);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<ParticipantDto>>>() {}, map);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
	}
    

    @Override
	public EwpDetailDto callGetDetail(String geaParticipantRefId, Instance instance) {
        try {
            RestfulResponse<EwpDetailDto> restfulResponse;
            String url = this.getTargetUrl().concat("/remote/v1/get-detail");
            Map<String, String> params = new HashMap<>();
            params.put("geaParticipantRefId", geaParticipantRefId);
            params.put("instance", instance == null ? null : instance.toString());
            if(super.isMq()){
                restfulResponse = RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getDetail", EwpDetailDto.class, MqParam.gen(geaParticipantRefId), MqParam.gen(instance));
            }else {
                setApiKeyParams(params);
                restfulResponse = httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<EwpDetailDto>>() {}, params);
            }
            if(Objects.nonNull(restfulResponse) && restfulResponse.hasSuccessful()){
                return restfulResponse.getData();
            }
        }catch (Exception e){
            logger.error("getServiceList", e);
        }
        return null;
	}

	@Override
	public RestfulResponse<Boolean> callHasPending(String geaParticipantRefId, String instance) {
        Map<String,String> map = new HashMap<>();
        map.put("geaParticipantRefId",geaParticipantRefId);
        map.put("instance",instance);
        if(super.isMq()){
            try {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "hasPending", Boolean.class, MqParam.gen(geaParticipantRefId), MqParam.gen(Instance.valueOf(instance)), MqParam.gen(Long.class));
            } catch (Exception e) {
                logger.error("has-pending", e);
            }
        }else {
            setApiKeyParams(map);
            String url = this.getTargetUrl().concat("/remote/v1/has-pending");
            return httpClientUtils.httpGet(url, RestfulResponse.class, map);
        }
        return null;
	}

	@Override
	public RestfulResponse<Map<String,String>> callGetParticipantName(List<String> geaParticipantRefId, Instance instance) {
		try {
	        if(super.isMq()){
	        	return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getParticipantName", Map.class, MqParam.gen(geaParticipantRefId), MqParam.gen(instance));
	        }else {
	        	Map<String,String> map = new HashMap<>();
	        	map.put("geaParticipantRefId",StringUtils.join(geaParticipantRefId.toArray(), ","));
	        	map.put("instance",instance.getValue());
	            setApiKeyParams(map);
	            String url = this.getTargetUrl().concat("/remote/v1/get-participant-name");
	            return httpClientUtils.httpGet(url, RestfulResponse.class, map);
	        }
		} catch (Exception e) {
			logger.error("get-participant-name", e);
		}
        return null;
	}

    @Override
	public RestfulResponse<List<String>> callGetParticipantCurrency(String geaParticipantRefId, Instance instance) {
        try {
            String url = this.getTargetUrl().concat("/remote/v1/get-participant-currency");
            Map<String, String> params = new HashMap<>();
            params.put("geaParticipantRefId", geaParticipantRefId);
            params.put("instance", instance.getValue());
            if(super.isMq()){
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getParticipantCurrency", String.class,MqParam.gen(geaParticipantRefId), MqParam.gen(instance));
            }else {
                setApiKeyParams(params);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<String>>>(){}, params);
            }
        }catch (Exception e){
            logger.error("getParticipantCurrency",e);
            return RestfulResponse.nullData();
        }
	}
    
	@Override
	public RestfulResponse<Map<String,String>> callGetRelatedServicesByMp(List<String> geaMpRefIds, Instance instance) {
		try {
	        if(super.isMq()){
	        	return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getRelatedServicesByMp", Map.class, MqParam.gen(List.class,geaMpRefIds),MqParam.gen(instance));
	        }else {
	        	Map<String,String> map = new HashMap<>();
	        	map.put("geaMpRefIds",StringUtils.join(geaMpRefIds.toArray(), ","));
	        	map.put("instance", instance.getValue());
	            setApiKeyParams(map);
	            String url = this.getTargetUrl().concat("/remote/v1/get-related-services-by-mp");
	            return httpClientUtils.httpGet(url, RestfulResponse.class, map);
	        }
		} catch (Exception e) {
			logger.error("get-participant-name", e);
		}
        return null;
	}

}
