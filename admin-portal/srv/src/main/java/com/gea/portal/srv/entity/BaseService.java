package com.gea.portal.srv.entity;

import com.tng.portal.common.enumeration.BaseServerStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name="base_service")
public class BaseService implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="gea_service_ref_id")
	private String geaServiceRefId;

	@Column(name="service_code")
	private String serviceCode;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private BaseServerStatus status;

	@Column(name="name_en")
	private String nameEn;

	@Column(name="name_zh_hk")
	private String nameZhHk;

	@Column(name="name_zh_cn")
	private String nameZhCn;

	@Column(name="description")
	private String description;

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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public BaseServerStatus getStatus() {
		return status;
	}

	public void setStatus(BaseServerStatus status) {
		this.status = status;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameZhHk() {
		return nameZhHk;
	}

	public void setNameZhHk(String nameZhHk) {
		this.nameZhHk = nameZhHk;
	}

	public String getNameZhCn() {
		return nameZhCn;
	}

	public void setNameZhCn(String nameZhCn) {
		this.nameZhCn = nameZhCn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getGeaServiceRefId() {
		return geaServiceRefId;
	}

	public void setGeaServiceRefId(String geaServiceRefId) {
		this.geaServiceRefId = geaServiceRefId;
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
}