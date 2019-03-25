package com.tng.portal.common.exception;

/**
 * Created by Roger on 2017/8/23.
 */
public class BusinessException extends RuntimeException{

    private int errorcode;
    private String errormsg;
    private String[] templateInput;

    public BusinessException() { }

    public BusinessException(int errorcode) {
        this.errorcode = errorcode;
    }
    public BusinessException(int errorcode,String errormsg){
        this.errorcode = errorcode;
        this.errormsg = errormsg;
    }

    public BusinessException(String errormsg) {
        this.errormsg = errormsg;
    }


    public BusinessException(String errormsg, Throwable cause) {
        super(errormsg, cause);
        this.errormsg = errormsg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BusinessException(int errorcode, String[] templateInput) {
        this.errorcode = errorcode;
        this.templateInput = templateInput;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String[] getTemplateInput() {
        return templateInput;
    }

    public void setTemplateInput(String[] templateInput) {
        this.templateInput = templateInput;
    }
}
