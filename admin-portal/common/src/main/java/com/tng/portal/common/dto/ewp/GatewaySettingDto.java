package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dell on 2018/9/21.
 */
public class GatewaySettingDto implements Serializable{
    private List<ApiGatewaySettingDto> apiGatewaySettingDto;
    private List<ApiGatewaySettingDto> oldApiGatewaySettingDto;
    private ApiGatewaySettingDto defApiGatewaySettingDto;
    private Boolean isFirst;   // For the first time deployment from pre-production,production A API gateway setting Request 

    public GatewaySettingDto() {
    }

    public GatewaySettingDto(List<ApiGatewaySettingDto> apiGatewaySettingDto, List<ApiGatewaySettingDto> oldApiGatewaySettingDto) {
        this.apiGatewaySettingDto = apiGatewaySettingDto;
        this.oldApiGatewaySettingDto = oldApiGatewaySettingDto;
    }

    public List<ApiGatewaySettingDto> getApiGatewaySettingDto() {
        return apiGatewaySettingDto;
    }

    public void setApiGatewaySettingDto(List<ApiGatewaySettingDto> apiGatewaySettingDto) {
        this.apiGatewaySettingDto = apiGatewaySettingDto;
    }

    public List<ApiGatewaySettingDto> getOldApiGatewaySettingDto() {
        return oldApiGatewaySettingDto;
    }

    public void setOldApiGatewaySettingDto(List<ApiGatewaySettingDto> oldApiGatewaySettingDto) {
        this.oldApiGatewaySettingDto = oldApiGatewaySettingDto;
    }

	public ApiGatewaySettingDto getDefApiGatewaySettingDto() {
		return defApiGatewaySettingDto;
	}

	public void setDefApiGatewaySettingDto(ApiGatewaySettingDto defApiGatewaySettingDto) {
		this.defApiGatewaySettingDto = defApiGatewaySettingDto;
	}

	public Boolean getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}
}
