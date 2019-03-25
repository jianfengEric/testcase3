package com.gea.portal.mp.service;

import com.tng.portal.common.vo.rest.RestfulResponse;

public interface AnaCallerService {
	
	 RestfulResponse<String> callFindBindingId(String bindingAccountId, String srcApplicationCode, String trgApplicationCode);
	
}
