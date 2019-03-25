package com.gea.portal.order.util;

public enum GeaTXStatusEnum {
	INPROCESS("1000","Inprocess"), 
	REJECTED("2000","Rejected"), 
	CANCELLED("3000","Cancelled"), 
	READY_FOR_PICKUP("4000","Ready for pickup"), 
	COMPLETED("5000","Completed"), 
	EXPIRED("6000","Expired");
	
	GeaTXStatusEnum(String code,String desc) {
		this.code = code;
		this.desc=desc;
	}
	private String code;
	
	private String desc;
	
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}

}
