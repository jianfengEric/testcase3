package com.tng.portal.ana.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Stephen on 2016/11/10.
 */
@Entity
@Table(name="ANA_ROLE_PERMISSION")
@IdClass(AnaRolePermissionKey.class)
@JsonInclude(Include.NON_NULL) 
public class AnaRolePermission implements Serializable{
	
	@Id
    @Column(name = "role_id")
	private Long roleId;

	@Id
	@Column(name = "PERMISSION_ID")
	private Integer permissionId;

	@Id
	@Column(name = "FUNCTION_CODE")
	private String functionCode;

	@Column(name = "PERMISSION_NAME")
	private String permissionName;

	@ApiModelProperty(value="update by accountid")
	@Column(name = "access_right")
	private String accessRight;

	@Version
	@Column(name = "optlock_ver")
	private long optlockver; //throws OptimisticLockException

	@ApiModelProperty(value="update by accountid")
	@Column(name = "update_by_acc_id")
	private String updateAccountid;

	@ApiModelProperty(value="creator by accountid")
	@Column(name = "creator_acc_id")
	private String creatorAccountid;

	@ApiModelProperty(value="update date")
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name = "update_date")
	private Date updateDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	@JsonIgnore
	private Date createDate;

	@Column(name = "BEFORE_ACTIVE")
	private String before;

	@Column(name = "AFTER_ACTIVE")
	private String after;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}

	public long getOptlockver() {
		return optlockver;
	}

	public void setOptlockver(long optlockver) {
		this.optlockver = optlockver;
	}

	public String getUpdateAccountid() {
		return updateAccountid;
	}

	public void setUpdateAccountid(String updateAccountid) {
		this.updateAccountid = updateAccountid;
	}

	public String getCreatorAccountid() {
		return creatorAccountid;
	}

	public void setCreatorAccountid(String creatorAccountid) {
		this.creatorAccountid = creatorAccountid;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}
}