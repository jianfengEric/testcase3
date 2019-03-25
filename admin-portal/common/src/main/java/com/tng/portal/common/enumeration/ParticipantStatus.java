package com.tng.portal.common.enumeration;

/**
 * Created by Owen on 2018/8/24.
 */
public enum ParticipantStatus {
    PENDING_FOR_PROCESS("PENDING_FOR_PROCESS","SUSPEND"),
    ACTIVE("ACTIVE","ACTIVE"),
    DORMANT("DORMANT","DORMANT"),
    SUSPEND("SUSPEND","SUSPEND"),
    CLOSED("CLOSED","CLOSED"),
	REJECTED("REJECTED", "REJECTED")
	;
	
	private String value;
	
	private String showView;
	
	public static ParticipantStatus findByValue(String value){
		for(ParticipantStatus item : ParticipantStatus.values()){
			if(item.getValue().equals(value)){
				return item;
			}
		}
		return null;
	}

    private ParticipantStatus(String value,String showView) {
        this.value = value;
        this.showView = showView;
    }

    public String getValue() {
        return value;
    }

	public String getShowView() {
		return showView;
	}

}
