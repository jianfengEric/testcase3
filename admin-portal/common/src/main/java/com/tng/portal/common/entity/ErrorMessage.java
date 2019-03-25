package com.tng.portal.common.entity;

import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.context.MessageSource;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Zero on 2017/2/6.
 */
public class ErrorMessage implements Serializable{

    protected transient MessageSource messageSource;

    public ErrorMessage(MessageSource messageSource)
    {

        this.messageSource = messageSource;

    }

    public ErrorMessage(){}

    public RestfulResponse getErrorMessageByErrorCode(String errorCode){
        RestfulResponse response = new RestfulResponse();
        response.setFailStatus();
        response.setErrorCode(errorCode);
        response.setMessageEN(messageSource.getMessage(errorCode, null, Locale.ENGLISH));
        response.setMessageZhCN(messageSource.getMessage(errorCode, null, Locale.SIMPLIFIED_CHINESE));
        response.setMessageZhHK(messageSource.getMessage(errorCode, null, Locale.TRADITIONAL_CHINESE));
        return response;
    }
}
