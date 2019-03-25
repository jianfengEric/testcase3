package com.tng.portal.sms.server.constant;

public enum SMSServiceApplicationStatus {

	ACT("ACT"),NACT("NACT");
	
	private String desc;

	private SMSServiceApplicationStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
