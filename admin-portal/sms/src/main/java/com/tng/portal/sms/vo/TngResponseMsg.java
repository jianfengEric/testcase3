package com.tng.portal.sms.vo;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.tng.portal.common.vo.rest.RestStatus;
import com.tng.portal.sms.constant.SystemMsg;

/**
 * Created by Zero on 2016/11/25.
 */
@Component
public class TngResponseMsg extends RestStatus implements Serializable{
    @Resource(name = "messageSource")
    protected transient MessageSource messageSource;

    public TngResponseMsg(MessageSource messageSource)
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
}
