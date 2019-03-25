package com.tng.portal.common.enumeration;

import com.tng.portal.common.util.ApplicationContext;

/**
 * Created by Owen on 2018/9/3.
 */
public enum ApprovalType {
    CREATE_PARTICIPANT("CREATE_PARTICIPANT"),
    FULL_COMPANY_INFORMATION("FULL_COMPANY_INFORMATION"),
    SERVICE_SETTING("SERVICE_SETTING"),
    SERVICE_POOL_ASSIGNMENT("SERVICE_POOL_ASSIGNMENT"),
    API_GATEWAY_SETTING("API_GATEWAY_SETTING"),
    SETTLEMENT_CUT_OFF_TIME("SETTLEMENT_CUT_OFF_TIME"),
    DEPOSIT("DEPOSIT"),
    CASH_OUT("CASH_OUT"),
    CREATE_INDIVIDUAL_MONEY_POOL("CREATE_INDIVIDUAL_MONEY_POOL"),
    MONEY_POOL_STATUS_ALERT_LINE("MONEY_POOL_STATUS_ALERT_LINE"),
    ADJUSTMENT("ADJUSTMENT"),
    VOID_CHARGE("VOID_CHARGE"),
    VOID_CHARGE_FEE("VOID_CHARGE_FEE"),
    VOID_CHARGE_CHARGE_FEE("VOID_CHARGE_CHARGE_FEE"),
    EXCHANGE_RATE_SETTING("EXCHANGE_RATE_SETTING"),
    PARTICIPANT_CHANGE_STATUS("PARTICIPANT_CHANGE_STATUS"),
	SERVICE_MARKUP("SERVICE_MARKUP"),
	;
	
	public static String getModule(ApprovalType approvalType){
        switch (approvalType) {
            case CREATE_PARTICIPANT:
            case FULL_COMPANY_INFORMATION:
            case SERVICE_SETTING:
            case SERVICE_POOL_ASSIGNMENT:
            case API_GATEWAY_SETTING:
            case SETTLEMENT_CUT_OFF_TIME:
            case PARTICIPANT_CHANGE_STATUS:
                return ApplicationContext.Modules.EWP;
            case DEPOSIT:
            case CASH_OUT:
            case CREATE_INDIVIDUAL_MONEY_POOL:
            case MONEY_POOL_STATUS_ALERT_LINE:
            case ADJUSTMENT:
            case VOID_CHARGE:
            case VOID_CHARGE_FEE:
            case VOID_CHARGE_CHARGE_FEE:
                return ApplicationContext.Modules.MP;
            case EXCHANGE_RATE_SETTING:
                return ApplicationContext.Modules.TRE;
            case SERVICE_MARKUP:
            	return ApplicationContext.Modules.SRV;
            default:
                return "";
        }
	}

    ApprovalType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;

}
