package com.tng.portal.common.enumeration;

/**
 */
public enum DeployType {

	PARTICIPANT("PARTICIPANT"),
	ADJUSTMENT("ADJUSTMENT"),
	DEPOSIT("DEPOSIT"),
	CASH_OUT("CASH_OUT"),
	EXCHANGE_RATE("EXCHANGE_RATE"),
	SERVICE_MARKUP("SERVICE_MARKUP"),
	;

    private DeployType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
