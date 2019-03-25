package com.gea.portal.dpy.service;

import java.util.Date;

import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling mp Interface to other modules 
 */
public interface SrvCallerService {

	RestfulResponse<String> callDeployment(Long deployRefId, DeployStatus status,Date scheduleDeployDate,String deployVersionNo);
}
