package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2018/8/27.
 */
public class ServiceAssignmentDto implements Serializable {
    private String participantId;
    private String instance;
    private List<MoneyPoolDto> moneyPoolDtoList;
    private List<MoneyPoolDto> oldMoneyPoolDtoList;
    private String requestRemark;

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public List<MoneyPoolDto> getMoneyPoolDtoList() {
        return moneyPoolDtoList;
    }

    public void setMoneyPoolDtoList(List<MoneyPoolDto> moneyPoolDtoList) {
        this.moneyPoolDtoList = moneyPoolDtoList;
    }

    public List<MoneyPoolDto> getOldMoneyPoolDtoList() {
        return oldMoneyPoolDtoList;
    }

    public void setOldMoneyPoolDtoList(List<MoneyPoolDto> oldMoneyPoolDtoList) {
        this.oldMoneyPoolDtoList = oldMoneyPoolDtoList;
    }

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}
