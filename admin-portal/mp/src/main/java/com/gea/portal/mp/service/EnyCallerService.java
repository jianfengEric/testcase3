package com.gea.portal.mp.service;

import com.gea.portal.mp.dto.MoneyPoolDetailPageDto;
import com.gea.portal.mp.dto.MoneyPoolTransactionDto;
import com.tng.portal.common.dto.mp.MoneyPoolListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.PageDatas;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;

/**
 *  Used for calling eny Interface to other modules 
 */
public interface EnyCallerService {

	RestfulResponse<PageDatas<MoneyPoolTransactionDto>> getMoneyPoolDetailPageList(MoneyPoolDetailPageDto detailPageDto, Instance instance, Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending);
	PageDatas<MoneyPoolListDto> listMoneyPoolForProcessingStatus(String moneyPoolId, String participantId , String participantName, Integer pageNo, Integer pageSize, String sortBy, Boolean isAscending,
																 Instance instance);

	public RestfulResponse<List<String>> getEnyServiceList();

	public RestfulResponse<List<String>> getEnyTransactionType();
}
