package com.gea.portal.apv.service.impl;

import com.gea.portal.apv.service.TreCallerService;
import com.tng.portal.common.dto.RequestApprovalDto;
import com.tng.portal.common.dto.tre.ExchangeRateFileDto;
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
import java.util.Map;

/**
 *  Used for calling tre Interface to other modules 
 */
@Service
@Transactional
public class TreCallerServiceImpl extends BaseCallerService implements TreCallerService{



    @Autowired
    private HttpClientUtils httpClientUtils;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.TRE;
	}
 	
 	
	@Override
	public RestfulResponse<ExchangeRateFileDto> callGetApproval(String instance) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getRequestApproval", ExchangeRateFileDto.class, MqParam.gen(Instance.valueOf(instance)));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/get-approval");
				Map<String, String> params = new HashMap<>();
				params.put("instance", instance);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<ExchangeRateFileDto>>() {}, params);
			}
		}catch (Exception e) {
			throw new BusinessException(e);
		}
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
	
	@Override
	public RestfulResponse<ExchangeRateFileDto> callGetDetail(Long exchRateFileId, Instance instance) {
		try {
			String url = this.getTargetUrl().concat("/remote/v1/get-detail");
			Map<String, String> params = new HashMap<>();
			params.put("exchRateFileId", exchRateFileId.toString());
			params.put("instance", instance.toString());
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getDetail", ExchangeRateFileDto.class, MqParam.gen(exchRateFileId.toString()), MqParam.gen(instance));
			} else  {
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<ExchangeRateFileDto>>() {
				}, params);
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
				params.put("approvalRequestId", id);
				params.put("requestUserId", requestUserId);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<RequestApprovalDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

}
