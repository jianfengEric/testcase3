package com.gea.portal.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gea.portal.order.util.OrderStatusEnum;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailDto implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(OrderDetailDto.class);

    @SerializedName(value = "geaRefId")
    private String transferId;

    @SerializedName(value = "pId")
    private String participantId;

    @SerializedName(value = "participantName")
    private String participantName;

    @SerializedName(value = "recordType")
    private String serviceType;

    @SerializedName(value = "createDatetime")
    private String submissionDateTime;

    @SerializedName(value = "updateDatetime")
    private String updateTime;

    @SerializedName(value = "status")
    private String status; // Submitted /Approved/Rejected

    @SerializedName(value = "csRemark")
    private String remark;

    @SerializedName(value = "fromAmount")
    private String fromAmount;

    @SerializedName(value = "fromCountry")
    private String fromCountry;

    @SerializedName(value = "toCountry")
    private String toCountry;

    @SerializedName(value = "toAmount")
    private String toAmount;

    @SerializedName(value = "exchangeRate")
    private String exchangeRate;

    private String moneyPoolId;

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(String submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        try {
            this.status = OrderStatusEnum.valueOf(status).getDesc();
        } catch (IllegalArgumentException e){
            logger.error(status + "'s order status transform has exception");
        }
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        if(fromAmount != null){
            this.fromAmount = fromAmount.toPlainString();
        }
    }

    public String getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        if(toAmount != null){
            this.toAmount = toAmount.toPlainString();
        }
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        if(exchangeRate != null){
            this.exchangeRate = exchangeRate.toPlainString();
        }
    }

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolRefId) {
        this.moneyPoolId = moneyPoolRefId;
    }

}
