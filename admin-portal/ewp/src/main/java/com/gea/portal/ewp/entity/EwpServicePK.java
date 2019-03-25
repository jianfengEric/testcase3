package com.gea.portal.ewp.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ewp_service database table.
 * 
 */
@Embeddable
public class EwpServicePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private long id;

	@Column(name="participant_id", insertable=false, updatable=false)
	private String participantId;

	@Column(name="service_id", insertable=false, updatable=false)
	private String serviceId;

	public EwpServicePK() {
	}
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getParticipantId() {
		return this.participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public String getServiceId() {
		return this.serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}