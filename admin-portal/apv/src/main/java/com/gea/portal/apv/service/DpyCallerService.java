package com.gea.portal.apv.service;

import com.tng.portal.common.dto.DeployDetailDto;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.vo.rest.RestfulResponse;

import java.util.List;
import java.util.Map;

/**
 *  Used for calling dpy Interface to other modules 
 */
public interface DpyCallerService {
	RestfulResponse<String> callDeploy(DeployDetailDto deploydetail);
	
	RestfulResponse<String> callDeployMp(DeployDetailDto deploydetail);
	
	RestfulResponse<String> callDeploySrv(DeployDetailDto deploydetail);
	
	RestfulResponse<String> callDeployTre(DeployDetailDto deploydetail);
	
	RestfulResponse<Boolean> callIsDeploy(String geaParticipantRefId,Instance deployEnvir);

	RestfulResponse<Map<Long,Map<String, String>>> callDeploymentInfo(List<Long> ids);

	RestfulResponse<Integer> callHasPending(DeployType deployType, Instance deployEnvir);
}
