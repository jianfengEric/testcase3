package com.tng.portal.common.vo.rest;

public class RestStatus {

    private Integer code = 0;
    private String message = "SUCCESS";
    
    public RestStatus(){}
    
    public RestStatus(Integer code, String message){
    	this.code = code;
    	this.message = message;
    }
    
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

    
}
