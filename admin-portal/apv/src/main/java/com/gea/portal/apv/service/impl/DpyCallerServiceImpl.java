package com.gea.portal.apv.service.impl;

import com.gea.portal.apv.service.DpyCallerService;
import com.tng.portal.common.dto.DeployDetailDto;
import com.tng.portal.common.enumeration.DeployType;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.*;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *  Used for calling dpy Interface to other modules 
 */
@Service
@Transactional
public class DpyCallerServiceImpl extends BaseCallerService implements DpyCallerService{

    @Autowired
    private HttpClientUtils httpClientUtils;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.DPY;
	}

    @Override
    public RestfulResponse<String> callDeploy(DeployDetailDto deploydetail) {
        try {
            if(super.isMq()){
                String jsonString = JsonUtils.toJSon(deploydetail);
                return RabbitMQUtil.sendRequestToSOAService(super.getTargetService(), "deployPt", String.class, MqParam.gen(jsonString));
            }else {
                String url = this.getTargetUrl().concat("/remote/v1/deploy-pt");
                Map<String, String> param = new HashMap<>();
                setApiKeyParams(param);
                return httpClientUtils.postSendJson(url, RestfulResponse.class, deploydetail, param);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

	@Override
	public RestfulResponse<String> callDeployMp(DeployDetailDto deploydetail) {
        try {
            if (super.isMq()) {
                String jsonString = JsonUtils.toJSon(deploydetail);
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "deployMp", String.class, MqParam.gen(jsonString));
            } else {
                String url = this.getTargetUrl().concat("/remote/v1/deploy-mp");
                Map<String, String> param = new HashMap<>();
                setApiKeyParams(param);
                return httpClientUtils.postSendJson(url, RestfulResponse.class, deploydetail, param);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
	
	@Override
	public RestfulResponse<String> callDeploySrv(DeployDetailDto deploydetail) {
        try {
            if (super.isMq()) {
                String jsonString = JsonUtils.toJSon(deploydetail);
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "deploySrv", String.class, MqParam.gen(jsonString));
            } else {
                String url = this.getTargetUrl().concat("/remote/v1/deploy-srv");
                Map<String, String> param = new HashMap<>();
                setApiKeyParams(param);
                return httpClientUtils.postSendJson(url, RestfulResponse.class, deploydetail, param);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
	
	@Override
	public RestfulResponse<String> callDeployTre(DeployDetailDto deploydetail) {
        try {
            if (super.isMq()) {
                String jsonString = JsonUtils.toJSon(deploydetail);
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "deployTre", String.class, MqParam.gen(jsonString));
            } else {
                String url = this.getTargetUrl().concat("/remote/v1/deploy-tre");
                Map<String, String> param = new HashMap<>();
                setApiKeyParams(param);
                return httpClientUtils.postSendJson(url, RestfulResponse.class, deploydetail, param);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

	@Override
	public RestfulResponse<Boolean> callIsDeploy(String geaParticipantRefId, Instance deployEnvir) {
        try {
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "isDeploy", Boolean.class, MqParam.gen(geaParticipantRefId), MqParam.gen(deployEnvir));
            } else {
            	String url = this.getTargetUrl().concat("/remote/v1/is-deploy");
            	Map<String, String> param = new HashMap<>();
            	param.put("geaParticipantRefId", geaParticipantRefId);
            	param.put("deployEnvir", deployEnvir.toString());
            	setApiKeyParams(param);
                return httpClientUtils.httpGet(url, RestfulResponse.class, param);
            }
        } catch (Exception e) {
            logger.error("is-deploy", e);
        }
        return null;
    }

    @Override
    public RestfulResponse<Map<Long,Map<String, String>>> callDeploymentInfo(List<Long> ids) {
        try {
            if(super.isMq()){
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "getDeploymentInfo", Map.class, MqParam.gen(List.class,ids));
            }else {
            	Map<String, String> map = new HashMap<>();
            	map.put("ids", StringUtils.join(ids.toArray(),","));
            	setApiKeyParams(map);
                String url = this.getTargetUrl().concat("/remote/v1/get-deployment-info");
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<Map<Long,Map<String, String>>>>() {}, map);
            }
        } catch (Exception e) {
            logger.error("get-deployment-info", e);
        }
        return null;
    }

	@Override
	public RestfulResponse<Integer> callHasPending(DeployType deployType, Instance deployEnvir) {
		try {
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "hasPending", Integer.class, MqParam.gen(deployType), MqParam.gen(deployEnvir));
            } else {
            	String url = this.getTargetUrl().concat("/remote/v1/has-pending");
            	Map<String, String> param = new HashMap<>();
            	param.put("deployType", deployType.toString());
            	param.put("deployEnvir", deployEnvir.toString());
            	setApiKeyParams(param);
                return httpClientUtils.httpGet(url, RestfulResponse.class, param);
            }
        } catch (Exception e) {
            logger.error("has-deploy", e);
        }
        return null;
	}
}
