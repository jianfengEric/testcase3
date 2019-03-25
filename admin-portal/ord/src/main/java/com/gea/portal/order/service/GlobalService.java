package com.gea.portal.order.service;

import com.gea.portal.order.dto.BaseServiceDto;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;

public interface GlobalService {
	public RestfulResponse<List<BaseServiceDto>> getOrderStatusList();
	public RestfulResponse<List<com.tng.portal.common.dto.srv.BaseServiceDto>> getServiceList();
	RestfulResponse<List<BaseServiceDto>> getGeaTXStatusList();
}
