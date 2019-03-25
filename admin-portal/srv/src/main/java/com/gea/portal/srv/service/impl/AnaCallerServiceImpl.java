package com.gea.portal.srv.service.impl;


import com.gea.portal.srv.service.AnaCallerService;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 *  Used for calling ana Interface to other modules 
 */
@Service
@Transactional
public class AnaCallerServiceImpl extends BaseCallerService implements AnaCallerService {


    @Autowired
    private HttpClientUtils httpClientUtils;

 	@Override
	protected String getTargetModule() {
		return ApplicationContext.Modules.ANA;
	}

    @Override
    public RestfulResponse<String> callFindBindingId(String bindingAccountId,String srcApplicationCode,String trgApplicationCode) {
        try {
            String url = this.getTargetUrl().concat("/remote/account/find-binding-id");
            Map<String, String> param = new HashMap<>();
            param.put("bindingAccountId", bindingAccountId);
            param.put("srcApplicationCode", srcApplicationCode);
            param.put("trgApplicationCode", trgApplicationCode);
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "findBindingId", String.class, MqParam.gen(bindingAccountId), MqParam.gen(srcApplicationCode), MqParam.gen(trgApplicationCode));
            } else  {
                setApiKeyParams(param);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<String>>() {}, param);
            }
        } catch (Exception e) {
            logger.error("/remote/account/find-binding-id", e);
        }
        return null;
    }

}
