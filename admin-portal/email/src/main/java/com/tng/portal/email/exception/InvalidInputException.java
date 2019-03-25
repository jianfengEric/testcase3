package com.tng.portal.email.exception;

/**
 * Created by Zero on 2016/11/14.
 */
public class InvalidInputException extends RuntimeException{

    private Integer errorCode;
    private String errorMsg;

    public InvalidInputException(){

    }

    public InvalidInputException(Integer errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
