package com.gea.portal.apv.service;

import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling dpy Interface to other modules 
 */
public interface AnaCallerService {
	
	 RestfulResponse<String> callFindBindingId(String bindingAccountId,String srcApplicationCode,String trgApplicationCode);
	
}
