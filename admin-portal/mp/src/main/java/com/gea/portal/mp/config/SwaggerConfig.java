package com.gea.portal.mp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan("com.tng.portal.ana.controller")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        ParameterBuilder consumerPar = new ParameterBuilder();
        ParameterBuilder apiKeyPar = new ParameterBuilder();
        String type = "string";
        String header = "header";
        tokenPar.name("token").description("token").modelRef(new ModelRef(type)).parameterType(header).required(false).build();
        consumerPar.name("consumer").description("consumer").modelRef(new ModelRef(type)).parameterType(header).required(false).build();
        apiKeyPar.name("apiKey").description("apiKey").modelRef(new ModelRef(type)).parameterType(header).required(false).build();
        
        List<Parameter> pars = new ArrayList<>();
        pars.add(tokenPar.build());
        pars.add(consumerPar.build());
        pars.add(apiKeyPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .enable(Boolean.valueOf(config))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MP API")
                .description("description")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
