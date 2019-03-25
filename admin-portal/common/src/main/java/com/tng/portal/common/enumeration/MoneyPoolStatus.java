package com.tng.portal.common.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2018/9/3.
 */
public enum  MoneyPoolStatus {

    PENDING_FOR_PROCESS("PENDING_FOR_PROCESS"),
    ACTIVE("ACTIVE"),
    DORMANT("DORMANT"),
    SUSPEND("SUSPEND"),
    CLOSED("CLOSED"),
    REJECTED("REJECTED");

    private MoneyPoolStatus(String value) {
        this.value = value;
    }
    
    public static MoneyPoolStatus findByValue(String value){
    	for(MoneyPoolStatus item : MoneyPoolStatus.values()){
    		if(item.getValue().equals(value)){
    			return item;
    		}
    	}
    	return null;
    }
    
    public static List<MoneyPoolStatus> findValidStatus(){
    	List<MoneyPoolStatus> mqStatus = new ArrayList<>();
    	mqStatus.add(MoneyPoolStatus.ACTIVE);
    	mqStatus.add(MoneyPoolStatus.CLOSED);
    	mqStatus.add(MoneyPoolStatus.DORMANT);
    	mqStatus.add(MoneyPoolStatus.SUSPEND);
    	return mqStatus;
    }

    public String getValue() {
        return value;
    }

    private String value;

}
