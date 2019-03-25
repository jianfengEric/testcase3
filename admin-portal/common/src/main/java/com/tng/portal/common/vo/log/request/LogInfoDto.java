package com.tng.portal.common.vo.log.request;

import java.io.Serializable;

/**
 * Created by Zero on 2016/12/13.
 */
public class LogInfoDto implements Serializable{
    private String applicationCode;
    private String exceptionClass;
    private String exceptionMethod;
    private Integer exceptionLine;
    private String exceptionMsg;
    private String exceptionType;
    private String createdDate;

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMethod() {
        return exceptionMethod;
    }

    public void setExceptionMethod(String exceptionMethod) {
        this.exceptionMethod = exceptionMethod;
    }

    public Integer getExceptionLine() {
        return exceptionLine;
    }

    public void setExceptionLine(Integer exceptionLine) {
        this.exceptionLine = exceptionLine;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
