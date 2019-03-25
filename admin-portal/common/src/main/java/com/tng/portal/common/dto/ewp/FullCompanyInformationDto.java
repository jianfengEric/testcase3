package com.tng.portal.common.dto.ewp;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dong on 2018/8/27.
 */
public class FullCompanyInformationDto implements Serializable{

	private static final long serialVersionUID = 8266968597510790268L;
	private String participantId;
	private String fullCompanyName;
	private String country;
    private String address;
    private String registrationNo;
	private String registrationDate;
    private List<OwnerDetailsDto> ownerDetailsDto;
    private List<KeyPersonInformationDto> keyPersonInformationDto;
    private List<DisputeContactDto> disputeContactDto;
    private MaterialDto materialDto;
	private String participantName;
	private String participantStatus;
	private String participantRecordStatus;
	private String fullCompanyStatus;
	private String notificationEmail;
	private String currentEnvir;
	private String requestRemark;


	public String getFullCompanyName() {
		return fullCompanyName;
	}

	public void setFullCompanyName(String fullCompanyName) {
		this.fullCompanyName = fullCompanyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public List<OwnerDetailsDto> getOwnerDetailsDto() {
		return ownerDetailsDto;
	}

	public void setOwnerDetailsDto(List<OwnerDetailsDto> ownerDetailsDto) {
		this.ownerDetailsDto = ownerDetailsDto;
	}

	public List<KeyPersonInformationDto> getKeyPersonInformationDto() {
		return keyPersonInformationDto;
	}

	public void setKeyPersonInformationDto(List<KeyPersonInformationDto> keyPersonInformationDto) {
		this.keyPersonInformationDto = keyPersonInformationDto;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public List<DisputeContactDto> getDisputeContactDto() {
		return disputeContactDto;
	}

	public void setDisputeContactDto(List<DisputeContactDto> disputeContactDto) {
		this.disputeContactDto = disputeContactDto;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	public String getFullCompanyStatus() {
		return fullCompanyStatus;
	}

	public void setFullCompanyStatus(String fullCompanyStatus) {
		this.fullCompanyStatus = fullCompanyStatus;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public MaterialDto getMaterialDto() {
		return materialDto;
	}

	public void setMaterialDto(MaterialDto materialDto) {
		this.materialDto = materialDto;
	}

	public String getCurrentEnvir() {
		return currentEnvir;
	}

	public void setCurrentEnvir(String currentEnvir) {
		this.currentEnvir = currentEnvir;
	}

	public String getParticipantRecordStatus() {
		return participantRecordStatus;
	}

	public void setParticipantRecordStatus(String participantRecordStatus) {
		this.participantRecordStatus = participantRecordStatus;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}


}
