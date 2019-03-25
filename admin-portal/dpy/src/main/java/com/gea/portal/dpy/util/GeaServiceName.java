package com.gea.portal.dpy.util;

/**
 * Created by Eric on 2018/10/8.
 */
public enum GeaServiceName {
    MONEY_TRANSFER_HUB_SERVICE("Money Transfer Hub Service","MTH"),
    MONEY_POOL_SERVICE("Money Pool Service","MP"),
    RATE_ENGINE_SERVICE("Rate Engine Service","RATE_ENGINE"),
    META_SERVICE("Meta Service","MS"),
    SCREENING_SERVER("Screening server","SS"),
    NOTIFICATION_SERVICE("Notification Service","NS");



    private GeaServiceName(String value, String sortName) {
        this.value = value;
        this.sortName = sortName;
    }

    private String value;
    private String sortName;

    
    public String getValue() {
    	return value;
    }
    public  String getSortName() {
        return sortName;
    }

    public static GeaServiceName findBySortName(String sortName){
        for(GeaServiceName item : GeaServiceName.values()){
            if(sortName.equalsIgnoreCase(item.getSortName())){
                return item;
            }
        }
        return null;
    }
    
    public static GeaServiceName findByValue(String value){
    	for(GeaServiceName item : GeaServiceName.values()){
            if(value.equalsIgnoreCase(item.getValue())){
                return item;
            }
        }
        return null;
    }
}
