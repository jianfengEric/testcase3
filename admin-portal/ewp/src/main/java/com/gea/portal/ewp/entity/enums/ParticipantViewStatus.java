package com.gea.portal.ewp.entity.enums;

import com.tng.portal.common.enumeration.ParticipantStatus;

/**
 * Created by Owen on 2018/8/24.
 */
public enum ParticipantViewStatus {

    Active("Active"),
    Dormant("Dormant"),
    Suspend("Suspend"),
    Closed("Closed");

    ParticipantViewStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private String value;


    public static ParticipantViewStatus participantStatusToView(ParticipantStatus participantStatus){
        switch (participantStatus){
            case ACTIVE:
                return ParticipantViewStatus.Active;
            case CLOSED:
                return ParticipantViewStatus.Closed;
            case SUSPEND: case PENDING_FOR_PROCESS:
                return ParticipantViewStatus.Suspend;
            case DORMANT:
                return ParticipantViewStatus.Dormant;
            default:
                return ParticipantViewStatus.Suspend;
        }
    }
    
    
    public static ParticipantViewStatus participantStatusToView(String value){
    	ParticipantStatus sta = ParticipantStatus.findByValue(value);
    	return participantStatusToView(sta);
    }

}
