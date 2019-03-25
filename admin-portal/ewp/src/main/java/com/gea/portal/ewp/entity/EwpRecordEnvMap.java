package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ewp_record_env_map database table.
 * 
 */
@Entity
@Table(name="ewp_record_env_map")
@NamedQuery(name="EwpRecordEnvMap.findAll", query="SELECT e FROM EwpRecordEnvMap e")
public class EwpRecordEnvMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

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

	//bi-directional many-to-one association to EwpDeployToProd
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="deploy_to_prod_id")
	private EwpDeployment ewpDeployToProd;

	//bi-directional many-to-one association to RequestApproval
	@ManyToOne
	@JoinColumn(name="preprod_req_apv_id")
	private RequestApproval preprodRequestApproval;

	//bi-directional many-to-one association to RequestApproval
	@ManyToOne
	@JoinColumn(name="prod_req_apv_id")
	private RequestApproval prodRequestApproval;

	@Column(name="participant_id")
	private Long participantId;

	public EwpRecordEnvMap() {
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

	public EwpDeployment getEwpDeployToProd() {
		return this.ewpDeployToProd;
	}

	public void setEwpDeployToProd(EwpDeployment ewpDeployToProd) {
		this.ewpDeployToProd = ewpDeployToProd;
	}

	public RequestApproval getPreprodRequestApproval() {
		return preprodRequestApproval;
	}

	public void setPreprodRequestApproval(RequestApproval preprodRequestApproval) {
		this.preprodRequestApproval = preprodRequestApproval;
	}

	public RequestApproval getProdRequestApproval() {
		return prodRequestApproval;
	}

	public void setProdRequestApproval(RequestApproval prodRequestApproval) {
		this.prodRequestApproval = prodRequestApproval;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}
}