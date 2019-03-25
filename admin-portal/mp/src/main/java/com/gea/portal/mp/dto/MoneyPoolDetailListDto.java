package com.gea.portal.mp.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoneyPoolDetailListDto implements Serializable {
    private String status;
    private String errorCode;
    private String error;
    private String totalElements;
    private String totalPages;
    private List<MoneyPoolTransactionDto> data;

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<MoneyPoolTransactionDto> getData() {
        return data;
    }

    public void setData(List<MoneyPoolTransactionDto> data) {
        this.data = data;
    }
}
