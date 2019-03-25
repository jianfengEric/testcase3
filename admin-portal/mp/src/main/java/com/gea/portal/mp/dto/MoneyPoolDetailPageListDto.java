package com.gea.portal.mp.dto;

import java.io.Serializable;

import com.tng.portal.common.vo.PageDatas;

/**
 * Created by dong on 2018/9/12.
 */
public class MoneyPoolDetailPageListDto implements Serializable {

    private String moneyPoolId;
    private String participantId;
    private String participantName;
    private String baseCurrency;
    private String serviceNo;
    private String status;
    private String alertLine;
    private int pageNo;
    private int pageSize;
    private long total;
    private long totalPages;
    private String geaMoneyPoolRefId;

    public String getGeaMoneyPoolRefId() {
        return geaMoneyPoolRefId;
    }

    public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
        this.geaMoneyPoolRefId = geaMoneyPoolRefId;
    }

    private String geaParticipantRefId;

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    private PageDatas<MoneyPoolTransactionDto> moneyPoolTransactionDto;

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
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

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlertLine() {
        return alertLine;
    }

    public void setAlertLine(String alertLine) {
        this.alertLine = alertLine;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public PageDatas<MoneyPoolTransactionDto> getMoneyPoolTransactionDto() {
        return moneyPoolTransactionDto;
    }

    public void setMoneyPoolTransactionDto(PageDatas<MoneyPoolTransactionDto> moneyPoolTransactionDto) {
        this.moneyPoolTransactionDto = moneyPoolTransactionDto;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }
}
