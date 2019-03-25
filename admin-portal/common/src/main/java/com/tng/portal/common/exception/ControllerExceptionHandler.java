package com.tng.portal.common.exception;


import com.tng.portal.common.serialize.JsonObjectMapper;
import com.tng.portal.common.util.I18nMessge;
import com.tng.portal.common.vo.rest.RestfulResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Zero on 2016/11/24.
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @Autowired
    private I18nMessge i18nMessge;

    @Autowired
    public HttpServletRequest request;

    @Autowired
    public HttpServletResponse response;


    /**
     * Handler business exception
     * @param e business exception
     */
    @ExceptionHandler(BusinessException.class)
    public void handleBusinessException(BusinessException e){
    	log.error("error", e);
        outputMessage(e);
    }

    private void outputMessage(BusinessException businessException){
        try {
            String errorcode = String.valueOf(businessException.getErrorcode());
            String errormsg = businessException.getErrormsg();
            log.warn("HttpRequest error:{} , errorcode:{} , errormsg:{}" , request.getRequestURL(),errorcode,errormsg);
            String[] templateInput = businessException.getTemplateInput();
            if (templateInput == null || templateInput.length == 0) {
                if(StringUtils.isNotBlank(errormsg)){
                    RestfulResponse restResponse = new RestfulResponse();
                    restResponse.setFailStatus();
                    restResponse.setErrorCode(String.valueOf(businessException.getErrorcode()));
                    restResponse.setMessageEN(errormsg);
                    restResponse.setMessageZhCN(errormsg);
                    restResponse.setMessageZhHK(errormsg);
                    String resultJson = new JsonObjectMapper().writeValueAsString(restResponse);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ServletOutputStream os = response.getOutputStream();
                    os.write(resultJson.getBytes(StandardCharsets.UTF_8.name()));
                }else{
                    RestfulResponse restResponse = i18nMessge.getErrorMessageByErrorCode(errorcode);
                    String resultJson = new JsonObjectMapper().writeValueAsString(restResponse);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ServletOutputStream os = response.getOutputStream();
                    os.write(resultJson.getBytes(StandardCharsets.UTF_8.name()));
                }
            } else {
                RestfulResponse restResponse = i18nMessge.getErrorMessageByErrorCode(errorcode, templateInput);
                String resultJson = new JsonObjectMapper().writeValueAsString(restResponse);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ServletOutputStream os = response.getOutputStream();
                os.write(resultJson.getBytes(StandardCharsets.UTF_8.name()));
            }
        }catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Handler access denied exception
     * @param e access denied exception
     * @throws IOException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e){
        outputMessage(new BusinessException(403));
    }

    /**
     * Handler throwable
     * @param e throwable
     * @throws IOException
     */
    @ExceptionHandler(value = { Error.class, Exception.class, Throwable.class })
    public void handleException(Throwable e){
        log.error("HttpRequest error:"+request.getRequestURL(),e);
        outputMessage(new BusinessException(102000));
    }
}
