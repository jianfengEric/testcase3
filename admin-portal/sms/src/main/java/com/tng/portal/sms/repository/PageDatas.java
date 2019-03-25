package com.tng.portal.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by Dell on 2016/11/11.
 */
public class PageDatas<T> {
    private static final  int DEFAULTPAGESIZE = 2;//sonar modify

    private List<T> list ;
    private int pageNo; // page 
    private int pageSize; //  Per page size 
    private int totalPages; // PageCount 
    private long total; //  Total 

    public PageDatas() {

    }
    public PageDatas(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
    public PageRequest pageRequest(){
        return pageRequest(null);
    }

    public PageRequest pageRequest(Sort sort){
        if (sort == null){
            return new PageRequest(this.pageNo-1,this.pageSize);
        }
        return new PageRequest(this.pageNo-1,this.pageSize,sort);
    }
    public PageRequest getPageRequest(int pageNO,int pageSize){
        if (pageNO<1){
            pageNO = 1;
        }
        this.pageNo = pageNO;
        this.pageSize = pageSize;
        int page = (this.pageNo-1) * pageSize - 1;
        if (page<0){
            page = 0;
        }
        return new PageRequest(page,pageSize);
    }

    public PageRequest getPageRequest(int pageNO){
        return getPageRequest(pageNO,DEFAULTPAGESIZE);
    }

    public void initDatas(List<T> list,long totalElements,int totalPages) {
        this.list = list;
        this.total = totalElements;
        this.totalPages = totalPages;
    }
    public void initDatas(Page page) {
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
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

    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
