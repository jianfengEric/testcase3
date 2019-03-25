package com.gea.portal.mp.service;

import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling dpy Interface to other modules 
 */
public interface DpyCallerService {
	
	public RestfulResponse<Boolean> callIsDeploy(String geaParticipantRefId,Instance deployEnvir);
	
}
