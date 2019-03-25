package com.gea.portal.ewp.entity;

import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.Instance;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ewp_company_form database table.
 * 
 */
@Entity
@Table(name="ewp_company_form")
@NamedQuery(name="EwpCompanyForm.findAll", query="SELECT e FROM EwpCompanyForm e")
public class EwpCompanyForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="bd_registration_expiry_date")
	private Date bdRegistrationExpiryDate;

	@Column(name="bd_registration_no")
	private String bdRegistrationNo;

	@Column(name="country")
	private String country;
	
	@Column(name="company_address")
	private String companyAddress;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Column(name="full_company_name")
	private String fullCompanyName;

	@Column(name="participant_name_nls")
	private String participantNameNls;

	@Column(name="participant_name_en")
	private String participantNameEn;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ParticipantStatus status;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to EwalletParticipant
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="participant_id")
	private EwalletParticipant ewalletParticipant;

	//bi-directional many-to-one association to EwpDisputeContact
	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="ewpCompanyForm")
	private List<EwpDisputeContact> ewpDisputeContacts;

	//bi-directional many-to-one association to EwpKeyPerson
	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="ewpCompanyForm")
	private List<EwpKeyPerson> ewpKeyPersons;

	//bi-directional many-to-one association to EwpOwner
	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="ewpCompanyForm")
	private List<EwpOwner> ewpOwners;

	@Column(name="notification_email")
	private String notificationEmail;

	public EwpCompanyForm() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBdRegistrationExpiryDate() {
		return this.bdRegistrationExpiryDate;
	}

	public void setBdRegistrationExpiryDate(Date bdRegistrationExpiryDate) {
		this.bdRegistrationExpiryDate = bdRegistrationExpiryDate;
	}

	public String getBdRegistrationNo() {
		return this.bdRegistrationNo;
	}

	public void setBdRegistrationNo(String bdRegistrationNo) {
		this.bdRegistrationNo = bdRegistrationNo;
	}

	public String getCompanyAddress() {
		return this.companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Instance getCurrentEnvir() {
		return currentEnvir;
	}

	public void setCurrentEnvir(Instance currentEnvir) {
		this.currentEnvir = currentEnvir;
	}

	public String getFullCompanyName() {
		return this.fullCompanyName;
	}

	public void setFullCompanyName(String fullCompanyName) {
		this.fullCompanyName = fullCompanyName;
	}

	public String getParticipantNameNls() {
		return this.participantNameNls;
	}

	public void setParticipantNameNls(String participantNameNls) {
		this.participantNameNls = participantNameNls;
	}

	public String getParticipantNameEn() {
		return this.participantNameEn;
	}

	public void setParticipantNameEn(String participantNameEn) {
		this.participantNameEn = participantNameEn;
	}

	public ParticipantStatus getStatus() {
		return status;
	}

	public void setStatus(ParticipantStatus status) {
		this.status = status;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public EwalletParticipant getEwalletParticipant() {
		return this.ewalletParticipant;
	}

	public void setEwalletParticipant(EwalletParticipant ewalletParticipant) {
		this.ewalletParticipant = ewalletParticipant;
	}

	public List<EwpDisputeContact> getEwpDisputeContacts() {
		return this.ewpDisputeContacts;
	}

	public void setEwpDisputeContacts(List<EwpDisputeContact> ewpDisputeContacts) {
		this.ewpDisputeContacts = ewpDisputeContacts;
	}

	public String getNotificationEmail() {
		return notificationEmail;
	}

	public void setNotificationEmail(String notificationEmail) {
		this.notificationEmail = notificationEmail;
	}

	public EwpDisputeContact addEwpDisputeContact(EwpDisputeContact ewpDisputeContact) {
		getEwpDisputeContacts().add(ewpDisputeContact);
		ewpDisputeContact.setEwpCompanyForm(this);

		return ewpDisputeContact;
	}

	public EwpDisputeContact removeEwpDisputeContact(EwpDisputeContact ewpDisputeContact) {
		getEwpDisputeContacts().remove(ewpDisputeContact);
		ewpDisputeContact.setEwpCompanyForm(null);

		return ewpDisputeContact;
	}

	public List<EwpKeyPerson> getEwpKeyPersons() {
		return this.ewpKeyPersons;
	}

	public void setEwpKeyPersons(List<EwpKeyPerson> ewpKeyPersons) {
		this.ewpKeyPersons = ewpKeyPersons;
	}

	public EwpKeyPerson addEwpKeyPerson(EwpKeyPerson ewpKeyPerson) {
		getEwpKeyPersons().add(ewpKeyPerson);
		ewpKeyPerson.setEwpCompanyForm(this);

		return ewpKeyPerson;
	}

	public EwpKeyPerson removeEwpKeyPerson(EwpKeyPerson ewpKeyPerson) {
		getEwpKeyPersons().remove(ewpKeyPerson);
		ewpKeyPerson.setEwpCompanyForm(null);

		return ewpKeyPerson;
	}

	public List<EwpOwner> getEwpOwners() {
		return this.ewpOwners;
	}

	public void setEwpOwners(List<EwpOwner> ewpOwners) {
		this.ewpOwners = ewpOwners;
	}

	public EwpOwner addEwpOwner(EwpOwner ewpOwner) {
		getEwpOwners().add(ewpOwner);
		ewpOwner.setEwpCompanyForm(this);

		return ewpOwner;
	}

	public EwpOwner removeEwpOwner(EwpOwner ewpOwner) {
		getEwpOwners().remove(ewpOwner);
		ewpOwner.setEwpCompanyForm(null);

		return ewpOwner;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}