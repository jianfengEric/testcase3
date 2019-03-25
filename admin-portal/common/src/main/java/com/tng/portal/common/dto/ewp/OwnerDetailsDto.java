package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by dong on 2018/8/27.
 */
public class OwnerDetailsDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8579233950972819289L;
	private String role;
    private String name;
    private String email;
    private String directLine;
    private String mobileNumber;
	private MaterialDto materialDto;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public String getDirectLine() {
		return directLine;
	}

	public void setDirectLine(String directLine) {
		this.directLine = directLine;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public MaterialDto getMaterialDto() {
		return materialDto;
	}

	public void setMaterialDto(MaterialDto materialDto) {
		this.materialDto = materialDto;
	}
}
