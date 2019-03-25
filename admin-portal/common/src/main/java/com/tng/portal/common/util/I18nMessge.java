package com.tng.portal.common.util;

import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Zero on 2016/11/25.
 */
@Component
public class I18nMessge implements Serializable{
    @Resource(name = "messageSource")
    protected transient MessageSource messageSource;

    public I18nMessge(MessageSource messageSource)
    {
        super();
        if (this.messageSource == null)
        {
            this.messageSource = messageSource;
        }
    }

    /**
     * Get error message by error code
     * @param errorcode error errCode
     * @return
     */
    public RestfulResponse getErrorMessageByErrorCode(String errorcode){
        RestfulResponse response = new RestfulResponse();
        response.setFailStatus();
        response.setErrorCode(errorcode);
        response.setMessageEN(messageSource.getMessage(errorcode, null, Locale.ENGLISH));
        response.setMessageZhCN(messageSource.getMessage(errorcode, null, Locale.SIMPLIFIED_CHINESE));
        response.setMessageZhHK(messageSource.getMessage(errorcode, null, Locale.TRADITIONAL_CHINESE));
        return response;
    }

    /**
     * write error info in stream
     * @param errorcode error message code
     * @param templateInput
     * @return
     */
    public RestfulResponse getErrorMessageByErrorCode(String errorcode, String...templateInput){
        RestfulResponse response = new RestfulResponse();
        response.setFailStatus();
        response.setErrorCode(errorcode);
        response.setMessageEN(messageSource.getMessage(errorcode, templateInput, Locale.ENGLISH));
        response.setMessageZhCN(messageSource.getMessage(errorcode, templateInput, Locale.SIMPLIFIED_CHINESE));
        response.setMessageZhHK(messageSource.getMessage(errorcode, templateInput, Locale.TRADITIONAL_CHINESE));
        return response;
    }

    public String getEnMessage(String code,String... param){
        return messageSource.getMessage(code, param, Locale.ENGLISH);
    }

    public String getZhCNMessage(String code,String... param){
        return messageSource.getMessage(code, param, Locale.SIMPLIFIED_CHINESE);
    }

    public String getZhHKMessage(String code,String... param){
        return messageSource.getMessage(code, param, Locale.TRADITIONAL_CHINESE);
    }

}
