package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="ewp_key_person")
@NamedQuery(name="EwpKeyPerson.findAll", query="SELECT e FROM EwpKeyPerson e")
public class EwpKeyPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="department_name")
	private String departmentName;

	@Column(name="email")
	private String email;

	@Column(name="mobile_sms")
	private Boolean mobileSms;

	@Column(name="person_name_en")
	private String personNameEn;

	@Column(name="person_name_zh_cn")
	private String personNameZhCn;

	@Column(name="phone_mobile")
	private String phoneMobile;

	@Column(name="phone_office")
	private String phoneOffice;

	@Column(name="role_name")
	private String roleName;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to EwpCompanyForm
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ewp_form_id")
	private EwpCompanyForm ewpCompanyForm;

	public EwpKeyPerson() {
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDepartmentName() {
		return this.departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getMobileSms() {
		return mobileSms;
	}

	public void setMobileSms(Boolean mobileSms) {
		this.mobileSms = mobileSms;
	}

	public String getPersonNameEn() {
		return this.personNameEn;
	}

	public void setPersonNameEn(String personNameEn) {
		this.personNameEn = personNameEn;
	}

	public String getPersonNameZhCn() {
		return this.personNameZhCn;
	}

	public void setPersonNameZhCn(String personNameZhCn) {
		this.personNameZhCn = personNameZhCn;
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

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

}