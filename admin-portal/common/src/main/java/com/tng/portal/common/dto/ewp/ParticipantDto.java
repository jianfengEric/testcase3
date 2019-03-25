package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2018/8/24.
 */
public class ParticipantDto implements Serializable{

    private String geaParticipantRefId;
    private String participantName;
    private String serviceNo;
    private String moneyPoolNo;
    private String participantStatus;
    private String participantId;
    private String approvalStatus;
    private List<MoneyPoolServiceDto> moneyPoolServices;

    public ParticipantDto() {
    }

    public ParticipantDto(String geaParticipantRefId, String participantName, String serviceNo, String moneyPoolNo, String participantStatus) {
        this.geaParticipantRefId = geaParticipantRefId;
        this.participantName = participantName;
        this.serviceNo = serviceNo;
        this.moneyPoolNo = moneyPoolNo;
        this.participantStatus = participantStatus;
    }

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getMoneyPoolNo() {
        return moneyPoolNo;
    }

    public void setMoneyPoolNo(String moneyPoolNo) {
        this.moneyPoolNo = moneyPoolNo;
    }

    public String getParticipantStatus() {
        return participantStatus;
    }

    public void setParticipantStatus(String participantStatus) {
        this.participantStatus = participantStatus;
    }

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

    public List<MoneyPoolServiceDto> getMoneyPoolServices() {
        return moneyPoolServices;
    }

    public void setMoneyPoolServices(List<MoneyPoolServiceDto> moneyPoolServices) {
        this.moneyPoolServices = moneyPoolServices;
    }
}
