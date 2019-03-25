package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ewp_service_config database table.
 * 
 */
@Entity
@Table(name="ewp_service_config")
@NamedQuery(name="EwpServiceConfig.findAll", query="SELECT e FROM EwpServiceConfig e")
public class EwpServiceConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="currency")
	private String currency;

	@Column(name="enable")
	private Boolean enable;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ewp_service_id")
	private EwpService ewpService;
	
	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="ewpServiceConfig")
	private List<EwpServiceCurrConfig> ewpServiceCurrConfig;

	public EwpServiceConfig() {
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

	public EwpService getEwpService() {
		return ewpService;
	}

	public void setEwpService(EwpService ewpService) {
		this.ewpService = ewpService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<EwpServiceCurrConfig> getEwpServiceCurrConfig() {
		return ewpServiceCurrConfig;
	}

	public void setEwpServiceCurrConfig(List<EwpServiceCurrConfig> ewpServiceCurrConfig) {
		this.ewpServiceCurrConfig = ewpServiceCurrConfig;
	}
	
}