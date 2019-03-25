package com.gea.portal.mp.service.impl;

import com.gea.portal.mp.service.SrvCallerService;
import com.tng.portal.common.dto.srv.BaseServiceDto;
import com.tng.portal.common.exception.BusinessException;
import com.tng.portal.common.service.BaseCallerService;
import com.tng.portal.common.util.ApplicationContext;
import com.tng.portal.common.util.HttpClientUtils;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2018/12/19.
 */
@Service
public class SrvCallerServiceImpl extends BaseCallerService implements SrvCallerService {

    @Autowired
    private HttpClientUtils httpClientUtils;

    @Override
    protected String getTargetModule() {
        return ApplicationContext.Modules.SRV;
    }

    @Override
    public RestfulResponse<List<BaseServiceDto>> getAllService() {
        try {
            if (super.isMq()) {
                return RabbitMQUtil.sendRequestToSOAService( super.getTargetService(), "listBaseServiceAll", RestfulResponse.class);
            } else  {
                String url = this.getTargetUrl().concat("/remote/v1/get-baseService");
                Map<String, String> params = new HashMap<>();
                setApiKeyParams(params);
                return httpClientUtils.httpGet(url, new ParameterizedTypeReference<RestfulResponse<List<BaseServiceDto>>>() {}, params);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

}
