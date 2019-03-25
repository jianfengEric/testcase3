package com.tng.portal.sms.server.constant;

public enum SMSStatus {

	NEW("NEW"),QUEUE("QUEUE"),SENDING("SENDING"),SUCCESS("SUCCESS"),FAIL("FAIL"),RESENT("RESENT");
	
	private String desc;

	private SMSStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
