package com.tng.portal.log.client.service.impl;

import com.tng.portal.log.client.service.LogClientService;
import com.tng.portal.log.client.task.AsyncTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Zero on 2016/12/13.
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LogClientServiceImpl implements LogClientService{

    private String applicationCode;

    @Autowired
    private AsyncTask asyncTask;

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    /**
     * Save the error log 
     * @param e Java Throwable object
     */
    @Override
    public void logError(Throwable e) {
        asyncTask.logToServer(applicationCode,e);
    }

    /**
     * Save the error log 
     * @param e Java Throwable object
     * @param msg additional error message
     */
    @Override
    public void logError(Throwable e, String msg) {
        asyncTask.logToServer(applicationCode, e, msg);
    }
}
