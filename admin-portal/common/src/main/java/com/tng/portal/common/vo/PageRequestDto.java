package com.tng.portal.common.vo;

import java.io.Serializable;

/**
 * Created by Owen on 2018/9/13.
 */
public class PageRequestDto implements Serializable{
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private Boolean isAscending;
    private String searchBy;
    private String search;

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

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getIsAscending() {
        return isAscending;
    }

    public void setIsAscending(Boolean isAscending) {
        this.isAscending = isAscending;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
