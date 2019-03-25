package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SMSQueryParamVo implements Serializable{
    private int pageNo;
    private int pageSize;
    private String startDate;
    private String endDate;
    private String mobileNumberSearch;
    private String contentSearch;
    private String senderIdSearch;
    private String referenceIdSearch;
    private String sortBy;
	@JsonProperty("isAscending")
    private boolean isAscending;
    private String applicationCode;
    private String successCountSearch;
    private String failCountSearch;
    private List<String> accountIds;

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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMobileNumberSearch() {
		return mobileNumberSearch;
	}
	public void setMobileNumberSearch(String mobileNumberSearch) {
		this.mobileNumberSearch = mobileNumberSearch;
	}
	public String getContentSearch() {
		return contentSearch;
	}
	public void setContentSearch(String contentSearch) {
		this.contentSearch = contentSearch;
	}
	public String getSenderIdSearch() {
		return senderIdSearch;
	}
	public void setSenderIdSearch(String senderIdSearch) {
		this.senderIdSearch = senderIdSearch;
	}
	public String getReferenceIdSearch() {
		return referenceIdSearch;
	}
	public void setReferenceIdSearch(String referenceIdSearch) {
		this.referenceIdSearch = referenceIdSearch;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public boolean isAscending() {
		return isAscending;
	}
	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}
	public String getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getSuccessCountSearch() {
		return successCountSearch;
	}

	public void setSuccessCountSearch(String successCountSearch) {
		this.successCountSearch = successCountSearch;
	}

	public String getFailCountSearch() {
		return failCountSearch;
	}

	public void setFailCountSearch(String failCountSearch) {
		this.failCountSearch = failCountSearch;
	}
	public List<String> getAccountIds() {
		return accountIds;
	}
	public void setAccountIds(List<String> accountIds) {
		this.accountIds = accountIds;
	}

}
