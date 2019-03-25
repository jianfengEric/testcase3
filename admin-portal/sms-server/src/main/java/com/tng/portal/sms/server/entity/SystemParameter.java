package com.tng.portal.sms.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the system_parameter database table.
 * 
 */
@Entity
@Table(name = "system_parameter")
@JsonInclude(Include.NON_NULL)
public class SystemParameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="param_id", unique=true, nullable=false, length=40)
	private String paramId;
	
	@Column(name="param_cat", length=50)
	private String paramCat;

	@Column(name="param_value", length=50)
	private String paramValue;
	
	@Column(length=200)
	private String description;
	
	@Column(length=10)
	private String status;
	
	@Column(name="display_order")
	private Long displayOrder;

	@Column(name="create_by", length=50)
	private String createBy;
	@JsonIgnore
	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="update_by", length=50)
	private String updateBy;
	
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date updateDate;

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public String getParamCat() {
		return paramCat;
	}

	public void setParamCat(String paramCat) {
		this.paramCat = paramCat;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}