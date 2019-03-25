package com.tng.portal.ana.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dell on 2017/9/13.
 */
public class AccountQueryPostDto implements Serializable {
    private String search;
    private List<String> accounts;

    private List<String> mids;
    /**
     *  Is it in accounts Query in list 
     */
    private boolean isInAccounts = true;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public List<String> getMids() {
        return mids;
    }

    public void setMids(List<String> mids) {
        this.mids = mids;
    }

    public boolean isInAccounts() {
        return isInAccounts;
    }

    public void setInAccounts(boolean inAccounts) {
        isInAccounts = inAccounts;
    }

    @Override
    public String toString() {
        return "AccountQueryPostDto{" +
                "search='" + search + '\'' +
                ", accounts=" + accounts +
                ", mids=" + mids +
                ", isInAccounts=" + isInAccounts +
                '}';
    }

}
