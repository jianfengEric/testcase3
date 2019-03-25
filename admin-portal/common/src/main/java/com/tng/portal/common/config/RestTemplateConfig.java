package com.tng.portal.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Dell on 2018/8/17.
 */
@Configuration
public class RestTemplateConfig {
    @Value("${rest.config.connectTimeout:10000}")
    private int connectTimeout;
    @Value("${rest.config.readTimeout:60000}")
    private int readTimeout;

    @Bean
    public SimpleClientHttpRequestFactory httpClientFactory() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(readTimeout);
        httpRequestFactory.setConnectTimeout(connectTimeout);
        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory httpClientFactory) {
        return new RestTemplate(httpClientFactory);
    }
}
