package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dell on 2018/9/21.
 */
public class FullCompanyInfoDto implements Serializable{
    private List<FullCompanyInformationDto> fullCompanyInformationDto;
    private List<FullCompanyInformationDto> oldFullCompanyInformationDto;
    private Boolean isDpyToProd;   // Is it allowed clone
    private Long geaSysPid;

    public FullCompanyInfoDto() {
    }

    public FullCompanyInfoDto(List<FullCompanyInformationDto> fullCompanyInformationDto, List<FullCompanyInformationDto> oldFullCompanyInformationDto) {
        this.fullCompanyInformationDto = fullCompanyInformationDto;
        this.oldFullCompanyInformationDto = oldFullCompanyInformationDto;
    }

    public List<FullCompanyInformationDto> getFullCompanyInformationDto() {
        return fullCompanyInformationDto;
    }

    public void setFullCompanyInformationDto(List<FullCompanyInformationDto> fullCompanyInformationDto) {
        this.fullCompanyInformationDto = fullCompanyInformationDto;
    }

    public List<FullCompanyInformationDto> getOldFullCompanyInformationDto() {
        return oldFullCompanyInformationDto;
    }

    public void setOldFullCompanyInformationDto(List<FullCompanyInformationDto> oldFullCompanyInformationDto) {
        this.oldFullCompanyInformationDto = oldFullCompanyInformationDto;
    }

	public Boolean getIsDpyToProd() {
		return isDpyToProd;
	}

	public void setIsDpyToProd(Boolean isDpyToProd) {
		this.isDpyToProd = isDpyToProd;
	}

	public Long getGeaSysPid() {
		return geaSysPid;
	}

	public void setGeaSysPid(Long geaSysPid) {
		this.geaSysPid = geaSysPid;
	}

}
