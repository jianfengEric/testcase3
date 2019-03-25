package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ewp_section_tab database table.
 * 
 */
@Entity
@Table(name="ewp_section_tab")
@NamedQuery(name="EwpSectionTab.findAll", query="SELECT e FROM EwpSectionTab e")
public class EwpSectionTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	private String code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	private String name;

	private String status;

	//bi-directional many-to-one association to EwpSectionTabField
	@OneToMany(mappedBy="ewpSectionTab")
	private List<EwpSectionTabField> ewpSectionTabFields;

	//bi-directional many-to-one association to RoleSectionTab
	@OneToMany(mappedBy="ewpSectionTab")
	private List<RoleSectionTab> roleSectionTabs;

	public EwpSectionTab() {
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<EwpSectionTabField> getEwpSectionTabFields() {
		return this.ewpSectionTabFields;
	}

	public void setEwpSectionTabFields(List<EwpSectionTabField> ewpSectionTabFields) {
		this.ewpSectionTabFields = ewpSectionTabFields;
	}

	public EwpSectionTabField addEwpSectionTabField(EwpSectionTabField ewpSectionTabField) {
		getEwpSectionTabFields().add(ewpSectionTabField);
		ewpSectionTabField.setEwpSectionTab(this);

		return ewpSectionTabField;
	}

	public EwpSectionTabField removeEwpSectionTabField(EwpSectionTabField ewpSectionTabField) {
		getEwpSectionTabFields().remove(ewpSectionTabField);
		ewpSectionTabField.setEwpSectionTab(null);

		return ewpSectionTabField;
	}

	public List<RoleSectionTab> getRoleSectionTabs() {
		return this.roleSectionTabs;
	}

	public void setRoleSectionTabs(List<RoleSectionTab> roleSectionTabs) {
		this.roleSectionTabs = roleSectionTabs;
	}

	public RoleSectionTab addRoleSectionTab(RoleSectionTab roleSectionTab) {
		getRoleSectionTabs().add(roleSectionTab);
		roleSectionTab.setEwpSectionTab(this);

		return roleSectionTab;
	}

	public RoleSectionTab removeRoleSectionTab(RoleSectionTab roleSectionTab) {
		getRoleSectionTabs().remove(roleSectionTab);
		roleSectionTab.setEwpSectionTab(null);

		return roleSectionTab;
	}

}