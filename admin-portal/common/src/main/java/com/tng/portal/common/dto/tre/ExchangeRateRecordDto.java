package com.tng.portal.common.dto.tre;

import java.io.Serializable;
import java.util.Date;
/**
 * Created by Dell on 2018/9/14.
 */
public class ExchangeRateRecordDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String currFrom;
    private String currTo;
    private String offerRate;
    private Date expireDate;
    private Long exchRateFileId;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrFrom() {
        return currFrom;
    }

    public void setCurrFrom(String currFrom) {
        this.currFrom = currFrom;
    }

    public String getCurrTo() {
        return currTo;
    }

    public void setCurrTo(String currTo) {
        this.currTo = currTo;
    }

    public String getOfferRate() {
        return offerRate;
    }

    public void setOfferRate(String offerRate) {
        this.offerRate = offerRate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Long getExchRateFileId() {
        return exchRateFileId;
    }

    public void setExchRateFileId(Long exchRateFileId) {
        this.exchRateFileId = exchRateFileId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
