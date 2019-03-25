package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ewp_section_tab_field database table.
 * 
 */
@Entity
@Table(name="ewp_section_tab_field")
@NamedQuery(name="EwpSectionTabField.findAll", query="SELECT e FROM EwpSectionTabField e")
public class EwpSectionTabField implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="field_label")
	private String fieldLabel;

	private String status;

	//bi-directional many-to-one association to EwpSectionTab
	@ManyToOne
	@JoinColumn(name="section_tab_id")
	private EwpSectionTab ewpSectionTab;

	//bi-directional many-to-one association to RoleSectionTabField
	@OneToMany(mappedBy="ewpSectionTabField")
	private List<RoleSectionTabField> roleSectionTabFields;

	public EwpSectionTabField() {
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFieldLabel() {
		return this.fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EwpSectionTab getEwpSectionTab() {
		return this.ewpSectionTab;
	}

	public void setEwpSectionTab(EwpSectionTab ewpSectionTab) {
		this.ewpSectionTab = ewpSectionTab;
	}

	public List<RoleSectionTabField> getRoleSectionTabFields() {
		return this.roleSectionTabFields;
	}

	public void setRoleSectionTabFields(List<RoleSectionTabField> roleSectionTabFields) {
		this.roleSectionTabFields = roleSectionTabFields;
	}

	public RoleSectionTabField addRoleSectionTabField(RoleSectionTabField roleSectionTabField) {
		getRoleSectionTabFields().add(roleSectionTabField);
		roleSectionTabField.setEwpSectionTabField(this);

		return roleSectionTabField;
	}

	public RoleSectionTabField removeRoleSectionTabField(RoleSectionTabField roleSectionTabField) {
		getRoleSectionTabFields().remove(roleSectionTabField);
		roleSectionTabField.setEwpSectionTabField(null);

		return roleSectionTabField;
	}

}