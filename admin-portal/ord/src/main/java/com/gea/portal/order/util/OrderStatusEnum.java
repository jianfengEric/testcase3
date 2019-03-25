package com.gea.portal.order.util;

public enum OrderStatusEnum {
	
	READY_FOR_PROCESS("READY_FOR_PROCESS","Ready for process"),
	PROCESSED("PROCESSED","Processed"),
	AMENDED("AMENDED","Amended"),
	CANCELLED("CANCELLED","Cancelled"),
	PENDING_FOR_SCREENING("PENDING_FOR_SCREENING","Pending for screening"),
	SCREENING_FAILED("SCREENING_FAILED","Screening failed"),
	PENDING_FOR_TOP_UP("PENDING_FOR_TOP_UP","Pending for top up"),
	PENDING_FOR_SUPPLEMENT("PENDING_FOR_SUPPLEMENT","Pending for supplement"),
	PENDING_FOR_REVIEW("PENDING_FOR_REVIEW","Pending for review"),
	SUPPLEMENT_REJECT("SUPPLEMENT_REJECT","Supplement reject"),
	SUPPLEMENT_CANCELLED("SUPPLEMENT_CANCELLED","Supplement cancelled"),
	PENDING_FOR_RESERVATION("PENDING_FOR_RESERVATION","Pending for reservation");

	private String value;
	private String desc;

	OrderStatusEnum(String value,String desc) {
		this.value = value;
		this.desc=desc;
	}

	public String getValue() {
		return value;
	}
	public String getDesc() {
		return desc;
	}

}
