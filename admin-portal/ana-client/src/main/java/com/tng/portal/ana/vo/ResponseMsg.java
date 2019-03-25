package com.tng.portal.ana.vo;

import com.tng.portal.common.constant.SystemMsg;
import com.tng.portal.common.vo.rest.RestStatus;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Zero on 2016/11/25.
 */
//@Component
public class ResponseMsg implements Serializable{
    @Resource(name = "messageSource")
    protected transient MessageSource messageSource;

    public ResponseMsg(MessageSource messageSource)
    {
        super();
        if (this.messageSource == null)
        {
            this.messageSource = messageSource;
        }
    }

    public RestStatus getResponseMsgByCode(SystemMsg systemMsg){
        String code = systemMsg.getCode();
        String msg = getI18nMessge(code);
        Integer msgCode = Integer.parseInt(code);
        return new RestStatus(msgCode,msg);
    }


    public String getI18nMessgeByCode(SystemMsg msgCode)
    {
       return this.getI18nMessge(msgCode.getCode());
    }

    public String getI18nMessge(String msgCode)
    {
        return this.getI18nMessge(msgCode, null, LocaleContextHolder.getLocale());
    }

    public String getI18nMessge(String msgCode, Object[] args, Locale locale)
    {

        return messageSource.getMessage(msgCode, args, locale);
    }

    /**
     * write error info in stream
     * 
     * @param msg
     * 			error message
     * 
     * @throws IOException
     */
    public RestfulResponse getErrorMessageByErrorCode(SystemMsg msg){
        String errorCode = msg.getCode();
        RestfulResponse response = new RestfulResponse();
        response.setFailStatus();
        response.setErrorCode(errorCode);
        response.setMessageEN(messageSource.getMessage(errorCode, null, Locale.ENGLISH));
        response.setMessageZhCN(messageSource.getMessage(errorCode, null, Locale.SIMPLIFIED_CHINESE));
        response.setMessageZhHK(messageSource.getMessage(errorCode, null, Locale.TRADITIONAL_CHINESE));
        return response;
    }

    /**
     * Get error message by error code
     * 
     * @param errorCode
     * 			error errCode
     * 
     * @throws IOException
     */
    public RestfulResponse getErrorMessageByErrorCode(String errorCode){
        RestfulResponse response = new RestfulResponse();
        response.setFailStatus();
        response.setErrorCode(errorCode);
        response.setMessageEN(messageSource.getMessage(errorCode, null, Locale.ENGLISH));
        response.setMessageZhCN(messageSource.getMessage(errorCode, null, Locale.SIMPLIFIED_CHINESE));
        response.setMessageZhHK(messageSource.getMessage(errorCode, null, Locale.TRADITIONAL_CHINESE));
        return response;
    }


    /**
     * write error info in stream
     *
     * @param msg
     * 			error message code
     *
     * @throws IOException
     */
    public RestfulResponse getErrorMessageByErrorCode(SystemMsg msg, String...templateInput){
        String errorCode = msg.getCode();
        RestfulResponse response = new RestfulResponse();
        response.setFailStatus();
        response.setErrorCode(errorCode);
        response.setMessageEN(messageSource.getMessage(errorCode, templateInput, Locale.ENGLISH));
        response.setMessageZhCN(messageSource.getMessage(errorCode, templateInput, Locale.SIMPLIFIED_CHINESE));
        response.setMessageZhHK(messageSource.getMessage(errorCode, templateInput, Locale.TRADITIONAL_CHINESE));
        return response;
    }
}
