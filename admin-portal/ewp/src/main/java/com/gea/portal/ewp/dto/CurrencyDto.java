package com.gea.portal.ewp.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2018/9/6.
 */
public class CurrencyDto implements Serializable{
	
    private String currFrom;
    
    private List<Map<String,String>> currToList;
    
	public CurrencyDto() {
		super();
	}
	
	public CurrencyDto(String currFrom, List<Map<String, String>> currToList) {
		super();
		this.currFrom = currFrom;
		this.currToList = currToList;
	}

	public String getCurrFrom() {
		return currFrom;
	}
	public void setCurrFrom(String currFrom) {
		this.currFrom = currFrom;
	}
	public List<Map<String,String>> getCurrToList() {
		return currToList;
	}
	public void setCurrToList(List<Map<String,String>> currToList) {
		this.currToList = currToList;
	}

}
