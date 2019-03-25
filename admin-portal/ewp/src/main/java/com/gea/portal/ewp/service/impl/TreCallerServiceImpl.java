package com.gea.portal.ewp.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gea.portal.ewp.service.TreCallerService;
import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

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
	public RestfulResponse<ExchangeRateListDto> callGetListData(Instance instance) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getListData", ExchangeRateListDto.class, MqParam.gen(instance));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/get-list-data");
				Map<String, String> params = new HashMap<>();
				params.put("instance", instance.toString());
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<ExchangeRateListDto>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
	

}
