package com.tng.portal.common.vo.rest;

public class RestResponse<T> {

	private RestStatus status;
	private T data;
	
	public RestResponse(){
		this.setSuccessStatus();
	}
	
	public RestResponse(RestStatus status){
		this.status = status;
	}
	
	public RestResponse(T data){
		this.data = data;
		this.status = new RestStatus();
	}
	
	public RestResponse(RestStatus status, T data){
		this.status = status;
		this.data = data;
	}
	public void setSuccessStatus(){
		this.status = new RestStatus(0,"SUCCESS");
	}
	public void setFailureStatus(String errorMsg){
		this.status = new RestStatus(-1,errorMsg);
	}
	public void setFailureStatus(int code,String errorMsg){
		this.status = new RestStatus(code,errorMsg);
	}
	
	public RestStatus getStatus() {
		return status;
	}
	public void setStatus(RestStatus status) {
		this.status = status;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	
}
