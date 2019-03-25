package com.tng.portal.log.client.task;

import com.tng.portal.common.util.MqParam;
import com.tng.portal.common.util.PropertiesUtil;
import com.tng.portal.common.util.RabbitMQUtil;
import com.tng.portal.common.vo.log.request.LogInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Zero on 2016/12/16.
 */
@Component
public class AsyncTask {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTask.class);
    private static String EXCEPTION = "Exception";
    private static final String SERVICE_NAME = PropertiesUtil.getLogValueByKey("log.service.name");


    @Async
    public void logToServer(String aplicationCode,Throwable e,String msg){
        try {
            LogInfoDto logDto = buildLoginfo(e,msg,aplicationCode);
            handleLog(logDto);
        }catch (Exception el){
            logger.error(EXCEPTION,el);
        }
    }

    @Async
    public void logToServer(String aplicationCode,Throwable e){
        try {
            LogInfoDto logDto = buildLoginfo(e,aplicationCode);
            handleLog(logDto);
        }catch (Exception e1){
            logger.error(EXCEPTION,e1);
        }
    }

    private LogInfoDto buildLoginfo(Throwable e,String applicationCode){
        LogInfoDto dto = null;
        StackTraceElement[] traceElements = e.getStackTrace();
        if(traceElements.length>0){
            dto = new LogInfoDto();
            StackTraceElement traceElement = traceElements[0];
            dto.setExceptionMethod(traceElement.getMethodName());
            dto.setExceptionClass(traceElement.getClassName());
            dto.setExceptionLine(traceElement.getLineNumber());
            dto.setExceptionMsg(e.getMessage());
            String type = e.getClass().getTypeName();
            dto.setExceptionType(type);
            dto.setApplicationCode(applicationCode);
        }
        return dto;
    }

    private  LogInfoDto buildLoginfo(Throwable e,String msg,String applicationCode){
        LogInfoDto dto = null;
        StackTraceElement[] traceElements = e.getStackTrace();
        if(traceElements.length>0){
            dto = new LogInfoDto();
            StackTraceElement traceElement = traceElements[0];
            dto.setExceptionMethod(traceElement.getMethodName());
            dto.setExceptionClass(traceElement.getClassName());
            dto.setExceptionLine(traceElement.getLineNumber());
            dto.setExceptionMsg(msg);
            String type = e.getClass().getGenericSuperclass().getTypeName();
            dto.setExceptionType(type);
            dto.setApplicationCode(applicationCode);
        }
        return dto;
    }

    private void handleLog(LogInfoDto dto){
        if(null!=dto){
            String methodName = "log";
            try {
                RabbitMQUtil.sendRequestToSOAService(SERVICE_NAME, methodName, MqParam.gen(dto));
            } catch (Exception e) {
                logger.error(EXCEPTION,e);
            }
        }
    }
}
