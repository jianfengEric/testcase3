package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dong on 2018/8/27.
 */
public class ServiceSettingDto implements Serializable{

	private static final long serialVersionUID = -6762057339450899885L;
	private String ewpServiceId;
	private String serviceId;
	private String name;
    private String serviceStatus;
    private String status;
    private String markup;
    private String serviceCode;
	private List<ServiceFromCurrencyDto> fromCurrencyDto;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public List<ServiceFromCurrencyDto> getFromCurrencyDto() {
		return fromCurrencyDto;
	}

	public void setFromCurrencyDto(List<ServiceFromCurrencyDto> fromCurrencyDto) {
		this.fromCurrencyDto = fromCurrencyDto;
	}

	public String getEwpServiceId() {
		return ewpServiceId;
	}

	public void setEwpServiceId(String ewpServiceId) {
		this.ewpServiceId = ewpServiceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
