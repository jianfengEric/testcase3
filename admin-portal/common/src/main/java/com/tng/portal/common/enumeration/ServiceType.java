package com.tng.portal.common.enumeration;

import java.util.stream.Stream;

/**
 * Created by Owen on 2018/8/27.
 */
public enum ServiceType {

    BANK_TRANSFER("Global Remittance - Bank Transfer",1),
    CASH_PICKUP("Global Remittance - Cash Pickup",2),
    PAYMENT("Global Bill Payment",3),
    TOP_UP("Global SIM Top-up",4),
    CHANGE_NAME("Global Remittance - Cash Pickup - Change Name",5),
    CANCEL("Global Remittance - Cash Pickup - Cancel",6);

	public static ServiceType findByNo(Integer no){
		for(ServiceType item : ServiceType.values()){
			if(item.getNo().equals(no)){
				return item;
			}
		}
		return null;
	}
	
	public static ServiceType findByValue(String value){
		for(ServiceType item : ServiceType.values()){
			if(item.getValue().equals(value)){
				return item;
			}
		}
		return null;
	}

    private ServiceType(String value,Integer no) {
        this.value = value;
        this.no = no;
    }

    public String getValue() {
        return value;
    }
    

	public Integer getNo() {
		return no;
	}

    private String value;
    private Integer no;

    public static ServiceType stringToInstance(String value){
        return Stream.of(ServiceType.values()).filter(item -> item.getValue().equals(value)).findFirst().orElse(null);
    }



}
