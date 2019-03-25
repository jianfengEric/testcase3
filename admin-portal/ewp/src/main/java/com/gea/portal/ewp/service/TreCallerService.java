package com.gea.portal.ewp.service;

import com.tng.portal.common.dto.tre.ExchangeRateListDto;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling tre Interface to other modules 
 */
public interface TreCallerService {
	
	public RestfulResponse<ExchangeRateListDto> callGetListData(Instance instance);
	
}
