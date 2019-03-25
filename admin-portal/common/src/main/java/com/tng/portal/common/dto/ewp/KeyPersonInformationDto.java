package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by dong on 2018/8/27.
 */
public class KeyPersonInformationDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2731517115639597705L;
	private String role;
    private String department;
    private String name;
    private String email;
    private String mobileNumber;
    private Boolean receiveSms;
    private String directLine;
	private MaterialDto materialDto;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Boolean getReceiveSms() {
		return receiveSms;
	}

	public void setReceiveSms(Boolean receiveSms) {
		this.receiveSms = receiveSms;
	}

	public String getDirectLine() {
		return directLine;
	}

	public void setDirectLine(String directLine) {
		this.directLine = directLine;
	}

	public MaterialDto getMaterialDto() {
		return materialDto;
	}

	public void setMaterialDto(MaterialDto materialDto) {
		this.materialDto = materialDto;
	}
}
