package com.tng.portal.sms.constant;

public enum SMSSendStatus {
	
	IN_PROGESS("1"),SUCCESS("2"),FAIL("3"),CANCELLED("4");
	
	private String code;
	
	private SMSSendStatus(String code){
		this.code = code;
	}
}
