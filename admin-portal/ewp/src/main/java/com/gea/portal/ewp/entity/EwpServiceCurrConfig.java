package com.gea.portal.ewp.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ewp_service_curr_config database table.
 * 
 */
@Entity
@Table(name="ewp_service_curr_config")
@NamedQuery(name="EwpServiceCurrConfig.findAll", query="SELECT e FROM EwpServiceCurrConfig e")
public class EwpServiceCurrConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="currency")
	private String currency;

	@Column(name="admin_fee")
	private BigDecimal adminFee;
	
	@Column(name="change_name_admin_fee")
	private BigDecimal changeNameAdminFee;
	
	@Column(name="cancel_admin_fee")
	private BigDecimal cancelAdminFee;

	@Column(name="enable")
	private Boolean enable;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ewp_service_config_id")
	private EwpServiceConfig ewpServiceConfig;
	
	public EwpServiceCurrConfig() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAdminFee() {
		return adminFee;
	}

	public void setAdminFee(BigDecimal adminFee) {
		this.adminFee = adminFee;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public EwpServiceConfig getEwpServiceConfig() {
		return ewpServiceConfig;
	}

	public void setEwpServiceConfig(EwpServiceConfig ewpServiceConfig) {
		this.ewpServiceConfig = ewpServiceConfig;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getChangeNameAdminFee() {
		return changeNameAdminFee;
	}

	public void setChangeNameAdminFee(BigDecimal changeNameAdminFee) {
		this.changeNameAdminFee = changeNameAdminFee;
	}

	public BigDecimal getCancelAdminFee() {
		return cancelAdminFee;
	}

	public void setCancelAdminFee(BigDecimal cancelAdminFee) {
		this.cancelAdminFee = cancelAdminFee;
	}
	
	

}