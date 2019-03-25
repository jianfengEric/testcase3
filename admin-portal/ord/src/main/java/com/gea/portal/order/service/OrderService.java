package com.gea.portal.order.service;

import com.gea.portal.order.dto.GeaTxQueueDto;
import com.gea.portal.order.dto.GeaTxQueueInDto;
import com.gea.portal.order.dto.OrderDetailDto;
import com.tng.portal.common.vo.PageDatas;

public interface OrderService {
	/***
	 * get all order recored 
	 * @param geaTxQueueInDto
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @param isAscending
	 * @return
	 */
	public PageDatas<GeaTxQueueDto> getOrders(GeaTxQueueInDto geaTxQueueInDto, Integer pageNo,
											  Integer pageSize, String sortBy, Boolean isAscending);

	public OrderDetailDto getOrderDetail(String transferId, String participantId);

}
