package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Owen on 2018/8/27.
 */
public class ServiceSettingRequestDto implements Serializable{
    private String participantId;
    private String instance;
    private List<ServiceSettingDto> serviceSettingDtoList;
    private List<ServiceSettingDto> oldServiceSettingDtoList;
    private String requestRemark;

    public List<ServiceSettingDto> getServiceSettingDtoList() {
        return serviceSettingDtoList;
    }

    public void setServiceSettingDtoList(List<ServiceSettingDto> serviceSettingDtoList) {
        this.serviceSettingDtoList = serviceSettingDtoList;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public List<ServiceSettingDto> getOldServiceSettingDtoList() {
        return oldServiceSettingDtoList;
    }

    public void setOldServiceSettingDtoList(List<ServiceSettingDto> oldServiceSettingDtoList) {
        this.oldServiceSettingDtoList = oldServiceSettingDtoList;
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
