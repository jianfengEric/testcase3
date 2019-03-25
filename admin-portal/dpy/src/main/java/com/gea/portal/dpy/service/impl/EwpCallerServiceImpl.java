package com.gea.portal.dpy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gea.portal.dpy.service.EwpCallerService;
import com.tng.portal.common.enumeration.DeployStatus;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.DateUtils;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;

/**
 *  Used for calling ewp Interface to other modules 
 */
@Service
@Transactional
public class EwpCallerServiceImpl extends BaseCallerService implements EwpCallerService {

    @Autowired
    private HttpClientUtils httpClientUtils;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.EWP;
	}

	@Override
	public RestfulResponse<String> callDeployment(Long deployRefId,DeployStatus status,Date scheduleDeployDate,String deployVersionNo) {
		try {
			if (super.isMq()) {
				return RabbitMQUtil.sendRequestToSOAService(super.getTargetService(), "synchDeployment", String.class, MqParam.gen(deployRefId), MqParam.gen(status), MqParam.gen(scheduleDeployDate), MqParam.gen(deployVersionNo));
			} else  {
				String url = this.getTargetUrl().concat("/remote/v1/synch-deployment");
				Map<String, String> params = new HashMap<>();
				params.put("deployRefId", String.valueOf(deployRefId));
				params.put("status", status.getValue());
				params.put("scheduleDeployDate", DateUtils.formatDate(scheduleDeployDate));
				params.put("deployVersionNo", deployVersionNo);
				setApiKeyParams(params);
				return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<String>>() {}, params);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}
}
