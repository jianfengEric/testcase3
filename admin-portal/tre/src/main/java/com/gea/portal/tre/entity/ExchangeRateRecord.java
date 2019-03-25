package com.gea.portal.tre.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dell on 2018/9/14.
 */
@Table(name = "exchange_rate_record")
@Entity
public class ExchangeRateRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "curr_from")
    private String currFrom;
    @Column(name = "curr_to")
    private String currTo;
    @Column(name = "offer_rate")
    private BigDecimal offerRate;
    @Column(name = "expire_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;
    @Column(name = "exch_rate_file_id")
    private Long exchRateFileId;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
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

    public BigDecimal getOfferRate() {
        return offerRate;
    }

    public void setOfferRate(BigDecimal offerRate) {
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
