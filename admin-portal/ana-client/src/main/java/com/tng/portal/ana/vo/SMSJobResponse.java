package com.tng.portal.ana.vo;

import java.io.Serializable;

import com.tng.portal.common.vo.PageDatas;

/**
 * Created by Zero on 2017/2/6.
 */
public class SMSJobResponse implements Serializable{
    private String status;
    private String errorCode;
    private String messageEN;
    private String messageZhHK;
    private String messageZhCN;
    private PageDatas<SMSJobQueryVo> data;

    public SMSJobResponse() {
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

    public void setSuccessStatus(){
        this.status = "success";
        this.messageEN = "success";
        this.messageZhHK = "\u6210\u529f";
        this.messageZhCN = "\u6210\u529f";
    }

    public void setFailStatus(){
        this.status = "fail";
    }

	public PageDatas<SMSJobQueryVo> getData() {
		return data;
	}

	public void setData(PageDatas<SMSJobQueryVo> data) {
		this.data = data;
	}
}
