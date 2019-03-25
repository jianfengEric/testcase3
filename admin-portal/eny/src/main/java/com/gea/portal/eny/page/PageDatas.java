package com.gea.portal.eny.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageDatas<T> {
    public static final Integer defaultPageSize = 10;

    private List<T> list;
    private Integer pageNo; // page 
    private Integer pageSize; //  Per page size 
    private Integer totalPages; // PageCount 
    private Long total; //  Total 
    private boolean all = false;

    public PageDatas() {
    }

    public PageDatas(Integer pageNo) {
        this.setPageNo(pageNo);
        this.setPageSize(defaultPageSize);
        if (pageNo == null || pageNo < 0) {
            this.all = true;
            this.pageNo = null;
            this.pageSize = null;
        }
    }

    public PageDatas(Integer pageNo, Integer pageSize) {
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        if (pageNo == null || pageNo < 0) {
            this.all = true;
            this.pageNo = null;
            this.pageSize = null;
        }
    }

    public void initDatas(List<T> list, long totalElements, int totalPages) {
        this.list = list;
        this.total = totalElements;
        this.totalPages = totalPages;
    }

    public Sort pageSort(String sortBy, Boolean isAscending) {
        return pageSort(sortBy, isAscending, sortBy);
    }

    public Sort pageSort(String sortBy, Boolean isAscending, String sortByDefault) {
        sortBy = StringUtils.isEmpty(sortBy) ? sortByDefault : sortBy;
        Sort.Direction direction = (isAscending != null && isAscending) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = new Sort(direction, sortBy);
        return sort;
    }

    public PageRequest pageRequest() {
        return pageRequest(null);
    }

    public PageRequest pageRequest(Sort sort) {
        if (sort == null) {
            return new PageRequest(this.pageNo - 1, this.pageSize);
        }
        return new PageRequest(this.pageNo - 1, this.pageSize, sort);
    }

    public List findAll(JpaRepository jpaRepository) {
        return findAll(jpaRepository, null);
    }

    public List findAll(JpaRepository jpaRepository, String sortBy, Boolean isAscending, String sortByDefault) {
        Sort sort = pageSort(sortBy, isAscending, sortByDefault);
        return findAll(jpaRepository, sort);
    }

    public List findAll(JpaRepository jpaRepository, Sort sort) {
        if (all) {
            if (sort == null) {
                return jpaRepository.findAll();
            }
            return jpaRepository.findAll(sort);
        }
        Page page = jpaRepository.findAll(pageRequest(sort));
        List list = page.getContent();
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        return list;
    }

    public void initPageParam(Page page) {
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if (pageNo == null || pageNo < 1) {
            this.pageNo = 1;
            return;
        }
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            this.pageSize = defaultPageSize;
            return;
        }
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @JsonIgnore
    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

}
