package com.tng.portal.common.dto.tre;

import java.io.Serializable;
import java.util.List;

public class ExchangeRateListDto  implements Serializable{
    private String fileName;
    private String expiryDate;
    private String lastUploadUserId;
    private String lastUploadDateTime;
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalPages;
    private Long total;
    private List<ExchangeRateRecordDto> data;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLastUploadUserId() {
        return lastUploadUserId;
    }

    public void setLastUploadUserId(String lastUploadUserId) {
        this.lastUploadUserId = lastUploadUserId;
    }

    public String getLastUploadDateTime() {
        return lastUploadDateTime;
    }

    public void setLastUploadDateTime(String lastUploadDateTime) {
        this.lastUploadDateTime = lastUploadDateTime;
    }

    public List<ExchangeRateRecordDto> getData() {
        return data;
    }

    public void setData(List<ExchangeRateRecordDto> data) {
        this.data = data;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
