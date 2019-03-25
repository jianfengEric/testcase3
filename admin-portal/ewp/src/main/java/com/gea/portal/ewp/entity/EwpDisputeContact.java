package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="ewp_dispute_contact")
@NamedQuery(name="EwpDisputeContact.findAll", query="SELECT e FROM EwpDisputeContact e")
public class EwpDisputeContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="role_name")
	private String roleName;

	@Column(name="department_name")
	private String departmentName;

	@Column(name="contact_name")
	private String contactName;

	@Column(name="contact_name_nls")
	private String contactNameNls;

	@Column(name="contact_person_type")
	private String contactPersonType;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="mobile_sms")
	private Boolean mobileSms;

	@Column(name="phone_mobile")
	private String phoneMobile;

	@Column(name="phone_office")
	private String phoneOffice;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to EwpCompanyForm
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ewp_form_id")
	private EwpCompanyForm ewpCompanyForm;

	@Column(name = "email")
	private String email;

	public EwpDisputeContact() {
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactNameNls() {
		return this.contactNameNls;
	}

	public void setContactNameNls(String contactNameNls) {
		this.contactNameNls = contactNameNls;
	}

	public String getContactPersonType() {
		return this.contactPersonType;
	}

	public void setContactPersonType(String contactPersonType) {
		this.contactPersonType = contactPersonType;
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

	public String getPhoneMobile() {
		return this.phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public String getPhoneOffice() {
		return this.phoneOffice;
	}

	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
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

	public EwpCompanyForm getEwpCompanyForm() {
		return this.ewpCompanyForm;
	}

	public void setEwpCompanyForm(EwpCompanyForm ewpCompanyForm) {
		this.ewpCompanyForm = ewpCompanyForm;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Boolean getMobileSms() {
		return mobileSms;
	}

	public void setMobileSms(Boolean mobileSms) {
		this.mobileSms = mobileSms;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}