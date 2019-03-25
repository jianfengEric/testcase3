package com.gea.portal.srv.entity;

import com.tng.portal.common.enumeration.BaseServerStatus;
import com.tng.portal.common.enumeration.Instance;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="service_config")
public class ServiceConfig implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="service_id")
	private BaseService baseService;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private BaseServerStatus status;

	@Column(name="markup")
	private String markup;

	@Column(name="current_envir")
	@Enumerated(EnumType.STRING)
	private Instance currentEnvir;

	@Column(name="create_date", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name="create_by")
	private String createBy;

	@Column(name="update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name="update_by")
	private String updateBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BaseServerStatus getStatus() {
		return status;
	}

	public void setStatus(BaseServerStatus status) {
		this.status = status;
	}

	public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}