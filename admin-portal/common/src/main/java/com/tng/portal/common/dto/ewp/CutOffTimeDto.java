package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by Dell on 2018/9/7.
 */
public class CutOffTimeDto implements Serializable{
    private String participantId;
    private String cutOffTime;
    private String oldCutOffTime;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(String cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public String getOldCutOffTime() {
        return oldCutOffTime;
    }

    public void setOldCutOffTime(String oldCutOffTime) {
        this.oldCutOffTime = oldCutOffTime;
    }
}
