package com.gea.portal.apv.service.impl;

import com.gea.portal.apv.service.MpCallerService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.mp.MpDetailDto;
import com.tng.portal.common.dto.mp.PoolAdjustmentDto;
import com.tng.portal.common.dto.mp.PoolDepositCashOutDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 	
	@Override
	public RestfulResponse<RequestApprovalDto> callApproval(String approvalRequestId,String requestUserId) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "approval", RequestApprovalDto.class, MqParam.gen(approvalRequestId),MqParam.gen(requestUserId));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/approval");
				Map<String, String> params = new HashMap<>();
				params.put("approvalRequestId", approvalRequestId);
				params.put("requestUserId", requestUserId);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<RequestApprovalDto> callReject(String approvalRequestId,String requestUserId) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "reject", RequestApprovalDto.class, MqParam.gen(approvalRequestId),MqParam.gen(requestUserId));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/reject");
				Map<String, String> params = new HashMap<>();
				params.put("approvalRequestId", approvalRequestId);
				params.put("requestUserId", requestUserId);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {}, params);
			}
		}catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<MpDetailDto> callGetDetail(String geaMoneyPoolRefId, String instance) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-detail");
			Map<String, String> params = new HashMap<>();
			params.put("geaMoneyPoolRefId", geaMoneyPoolRefId);
			params.put("instance", instance);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getDetail", MpDetailDto.class, MqParam.gen(geaMoneyPoolRefId), MqParam.gen(Instance.valueOf(instance)));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<MpDetailDto>>() {}, params);
			}
		}catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<String> callGetApprovalInfo(String mpApvReqId) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-approval-info");
			Map<String, String> param = new HashMap<>();
			param.put("mpApvReqId", mpApvReqId);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getRequestApprovalInfo", String.class, MqParam.gen(mpApvReqId));
			} else  {
				setApiKeyParams(param);
				return httpClientUtils.httpGet(url, RestfulResponse.class, param);
			}
		}catch (Exception e) {
			logger.error("/remote/v1/get-approval-info", e);
		}
        return null;
	}

	@Override
	public RestfulResponse<List<RequestApprovalDto>> callGetApproval(String instance) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-approval");
			Map<String, String> params = new HashMap<>();
			params.put("instance", instance);
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
	public RestfulResponse<RequestApprovalDto> callGetRequestApproval(String requestApprovalId) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-request-approval");
			Map<String, String> params = new HashMap<>();
			params.put("id", requestApprovalId);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getApproval", RequestApprovalDto.class, MqParam.gen(requestApprovalId));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<PoolAdjustmentDto> callGetAdjustment(Long id) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-adjustment");
			Map<String, String> params = new HashMap<>();
			params.put("id", id.toString());
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getAdjustment", PoolAdjustmentDto.class, MqParam.gen(id));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<PoolAdjustmentDto>>() {
				}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<PoolDepositCashOutDto> callGetDepositCashOut(Long id) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-deposit-cash-out");
			Map<String, String> params = new HashMap<>();
			params.put("id", id.toString());
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getDepositCashOut", PoolDepositCashOutDto.class, MqParam.gen(id));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<PoolDepositCashOutDto>>() {}, params);
			}
		}catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	
	@Override
	public RestfulResponse<String> callSaveDeployment(String requestApprovalId) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/save-deployment");
			Map<String, String> params = new HashMap<>();
			params.put("requestApprovalId", requestApprovalId);
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "saveDeployment", String.class, MqParam.gen(Long.valueOf(requestApprovalId)));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<String>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
