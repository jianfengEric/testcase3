package com.tng.portal.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.tng.portal.common.util.PropertiesUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger
@EnableSwagger2
@EnableWebMvc
@ComponentScan("com.tng.portal.sms.controller")
public class SwaggerConfig {

    private static String config = PropertiesUtil.getAppValueByKey("swagger.config");

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(Boolean.valueOf(config))
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.tng.portal.sms.controller"))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("sms API")
                .description("description")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
