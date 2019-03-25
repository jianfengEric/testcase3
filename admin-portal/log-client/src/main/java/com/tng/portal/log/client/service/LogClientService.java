package com.tng.portal.log.client.service;

/**
 * Created by Zero on 2016/12/13.
 */
public interface LogClientService {
    void logError(Throwable e);
    void logError(Throwable e,String msg);
}
