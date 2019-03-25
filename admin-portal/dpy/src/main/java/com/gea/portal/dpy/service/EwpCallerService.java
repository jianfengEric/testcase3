package com.gea.portal.dpy.service;

import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.Date;

/**
 *  Used for calling ewp Interface to other modules 
 */
public interface EwpCallerService {

	RestfulResponse<String> callDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo);
}
