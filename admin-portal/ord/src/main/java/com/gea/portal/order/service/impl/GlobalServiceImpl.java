package com.gea.portal.order.service.impl;

import com.gea.portal.order.dto.BaseServiceDto;
import com.gea.portal.order.service.GlobalService;
import com.gea.portal.order.util.GeaTXStatusEnum;
import com.gea.portal.order.util.OrderStatusEnum;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 * 
 * @author christ
 * @desc get common Service Type and Order Status
 * @time 2018-12-04
 *
 */
@Service
public class GlobalServiceImpl extends BaseCallerService implements GlobalService {
	@Autowired
	private HttpClientUtils httpClientUtils;

	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.SRV;
	}

	@Override
	public RestfulResponse<List<BaseServiceDto>> getOrderStatusList() {
		RestfulResponse<List<BaseServiceDto>> restfulResponse = new RestfulResponse<>();
		List<BaseServiceDto> list = new ArrayList<>();
		OrderStatusEnum[] s = OrderStatusEnum.values();
		for (int i = 0; i < s.length; i++) {
			OrderStatusEnum status = s[i];
			BaseServiceDto dto = new BaseServiceDto();
			dto.setId(String.valueOf(i + 1));
			dto.setServiceCode(status.name());
			dto.setNameEn(status.getDesc());
			list.add(dto);
		}
		restfulResponse.setData(list);
		return restfulResponse;
	}

	@Override
	public RestfulResponse<List<com.tng.portal.common.dto.srv.BaseServiceDto>> getServiceList() {
		try {
			if (isMq()) {
				return RabbitMQUtil.sendRequestToSOAService(getTargetService(), "listBaseServiceAll", RestfulResponse.class);
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/get-baseService");
				Map<String, String> params = new HashMap<>();
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<com.tng.portal.common.dto.srv.BaseServiceDto>>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public RestfulResponse<List<BaseServiceDto>> getGeaTXStatusList() {
		RestfulResponse<List<BaseServiceDto>> restfulResponse = new RestfulResponse<>();
		List<BaseServiceDto> list = new ArrayList<>();
		GeaTXStatusEnum[] statusEnums = GeaTXStatusEnum.values();
		for (int i = 0; i < statusEnums.length; i++) {
			GeaTXStatusEnum geaTXStatus = statusEnums[i];
			BaseServiceDto dto = new BaseServiceDto();
			dto.setId(String.valueOf(i + 1));
			dto.setServiceCode(geaTXStatus.getCode());
			dto.setNameEn(geaTXStatus.getDesc());
			list.add(dto);
		}
		restfulResponse.setData(list);
		return restfulResponse;
	}


}
