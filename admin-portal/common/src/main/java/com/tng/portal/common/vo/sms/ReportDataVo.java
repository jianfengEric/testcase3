package com.tng.portal.common.vo.sms;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by Owen on 2017/6/12.
 */
public class ReportDataVo {

    private String date;
    /**
     *  Back to background for query 
     */
    private String formatDate;
    private String accountId;
    private String jobId;
    private String sender;
    private long jobCount;
    private long successCount;
    private long failCount;
    private long totalCount;
    private String provider;
    private String mobNo;
    private List<String> failMobiles;

    @JsonIgnore
    private String allMobileNumber;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(long successCount) {
        this.successCount = successCount;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public long getJobCount() {
        return jobCount;
    }

    public void setJobCount(long jobCount) {
        this.jobCount = jobCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

	public List<String> getFailMobiles() {
		return failMobiles;
	}

	public void setFailMobiles(List<String> failMobiles) {
		this.failMobiles = failMobiles;
	}

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAllMobileNumber() {
        return allMobileNumber;
    }

    public void setAllMobileNumber(String allMobileNumber) {
        this.allMobileNumber = allMobileNumber;
    }
}

