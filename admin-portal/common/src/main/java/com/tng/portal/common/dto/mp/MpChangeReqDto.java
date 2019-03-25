package com.tng.portal.common.dto.mp;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Jimmy on 2018/9/3.
 */
public class MpChangeReqDto implements Serializable {

    private String geaParticipantRefId;
    private String moneyPoolId;
    private BigDecimal toAlertLine;
    private String currentEnvir;
    private String toStatus;
    private String status;
    private String requestRemark;

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
    }

    public BigDecimal getToAlertLine() {
        return toAlertLine;
    }

    public void setToAlertLine(BigDecimal toAlertLine) {
        this.toAlertLine = toAlertLine;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getCurrentEnvir() {
		return currentEnvir;
	}

	public void setCurrentEnvir(String currentEnvir) {
		this.currentEnvir = currentEnvir;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
    
    
}
