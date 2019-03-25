package com.gea.portal.eny.enumeration;

/**
 * Created by Eric on 2019/1/2.
 */
public enum SortName {
    MP_CF_REF_NO("geaMoneyTransferId"),
    CREATE_DATETIME("transactionDateTime"),
    SERVICE_ID("serviceId"),
    ACTION("transactionType"),
    CURRENCY_TYPE("currency"),
    CHANGE_BALANCE("amount"),
    PREVIOUS_TOTAL_BALANCE("beforeTotalBalance"),
    LAST_TOTAL_BALANCE("afterTotalBalance"),
    REMARK("remark"),

    //other
    GEA_USER_ID("geaUserId"),
    ;

    public static String getSortName(SortName sortBy){
        String prefix="m.";
        switch (sortBy) {
            case MP_CF_REF_NO:
                return prefix+SortName.MP_CF_REF_NO.getValue();
            case CREATE_DATETIME:
                return prefix+SortName.CREATE_DATETIME.name();
            case SERVICE_ID:
                return prefix+SortName.SERVICE_ID.name();
            case ACTION:
                return prefix+SortName.ACTION.name();
            case CURRENCY_TYPE:
                return prefix+SortName.CURRENCY_TYPE.name();
            case CHANGE_BALANCE:
                return prefix+SortName.CHANGE_BALANCE.name();
            case PREVIOUS_TOTAL_BALANCE:
                return prefix+SortName.PREVIOUS_TOTAL_BALANCE.name();
            case LAST_TOTAL_BALANCE:
                return prefix+SortName.LAST_TOTAL_BALANCE.name();
            case REMARK:
                return prefix+SortName.REMARK.name();
            default:
                return prefix+SortName.CREATE_DATETIME.name();
        }
    }

    SortName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;

    public static SortName findByValue(String sortBy) {
        for(SortName item:SortName.values()){
            if(item.getValue().equals(sortBy)){
                return item;
            }
        }
        return null;
    }
}
