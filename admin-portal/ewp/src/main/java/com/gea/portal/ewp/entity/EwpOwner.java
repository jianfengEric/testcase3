package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ewp_owner database table.
 * 
 */
@Entity
@Table(name="ewp_owner")
@NamedQuery(name="EwpOwner.findAll", query="SELECT e FROM EwpOwner e")
public class EwpOwner implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="email")
	private String email;

	@Column(name="name_en")
	private String nameEn;

	@Column(name="name_nls")
	private String nameNls;

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

	public EwpOwner() {
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameNls() {
		return this.nameNls;
	}

	public void setNameNls(String nameNls) {
		this.nameNls = nameNls;
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