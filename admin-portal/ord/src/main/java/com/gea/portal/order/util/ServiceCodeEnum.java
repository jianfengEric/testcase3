package com.gea.portal.order.util;

public enum ServiceCodeEnum {
	BILL_PAYMENT("BILL_PAYMENT", "Global Sim Top-up"), SIM_TOP_UP("SIM_TOP_UP",
			"Global Bill paymen"), REMITTANCE("REMITTANCE", "Money Transfer Out");

	private String value;

	private String desc;

	ServiceCodeEnum(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
}
