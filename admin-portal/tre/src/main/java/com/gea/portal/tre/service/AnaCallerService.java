package com.gea.portal.tre.service;

import com.tng.portal.common.vo.rest.RestfulResponse;

public interface AnaCallerService {
	
	 RestfulResponse<String> callFindBindingId(String bindingAccountId, String srcApplicationCode, String trgApplicationCode);
	
}
