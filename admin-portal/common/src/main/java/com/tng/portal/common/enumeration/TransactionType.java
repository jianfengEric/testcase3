package com.tng.portal.common.enumeration;

public enum TransactionType {

    TOP_UP("TOP_UP","Deposit"),
    WITH_DRAWAL("WITH_DRAWAL","Withdraw"),
    RESERVATION("RESERVATION","Reserve"),
    CHARGE("CHARGE","Charge"),
    RELEASE("RELEASE","Release"),
    ADJUSTMENT_TOP_UP("ADJUSTMENT_TOP_UP","Adjustment Deposit"),
    ADJUSTMENT_WITH_DRAWAL("ADJUSTMENT_WITH_DRAWAL","Adjustment Cash-out");

	private String value;
	private String key;
	
    private TransactionType(String key,String value) {
    	this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public String getKey(){
    	return key;
    }


    public static TransactionType findByKey(String key){
    	for(TransactionType item : TransactionType.values()){
    		if(key.equals(item.getKey())){
    			return item;
    		}
    	}
    	return null;
    }

    public static TransactionType findByValue(String value){
    	for(TransactionType item : TransactionType.values()){
    		if(value.equals(item.getValue())){
    			return item;
    		}
    	}
    	return null;
    }

}
