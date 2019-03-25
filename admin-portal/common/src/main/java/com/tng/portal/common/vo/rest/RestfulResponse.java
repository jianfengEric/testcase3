package com.tng.portal.common.vo.rest;

import java.io.Serializable;

/**
 * Created by Zero on 2017/2/6.
 */
public class RestfulResponse<T> implements Serializable{
    private static final String SUCCESS="success";
    private static final String FAIL="fail";

    private String status;
    private String errorCode;
    private String messageEN;
    private String messageZhHK;
    private String messageZhCN;
    private T data;

    public RestfulResponse() {
        this.setSuccessStatus();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessageEN() {
        return messageEN;
    }

    public void setMessageEN(String messageEN) {
        this.messageEN = messageEN;
    }

    public String getMessageZhHK() {
        return messageZhHK;
    }

    public void setMessageZhHK(String messageZhHK) {
        this.messageZhHK = messageZhHK;
    }

    public String getMessageZhCN() {
        return messageZhCN;
    }

    public void setMessageZhCN(String messageZhCN) {
        this.messageZhCN = messageZhCN;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccessStatus(){
        this.status = SUCCESS;
        this.messageEN = SUCCESS;
        this.messageZhHK = "\u6210\u529f";
        this.messageZhCN = "\u6210\u529f";
    }

    public void setFailStatus(){
        this.status = FAIL;
        this.messageEN = FAIL;
        this.messageZhHK = "\u5931\u8d25";
        this.messageZhCN = "\u5931\u6557";
    }

    public boolean hasSuccessful(){
        return SUCCESS.equals(status);
    }

    public boolean hasFailed(){
        return FAIL.equals(status);
    }

    /**
     *  parameter data Not for null Set it to a successful state. null Set to failure state 
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestfulResponse<T> ofData(T data){
        RestfulResponse<T> restfulResponse = new RestfulResponse<>();
        if (data!=null) {
            restfulResponse.setData(data);
        }
        restfulResponse.setSuccessStatus();
        return restfulResponse;
    }

    /**
     *
     * @return
     */
    public static RestfulResponse nullData(){
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setSuccessStatus();
        return restfulResponse;
    }

    public static RestfulResponse failData(){
        RestfulResponse restfulResponse = new RestfulResponse();
        restfulResponse.setFailStatus();
        return restfulResponse;
    }

    @Override
    public String toString() {
        return "RestfulResponse{" +
                "status='" + status + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", messageEN='" + messageEN + '\'' +
                ", messageZhHK='" + messageZhHK + '\'' +
                ", messageZhCN='" + messageZhCN + '\'' +
                ", data=" + data +
                '}';
    }

}
