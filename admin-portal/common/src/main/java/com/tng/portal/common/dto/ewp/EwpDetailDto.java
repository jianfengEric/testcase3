package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by Jimmy on 2018/9/6.
 */
public class EwpDetailDto implements Serializable {

    private FullCompanyInfoDto fullCompanyInfoDto;
    private ServiceSettingRequestDto serviceSettingRequestDto;
    private ServiceAssignmentDto serviceAssignmentDto;
    private GatewaySettingDto gatewaySettingDto;
    private CutOffTimeDto cutOffTimeDto;
    private StatusChangeDto statusChangeDto;

    public ServiceSettingRequestDto getServiceSettingRequestDto() {
        return serviceSettingRequestDto;
    }

    public void setServiceSettingRequestDto(ServiceSettingRequestDto serviceSettingRequestDto) {
        this.serviceSettingRequestDto = serviceSettingRequestDto;
    }

    public ServiceAssignmentDto getServiceAssignmentDto() {
        return serviceAssignmentDto;
    }

    public void setServiceAssignmentDto(ServiceAssignmentDto serviceAssignmentDto) {
        this.serviceAssignmentDto = serviceAssignmentDto;
    }

    public CutOffTimeDto getCutOffTimeDto() {
        return cutOffTimeDto;
    }

    public void setCutOffTimeDto(CutOffTimeDto cutOffTimeDto) {
        this.cutOffTimeDto = cutOffTimeDto;
    }

    public FullCompanyInfoDto getFullCompanyInfoDto() {
        return fullCompanyInfoDto;
    }

    public void setFullCompanyInfoDto(FullCompanyInfoDto fullCompanyInfoDto) {
        this.fullCompanyInfoDto = fullCompanyInfoDto;
    }

    public GatewaySettingDto getGatewaySettingDto() {
        return gatewaySettingDto;
    }

    public void setGatewaySettingDto(GatewaySettingDto gatewaySettingDto) {
        this.gatewaySettingDto = gatewaySettingDto;
    }

	public StatusChangeDto getStatusChangeDto() {
		return statusChangeDto;
	}

	public void setStatusChangeDto(StatusChangeDto statusChangeDto) {
		this.statusChangeDto = statusChangeDto;
	}
}
