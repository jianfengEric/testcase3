package com.tng.portal.common.dto.gea;

import com.tng.portal.common.dto.DeployDetailDto;

/**
 * Created by Dell on 2018/10/17.
 */
public class GeaBaseResponse {
    private String status;
    private String errorCode;
    private String error;
    private DeployDetailDto participantEntry;

    public boolean hasSuccessful(){
        return "success".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DeployDetailDto getParticipantEntry() {
        return participantEntry;
    }

    public void setParticipantEntry(DeployDetailDto participantEntry) {
        this.participantEntry = participantEntry;
    }

    @Override
    public String toString() {
        return "GeaBaseResponse{" +
                "status='" + status + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", error='" + error + '\'' +
                ", participantEntry=" + participantEntry +
                '}';
    }
}
