package com.gea.portal.ewp.entity;

import com.tng.portal.common.enumeration.ParticipantStatus;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ewallet_participant database table.
 * 
 */
@Entity
@Table(name="ewallet_participant")
@NamedQuery(name="EwalletParticipant.findAll", query="SELECT e FROM EwalletParticipant e")
public class EwalletParticipant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="create_by")
	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="gea_ref_id")
	private String geaRefId;
	
	@Column(name="gea_pre_pid")
	private Long geaPrePid;
	
	@Column(name="gea_prd_pid")
	private Long geaPrdPid;

	@Column(name = "pre_status")
	@Enumerated(EnumType.STRING)
	private ParticipantStatus preStatus;

	@Column(name = "prod_status")
	@Enumerated(EnumType.STRING)
	private ParticipantStatus prodStatus;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	//bi-directional many-to-one association to EwpCompanyForm
	@OneToMany(cascade =CascadeType.PERSIST, mappedBy="ewalletParticipant")
	@OrderBy(" createDate desc ")
	private List<EwpCompanyForm> ewpCompanyForms;

	//bi-directional many-to-one association to EwpGatewayConfig
	@OneToMany(mappedBy="ewalletParticipant")
	@OrderBy(" createDate desc ")
	private List<EwpGatewayConfig> ewpGatewayConfigs;

	//bi-directional many-to-one association to EwpService
	@OneToMany(mappedBy="ewalletParticipant")
//	@Where(clause = "status='ACT'")
//	@OrderBy(" baseService.id asc ")
	@OrderBy(" baseServiceId asc ")
	private List<EwpService> ewpServices;

	//bi-directional many-to-one association to EwpStatusChange
	@OneToMany(mappedBy="ewalletParticipant")
	@OrderBy(" createDate desc ")
	private List<EwpStatusChange> ewpStatusChanges;

	@OneToMany(mappedBy="ewalletParticipant")
	@OrderBy(" createDate desc ")
	private List<RequestApproval> requestApproval;

	@OneToMany(mappedBy="ewalletParticipant")
	@OrderBy(" createDate desc ")
	private List<EwpServiceMoneyPool> ewpServiceMoneyPool;

	public EwalletParticipant() {
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

	public String getGeaRefId() {
		return this.geaRefId;
	}

	public void setGeaRefId(String geaRefId) {
		this.geaRefId = geaRefId;
	}

	public ParticipantStatus getPreStatus() {
		return preStatus;
	}

	public void setPreStatus(ParticipantStatus preStatus) {
		this.preStatus = preStatus;
	}

	public ParticipantStatus getProdStatus() {
		return prodStatus;
	}

	public void setProdStatus(ParticipantStatus prodStatus) {
		this.prodStatus = prodStatus;
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

	public List<EwpCompanyForm> getEwpCompanyForms() {
		return this.ewpCompanyForms;
	}

	public void setEwpCompanyForms(List<EwpCompanyForm> ewpCompanyForms) {
		this.ewpCompanyForms = ewpCompanyForms;
	}

	public EwpCompanyForm addEwpCompanyForm(EwpCompanyForm ewpCompanyForm) {
		getEwpCompanyForms().add(ewpCompanyForm);
		ewpCompanyForm.setEwalletParticipant(this);

		return ewpCompanyForm;
	}

	public EwpCompanyForm removeEwpCompanyForm(EwpCompanyForm ewpCompanyForm) {
		getEwpCompanyForms().remove(ewpCompanyForm);
		ewpCompanyForm.setEwalletParticipant(null);

		return ewpCompanyForm;
	}

	public List<EwpGatewayConfig> getEwpGatewayConfigs() {
		return this.ewpGatewayConfigs;
	}

	public void setEwpGatewayConfigs(List<EwpGatewayConfig> ewpGatewayConfigs) {
		this.ewpGatewayConfigs = ewpGatewayConfigs;
	}

	public EwpGatewayConfig addEwpGatewayConfig(EwpGatewayConfig ewpGatewayConfig) {
		getEwpGatewayConfigs().add(ewpGatewayConfig);
		ewpGatewayConfig.setEwalletParticipant(this);

		return ewpGatewayConfig;
	}

	public EwpGatewayConfig removeEwpGatewayConfig(EwpGatewayConfig ewpGatewayConfig) {
		getEwpGatewayConfigs().remove(ewpGatewayConfig);
		ewpGatewayConfig.setEwalletParticipant(null);

		return ewpGatewayConfig;
	}

	public List<EwpService> getEwpServices() {
		return this.ewpServices;
	}

	public void setEwpServices(List<EwpService> ewpServices) {
		this.ewpServices = ewpServices;
	}

	public EwpService addEwpService(EwpService ewpService) {
		getEwpServices().add(ewpService);
		ewpService.setEwalletParticipant(this);

		return ewpService;
	}

	public EwpService removeEwpService(EwpService ewpService) {
		getEwpServices().remove(ewpService);
		ewpService.setEwalletParticipant(null);

		return ewpService;
	}

	public List<EwpStatusChange> getEwpStatusChanges() {
		return this.ewpStatusChanges;
	}

	public void setEwpStatusChanges(List<EwpStatusChange> ewpStatusChanges) {
		this.ewpStatusChanges = ewpStatusChanges;
	}

	public EwpStatusChange addEwpStatusChange(EwpStatusChange ewpStatusChange) {
		getEwpStatusChanges().add(ewpStatusChange);
		ewpStatusChange.setEwalletParticipant(this);

		return ewpStatusChange;
	}

	public EwpStatusChange removeEwpStatusChange(EwpStatusChange ewpStatusChange) {
		getEwpStatusChanges().remove(ewpStatusChange);
		ewpStatusChange.setEwalletParticipant(null);

		return ewpStatusChange;
	}

	public List<RequestApproval> getRequestApproval() {
		return requestApproval;
	}

	public void setRequestApproval(List<RequestApproval> requestApproval) {
		this.requestApproval = requestApproval;
	}

	public List<EwpServiceMoneyPool> getEwpServiceMoneyPool() {
		return ewpServiceMoneyPool;
	}

	public void setEwpServiceMoneyPool(List<EwpServiceMoneyPool> ewpServiceMoneyPool) {
		this.ewpServiceMoneyPool = ewpServiceMoneyPool;
	}


	public Long getGeaPrePid() {
		return geaPrePid;
	}


	public void setGeaPrePid(Long geaPrePid) {
		this.geaPrePid = geaPrePid;
	}


	public Long getGeaPrdPid() {
		return geaPrdPid;
	}


	public void setGeaPrdPid(Long geaPrdPid) {
		this.geaPrdPid = geaPrdPid;
	}

}