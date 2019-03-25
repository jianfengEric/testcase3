package com.tng.portal.ana.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.tng.portal.common.dto.ewp.ParticipantDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling ewp Interface to other modules 
 */
@Service("anaEwpCallerService")
public class EwpCallerService extends BaseCallerService {


	@Autowired
	private HttpClientUtils httpClientUtils;
	
	/*@Override
	protected Map<String, String> setApiKeyParams(Map<String, String> params){
		params.put("apiKey", PropertiesUtil.getPropertyValueByKey("ana.api.key"));
		params.put("consumer", ApplicationContext.Modules.ANA);
		return params;
	}*/

	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.EWP;
	}

	public ParticipantDto getParticipant(String geaParticipantRefId){
		List<ParticipantDto> list = getParticipantList(geaParticipantRefId);
		if(CollectionUtils.isNotEmpty(list)){
			return list.stream().findFirst().orElse(null);
		}
		return null;
	}

	public List<ParticipantDto> getParticipantList(String geaParticipantRefId) {
		List<ParticipantDto> list = new ArrayList<>();
		try {
				RestfulResponse<List<ParticipantDto>> response = null;
				Map<String, String> map = new HashMap<>();
				map.put("geaParticipantRefId", geaParticipantRefId);
				map.put("instance", Instance.PRE_PROD.getValue());
				map.put("status", ParticipantStatus.ACTIVE.getValue());
				String url = this.getTargetUrl().concat("/remote/v1/get-participant-list");
				setApiKeyParams(map);
				response = httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<ParticipantDto>>>() {
				}, map);
			if(Objects.nonNull(response) && response.hasSuccessful()){
				list = response.getData();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return list;
	}

	public RestfulResponse<Map<String,ParticipantDto>> callGetParticipantByIds(List<String> geaParticipantRefId, Instance instance){
		try {
			Map<String, String> map = new HashMap<>();
			map.put("geaParticipantRefId", String.join(",", geaParticipantRefId));
			map.put("instance", instance.toString());
			String url = this.getTargetUrl().concat("/remote/v1/get-participant-by-ids");
			setApiKeyParams(map);
			return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<Map<String,ParticipantDto>>>() { }, map);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
