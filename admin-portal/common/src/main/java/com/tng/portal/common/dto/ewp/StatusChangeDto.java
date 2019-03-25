package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by Jimmy on 2018/8/31.
 */
public class StatusChangeDto implements Serializable {

    private String participantId;
    private String fromStatus;
    private String toStatus;
    private String changeReason;
    private String currentEnvir;
    private String status;
    private String requestRemark;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(String fromStatus) {
        this.fromStatus = fromStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getCurrentEnvir() {
        return currentEnvir;
    }

    public void setCurrentEnvir(String currentEnvir) {
        this.currentEnvir = currentEnvir;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}

}
