package com.tng.portal.common.enumeration;

import java.util.Arrays;

/**
 * Created by Eric on 2018/10/8.
 */
public enum ServiceName {
    participant("participant"),
    ewpServiceMarkupSetting("ewpServiceMarkupSetting"),
    ewpMoneypool("ewpMoneypool"),
    ewpMoneypoolServiceMap("ewpMoneypoolServiceMap"),
    ewpServiceCurrencySetting("ewpServiceCurrencySetting"),
    adjustment("adjustment"),
    deposit("deposit"),
    cashout("cashout"),
    ewpProgramSetting("ewpProgramSetting"),
    ewpApiGatewayConfig("ewpApiGatewayConfig"),
    settlementCutOffTimeSetting("settlementCutOffTimeSetting"),
    serviceMarkup("serviceMarkup"),
    exchangeRate("exchangeRate"),
	;
	
	public static ServiceName findByValue(String value){
		return Arrays.asList(ServiceName.values()).stream().filter(item -> item.getValue().equals(value)).findFirst().orElse(null);
	}

    private ServiceName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
