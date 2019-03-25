package com.gea.portal.apv.service.impl;

import com.gea.portal.apv.service.EwpCallerService;
import com.tng.portal.common.dto.RequestApprovalDto;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *  Used for calling ewp Interface to other modules 
 */
@Service
@Transactional
public class EwpCallerServiceImpl extends BaseCallerService implements EwpCallerService{



    @Autowired
    private HttpClientUtils httpClientUtils;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.EWP;
	}
 	
	@Override
	public RestfulResponse<RequestApprovalDto> callApproval(String id,String requestUserId) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "approval", RequestApprovalDto.class, MqParam.gen(id),MqParam.gen(requestUserId));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/approval");
				Map<String, String> params = new HashMap<>();
				params.put("id", id);
				params.put("requestUserId", requestUserId);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<RequestApprovalDto> callReject(String id,String requestUserId) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "reject", RequestApprovalDto.class, MqParam.gen(id),MqParam.gen(requestUserId));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/reject");
				Map<String, String> params = new HashMap<>();
				params.put("id", id);
				params.put("requestUserId", requestUserId);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<EwpDetailDto> callGetDetail(String geaParticipantRefId, String instance) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-detail");
			Map<String, String> params = new HashMap<>();
			params.put("geaParticipantRefId", geaParticipantRefId);
			params.put("instance", instance == null ? null : instance);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getDetail", EwpDetailDto.class, MqParam.gen(geaParticipantRefId), MqParam.gen(Instance.valueOf(instance)));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<EwpDetailDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<String> callGetApprovalInfo(String ewpApvReqId) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-approval-info");
			Map<String, String> param = new HashMap<>();
			param.put("ewpApvReqId", ewpApvReqId);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getRequestApprovalInfo", String.class, MqParam.gen(ewpApvReqId));
			} else  {
				setApiKeyParams(param);
				return httpClientUtils.httpGet(url, RestfulResponse.class, param);
			}
		} catch (Exception e) {
			logger.error("/remote/v1/get-approval-info", e);
		}
		return null;
	}

	@Override
	public RestfulResponse<List<RequestApprovalDto>> callGetApproval(String instance) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-approval");
			Map<String, String> params = new HashMap<>();
			params.put("instance", instance == null ? null : instance);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getRequestApproval", RequestApprovalDto.class, MqParam.gen(instance));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<RequestApprovalDto>>>() {}, params);
			}
		}catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<RequestApprovalDto> callRequestApproval(String requestApprovalId) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-request-approval");
			Map<String, String> params = new HashMap<>();
			params.put("id", requestApprovalId);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getApproval", RequestApprovalDto.class, MqParam.gen(requestApprovalId));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {	}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<Boolean> callIsNeedDeploy(String geaParticipantRefId,Instance instance, Long ewpRequestApprovalId, Long mpRequestApprovalId) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/is-need-deploy");
			Map<String, String> params = new HashMap<>();
			params.put("geaParticipantRefId", geaParticipantRefId);
			params.put("instance", instance.toString());
			if (ewpRequestApprovalId != null) {
				params.put("ewpRequestApprovalId", ewpRequestApprovalId.toString());
			}
			if (mpRequestApprovalId != null) {
				params.put("mpRequestApprovalId", mpRequestApprovalId.toString());
			}
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "isNeedDeploy", Boolean.class, MqParam.gen(params));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<Boolean>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}


	@Override
	public RestfulResponse<String> callSaveDeployment(String geaParticipantRefId, Instance instance) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/save-deployment");
			Map<String, String> params = new HashMap<>();
			params.put("geaParticipantRefId", geaParticipantRefId);
			params.put("instance", instance.toString());
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "saveDeployment", String.class, MqParam.gen(geaParticipantRefId), MqParam.gen(instance));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<String>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<Map<String,String>> callGetParticipantName(List<String> geaParticipantRefId, String instance) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getParticipantName", Map.class, MqParam.gen(geaParticipantRefId), MqParam.gen(Instance.valueOf(instance)));
			} else  {
				Map<String, String> map = new HashMap<>();
				map.put("geaParticipantRefId", StringUtils.join(geaParticipantRefId.toArray(),","));
				map.put("instance", instance);
				setApiKeyParams(map);
				String url = this.getTargetUrl().concat("/remote/v1/get-participant-name");
				return httpClientUtils.httpGet(url, RestfulResponse.class, map);
			}
		} catch (Exception e) {
			logger.error("/remote/v1/get-participant-name", e);
		}
		return null;
	}

	@Override
	public List<ParticipantDto> callGetParticipantByNameOrIdList(String geaParticipantRefId, String participantName, Instance instance) {
		try {
			RestfulResponse<List<ParticipantDto>> response = null;
			Map<String,String> map = new HashMap<>();
			map.put("geaParticipantRefId",geaParticipantRefId);
			map.put("participantName",participantName);
			map.put("instance", instance.name());
			if(super.isMq()){
				response = RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getParticipantByNameOrIdList", ParticipantDto.class, MqParam.gen(map));
			}else {
				String url = this.getTargetUrl().concat("/remote/v1/get-participant-by-name-or-id-list");
				setApiKeyParams(map);
				response = httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<ParticipantDto>>>(){}, map);
			}
			if(Objects.nonNull(response) && response.hasSuccessful()){
				return response.getData();
			}
		}catch (Exception e){
			logger.error("get-participant-by-name-or-id-list", e.getMessage());
		}
		return Collections.emptyList();
	}

}
