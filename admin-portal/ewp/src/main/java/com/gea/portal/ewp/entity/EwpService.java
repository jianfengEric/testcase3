package com.gea.portal.ewp.entity;

import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ewp_service database table.
 * 
 */
@Entity
@Table(name="ewp_service")
@NamedQuery(name="EwpService.findAll", query="SELECT e FROM EwpService e")
public class EwpService implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Column(name="mark_up")
	private BigDecimal markUp;

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

	//bi-directional many-to-one association to BaseService
	@Column(name="service_id")
	private Long baseServiceId;

	@OneToMany(cascade =CascadeType.PERSIST, mappedBy = "ewpService")
	private List<EwpServiceConfig> ewpServiceConfig;

	@OneToMany(mappedBy = "ewpService")
	private List<EwpServiceMoneyPool> ewpMoneyPool;

	@Column(name = "service_status")
//	@Enumerated(EnumType.STRING)
	private String serviceStatus;

	@Column(name = "chg_ver_no")
	private Integer changeVersionNo;

	public EwpService() {
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

	public Instance getCurrentEnvir() {
		return currentEnvir;
	}

	public void setCurrentEnvir(Instance currentEnvir) {
		this.currentEnvir = currentEnvir;
	}

	public BigDecimal getMarkUp() {
		return this.markUp;
	}

	public void setMarkUp(BigDecimal markUp) {
		this.markUp = markUp;
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

	public List<EwpServiceConfig> getEwpServiceConfig() {
		return ewpServiceConfig;
	}

	public void setEwpServiceConfig(List<EwpServiceConfig> ewpServiceConfig) {
		this.ewpServiceConfig = ewpServiceConfig;
	}

	public List<EwpServiceMoneyPool> getEwpMoneyPool() {
		return ewpMoneyPool;
	}

	public void setEwpMoneyPool(List<EwpServiceMoneyPool> ewpMoneyPool) {
		this.ewpMoneyPool = ewpMoneyPool;
	}


	public Integer getChangeVersionNo() {
		return changeVersionNo;
	}

	public void setChangeVersionNo(Integer changeVersionNo) {
		this.changeVersionNo = changeVersionNo;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public Long getBaseServiceId() {
		return baseServiceId;
	}

	public void setBaseServiceId(Long baseServiceId) {
		this.baseServiceId = baseServiceId;
	}
}