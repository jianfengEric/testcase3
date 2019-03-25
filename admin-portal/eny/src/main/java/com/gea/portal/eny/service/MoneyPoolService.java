package com.gea.portal.eny.service;

import com.gea.portal.eny.dto.MoneyPoolDto;
import com.gea.portal.eny.page.PageDatas;
import com.gea.portal.eny.rest.RestfulResponse;


public interface MoneyPoolService {
	 RestfulResponse<PageDatas<MoneyPoolDto>> getMoneyPool(MoneyPoolDto moneyPoolDto);
}
