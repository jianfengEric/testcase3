package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the role_section_tab database table.
 * 
 */
@Entity
@Table(name="role_section_tab")
@NamedQuery(name="RoleSectionTab.findAll", query="SELECT r FROM RoleSectionTab r")
public class RoleSectionTab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;

	@Column(name="access_right")
	private String accessRight;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to EwpSectionTab
	@ManyToOne
	@JoinColumn(name="ewp_tab_id")
	private EwpSectionTab ewpSectionTab;

	public RoleSectionTab() {
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessRight() {
		return this.accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
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

	public EwpSectionTab getEwpSectionTab() {
		return this.ewpSectionTab;
	}

	public void setEwpSectionTab(EwpSectionTab ewpSectionTab) {
		this.ewpSectionTab = ewpSectionTab;
	}

}