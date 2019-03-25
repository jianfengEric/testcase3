package com.gea.portal.ewp.service.impl;

import com.gea.portal.ewp.service.DpyCallerService;
import com.tng.portal.common.enumeration.Instance;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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
	public RestfulResponse<Boolean> callIsDeploy(String geaParticipantRefId, Instance deployEnvir) {
		String url = this.getTargetUrl().concat("/remote/v1/is-deploy");
        Map<String, String> param = new HashMap<>();
        param.put("geaParticipantRefId", geaParticipantRefId);
        param.put("deployEnvir", deployEnvir.toString());
        if(super.isMq()){
            try {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "isDeploy", Boolean.class, MqParam.gen(geaParticipantRefId), MqParam.gen(deployEnvir));
            } catch (Exception e) {
                logger.error("is-deploy", e);
            }
        }else {
            setApiKeyParams(param);
            return  httpClientUtils.httpGet(url, RestfulResponse.class, param);
        }
        return null;
	}

}
