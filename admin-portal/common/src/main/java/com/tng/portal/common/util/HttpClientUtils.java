package com.tng.portal.common.util;

import com.tng.portal.common.exception.BusinessException;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Zero on 2016/11/30.
 */
@Component("httpClientUtils")
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    private static final String ACCEPT_ENCODING = "gzip, deflate, br";

    private static final String ACCEPT_LANGUAGE = "zh-CN,zh;q=0.8,en;q=0.6";
    
    private static final String ERR_MSG = "url:{} clazz:{} token:{} prams:{}";
    
    private static final String STR_ACCEPT = "Accept";
    private static final String STR_API_KEY = "apiKey";
    private static final String STR_CONSUMER = "consumer";
    private static final String STR_TOKEN = "token";
    private static final String STR_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String STR_ACCEPT_LANGUAGE = "Accept-Language";

    @Autowired
    private RestTemplate restTemplate;

    public <T> T postSendJson(String url,Class<T> clz,Object obj){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(CONTENT_TYPE);
        headers.setContentType(type);
        headers.add(STR_ACCEPT, MediaType.APPLICATION_JSON.toString());
        String jsonString = JsonUtils.toJSon(obj);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonString, headers);
        return  restTemplate.postForObject(url, formEntity, clz);
    }
    
    public <T> T postSendJson(String url,ParameterizedTypeReference<T> type,Object obj){
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType(CONTENT_TYPE);
        headers.setContentType(mediaType);
        headers.add(STR_ACCEPT, MediaType.APPLICATION_JSON.toString());
        String jsonString = JsonUtils.toJSon(obj);
        HttpEntity<String> formEntity = new HttpEntity<>(jsonString, headers);
        return  restTemplate.exchange(url, HttpMethod.POST, formEntity, type).getBody();
    }

    public <T> T postSendJson(String url,Class<T> clz,Object obj,Map<String,String> urlVariables){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType(CONTENT_TYPE);
        headers.setContentType(type);
        headers.add(STR_ACCEPT, MediaType.APPLICATION_JSON.toString());
        String jsonString = JsonUtils.toJSon(obj);

        if (urlVariables != null && urlVariables.size() > 0) {
            Iterator<Map.Entry<String, String>> it = urlVariables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = it.next();
                if (pair.getKey().equals(STR_API_KEY) || pair.getKey().equals(STR_CONSUMER)){
                    headers.add(pair.getKey(),pair.getValue());
                }
            }
        }
        HttpEntity<String> formEntity = new HttpEntity<>(jsonString, headers);
        return  restTemplate.postForObject(url, formEntity, clz);
    }

    public  <T> T getObject(String url, Class<T> responseType, Map<String,String> urlVariables,String token){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        if(urlVariables != null && urlVariables.size() > 0){
            Iterator<Map.Entry<String,String >> it = urlVariables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = it.next();
                List<String> values = new ArrayList<>();
                values.add(pair.getValue());
                params.put(pair.getKey(),values);
            }
        }
        UriComponents uriComponents=UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(STR_TOKEN,token);
        HttpEntity formEntity = new HttpEntity(headers);
        return restTemplate.exchange(uriComponents.toString(), HttpMethod.GET,formEntity,responseType).getBody();
    }

    public MultiValueMap<String,String> objectToMap(Object obj) throws IllegalArgumentException, IllegalAccessException {
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();

        Class clz =  obj.getClass();
        Field[] fields =  clz.getDeclaredFields();
        Field.setAccessible(fields, true);
        for(Field field:fields){
            List<String> values = new ArrayList<>();
            Object fieldValue=field.get(obj);
            if(null!=fieldValue){
                values.add(fieldValue.toString());
                multiValueMap.put(field.getName(), values);
            }
        }

        return multiValueMap;
    }

    public <T> T httpPost(String url, Object prams, Class<T> responseType, String token, Map<String, String> apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType(CONTENT_TYPE);
            headers.setContentType(type);
            headers.add(STR_ACCEPT, MediaType.APPLICATION_JSON.toString());
            headers.add(STR_ACCEPT_ENCODING, ACCEPT_ENCODING);
            headers.add(STR_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE);
            headers.add(STR_TOKEN, token);
            if(MapUtils.isNotEmpty(apiKey)) {
                apiKey.entrySet().stream().forEach(item -> headers.add(item.getKey(), item.getValue()));
            }
            String jsonString = JsonUtils.toJSon(prams);
            HttpEntity<String> formEntity = new HttpEntity(jsonString, headers);
            return restTemplate.postForObject(url, formEntity, responseType);
        }catch (Exception e){
            logger.error("url:{} prams:{} responseType:{} token:{}",url,prams,responseType,token,e);
        }
        return null;
    }

    public <T> T postObject(String url,Class<T> responseType,Object obj){
        try {
            MultiValueMap<String,String> params = objectToMap(obj);
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
            String response = restTemplate.postForEntity(uriComponents.toString(),null,String.class).getBody();
            return JsonUtils.readValue(response, responseType);
        } catch (Exception e) {
            logger.error("url:{} responseType:{} obj:{}",url,responseType,obj,e);
            throw new BusinessException("http request exception!");
        }
    }


    public <T> T httpGet(String url, Class<T> clazz, String token, Object prams, Map<String, String> apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(STR_TOKEN, token);
            if(MapUtils.isNotEmpty(apiKey)) {
                apiKey.entrySet().stream().forEach(item -> headers.add(item.getKey(), item.getValue()));
            }
            String pramsJson = JsonUtils.toJSon(prams);
            HttpEntity formEntity = new HttpEntity(pramsJson, headers);
            return restTemplate.exchange(url, HttpMethod.GET, formEntity, clazz).getBody();
        }catch (Exception e){
            logger.error(ERR_MSG,url,clazz,token,prams,e);
        }
        return null;
    }

    public <T> T httpDelete(String url, Class<T> clazz, String token, Object prams, Map<String, String> apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType(CONTENT_TYPE);
            headers.setContentType(type);
            headers.add(STR_ACCEPT, MediaType.APPLICATION_JSON.toString());
            headers.add(STR_ACCEPT_ENCODING, ACCEPT_ENCODING);
            headers.add(STR_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE);
            headers.add(STR_TOKEN, token);
            if(MapUtils.isNotEmpty(apiKey)) {
                apiKey.entrySet().stream().forEach(item -> headers.add(item.getKey(), item.getValue()));
            }
            String pramsJson = JsonUtils.toJSon(prams);
            HttpEntity formEntity = new HttpEntity(pramsJson, headers);
            return restTemplate.exchange(url, HttpMethod.DELETE, formEntity, clazz).getBody();
        }catch (Exception e){
            logger.error(ERR_MSG,url,clazz,token,prams,e);
        }
        return null;
    }

    public <T> T httpPut(String url, Class<T> clazz, String token, Object prams, Map<String, String> apiKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType(CONTENT_TYPE);
            headers.setContentType(type);
            headers.add(STR_ACCEPT, MediaType.APPLICATION_JSON.toString());
            headers.add(STR_ACCEPT_ENCODING, ACCEPT_ENCODING);
            headers.add(STR_ACCEPT_LANGUAGE, ACCEPT_LANGUAGE);
            headers.add(STR_TOKEN, token);
            if(MapUtils.isNotEmpty(apiKey)) {
                apiKey.entrySet().stream().forEach(item -> headers.add(item.getKey(), item.getValue()));
            }
            String pramsJson = JsonUtils.toJSon(prams);
            HttpEntity formEntity = new HttpEntity(pramsJson, headers);
            logger.error(ERR_MSG,url,clazz,token,pramsJson);
            return restTemplate.exchange(url, HttpMethod.PUT, formEntity, clazz).getBody();
        }catch (Exception e){
            logger.error(ERR_MSG,url,clazz,token,prams,e);
        }
        return null;
    }

    public <T> T httpGet(String url, Class<T> responseType, Map<String, String> urlVariables){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        if (urlVariables != null && urlVariables.size() > 0) {
            Iterator<Map.Entry<String, String>> it = urlVariables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = it.next();
                List<String> values = new ArrayList<>();
                 if (pair.getKey().equals(STR_API_KEY)){
                    headers.add(pair.getKey(),pair.getValue());
                } else if(pair.getKey().equals(STR_CONSUMER)){
                    headers.add(pair.getKey(),pair.getValue());
                }else {
                     values.add(pair.getValue());
                     params.put(pair.getKey(), values);
                 }
            }
        }
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
        HttpEntity formEntity = new HttpEntity(headers);
        return restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, formEntity, responseType).getBody();
    }

    /**
     * http get Request to support generic 
     * @param url
     * @param type  Parameterized type 
     * @param urlVariables  Interface parameters 
     * @param <T>  Return type, you need to provide a parameter free constructor. 
     * @return
     * @throws Exception
     */
    public <T> T httpGet(String url, ParameterizedTypeReference<T> type, Map<String, String> urlVariables) throws Exception {
        return httpGet(url,type,urlVariables,"");
    }

    /**
     * http get Request to support generic 
     * @param url
     * @param type  Parameterized type 
     * @param urlVariables  Interface parameters 
     * @param token
     * @param <T>  Return type, you need to provide a parameter free constructor. 
     * @return
     * @throws Exception
     */
    public <T> T httpGet(String url, ParameterizedTypeReference<T> type, Map<String, String> urlVariables,String token) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        if (urlVariables != null && urlVariables.size() > 0) {
            Iterator<Map.Entry<String, String>> it = urlVariables.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = it.next();
                 if (pair.getKey().equals(STR_API_KEY)){
                    headers.add(pair.getKey(),pair.getValue());
                } else if(pair.getKey().equals(STR_CONSUMER)){
                    headers.add(pair.getKey(),pair.getValue());
                }else {
                	List<String> values = new ArrayList<>();
                    values.add(pair.getValue());
                    params.put(pair.getKey(), values);
                }
            }
        }
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();

        if(!StringUtils.isEmpty(token)) {
            headers.add(STR_TOKEN, token);
        }
        HttpEntity formEntity = new HttpEntity(headers);
        return restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, formEntity, type).getBody();
    }

    public <T> String postObjectForString(String url,Class<T> responseType,Map<String,String> params){
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(mapToMultiValueMap(params)).build();
            String encodeUrl =  uriComponents.toUriString();
            return restTemplate.postForEntity(encodeUrl,null,String.class).getBody();
        } catch (Exception e) {
            logger.error("url:{} responseType:{} prams:{}",url,responseType,params,e);
        }
        return null;
    }
    private MultiValueMap<String,String> mapToMultiValueMap(Map<String,String> map){
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        return multiValueMap;
    }
}
