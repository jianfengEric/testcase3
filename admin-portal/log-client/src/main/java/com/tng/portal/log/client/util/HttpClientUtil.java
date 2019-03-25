package com.tng.portal.log.client.util;

import com.tng.portal.common.util.JsonUtils;
import com.tng.portal.common.util.PropertiesUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Zero on 2016/12/13.
 */
@Component("logClientHttpClientUtil")
public class HttpClientUtil {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T postSendJson(String url,Class<T> clz,Object obj){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String token = PropertiesUtil.getLogValueByKey("log.client.token");
        headers.add("token",token);
        String jsonString = JsonUtils.toJSon(obj);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonString, headers);
        return  restTemplate.postForObject(url, formEntity, clz);
    }
}
