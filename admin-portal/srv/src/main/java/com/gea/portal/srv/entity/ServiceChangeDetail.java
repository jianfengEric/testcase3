package com.gea.portal.srv.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="service_change_detail")
public class ServiceChangeDetail implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="batch_id")
	private ServiceBatchChgReq serviceBatchChgReq;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="service_id")
	private BaseService baseService;

	@Column(name="to_mark_up")
	private String toMarkUp;

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

	public String getToMarkUp() {
		return toMarkUp;
	}

	public void setToMarkUp(String toMarkUp) {
		this.toMarkUp = toMarkUp;
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

	public ServiceBatchChgReq getServiceBatchChgReq() {
		return serviceBatchChgReq;
	}

	public void setServiceBatchChgReq(ServiceBatchChgReq serviceBatchChgReq) {
		this.serviceBatchChgReq = serviceBatchChgReq;
	}

	public com.gea.portal.srv.entity.BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(com.gea.portal.srv.entity.BaseService baseService) {
		this.baseService = baseService;
	}
}