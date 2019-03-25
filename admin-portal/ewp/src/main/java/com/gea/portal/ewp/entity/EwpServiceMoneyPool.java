package com.gea.portal.ewp.entity;

import com.tng.portal.common.enumeration.ParticipantStatus;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the ewp_owner database table.
 * 
 */
@Entity
@Table(name="ewp_service_money_pool")
//@NamedQuery(name="EwpMoneyPool.findAll", query="SELECT e FROM EwpMoneyPool e")
public class EwpServiceMoneyPool implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ParticipantStatus status;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="create_by")
	private String createBy;

	@Column(name="update_by")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ewp_service_id")
	private EwpService ewpService;
	
	@Column(name = "gea_moneypool_ref_id")
	private String geaMoneyPoolRefId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="participant_id")
	private EwalletParticipant ewalletParticipant;


	public EwpServiceMoneyPool() {
	}


	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ParticipantStatus getStatus() {
		return status;
	}

	public void setStatus(ParticipantStatus status) {
		this.status = status;
	}

	public Instance getCurrentEnvir() {
		return currentEnvir;
	}

	public void setCurrentEnvir(Instance currentEnvir) {
		this.currentEnvir = currentEnvir;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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

	public String getGeaMoneyPoolRefId() {
		return geaMoneyPoolRefId;
	}

	public void setGeaMoneyPoolRefId(String geaMoneyPoolRefId) {
		this.geaMoneyPoolRefId = geaMoneyPoolRefId;
	}

	public EwalletParticipant getEwalletParticipant() {
		return ewalletParticipant;
	}

	public void setEwalletParticipant(EwalletParticipant ewalletParticipant) {
		this.ewalletParticipant = ewalletParticipant;
	}
}