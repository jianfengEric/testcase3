package com.tng.portal.common.dto.tre;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ExchangeRateFileDto  implements Serializable{
    private String exchangeRateFileId;
    private String currentFileName;
    private String lastUserId;
    private String lastDateTime;
    private String newFileName;
    private String newUserId;
    private String newDateTime;
    private String status;
    private Long requestApprovalId;
    private Map errorData;
    private List<CorrelationDataDto> data;
    private List<CorrelationDataDto> newData;
    private String requestRemark;

    public String getExchangeRateFileId() {
        return exchangeRateFileId;
    }

    public void setExchangeRateFileId(String exchangeRateFileId) {
        this.exchangeRateFileId = exchangeRateFileId;
    }

    public List<CorrelationDataDto> getData() {
        return data;
    }

    public void setData(List<CorrelationDataDto> data) {
        this.data = data;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public String getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(String lastUserId) {
        this.lastUserId = lastUserId;
    }

    public String getLastDateTime() {
        return lastDateTime;
    }

    public void setLastDateTime(String lastDateTime) {
        this.lastDateTime = lastDateTime;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(String newUserId) {
        this.newUserId = newUserId;
    }

    public String getNewDateTime() {
        return newDateTime;
    }

    public void setNewDateTime(String newDateTime) {
        this.newDateTime = newDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Long getRequestApprovalId() {
		return requestApprovalId;
	}

	public void setRequestApprovalId(Long requestApprovalId) {
		this.requestApprovalId = requestApprovalId;
	}

    public List<CorrelationDataDto> getNewData() {
        return newData;
    }

    public void setNewData(List<CorrelationDataDto> newData) {
        this.newData = newData;
    }

    public Map getErrorData() {
        return errorData;
    }

    public void setErrorData(Map errorData) {
        this.errorData = errorData;
    }

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}
