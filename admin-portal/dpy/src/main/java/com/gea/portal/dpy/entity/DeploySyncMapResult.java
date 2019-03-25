package com.gea.portal.dpy.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gea.portal.dpy.enumeration.MapDeployStatus;
import com.gea.portal.dpy.enumeration.MapServerAckStatus;

/**
 * Created by Eric on 2018/9/12.
 */

@Entity
@Table(name = "deploy_sync_map_result")
public class DeploySyncMapResult {
    private long id;
    private long deployQueueId;
    private String geaServerName;
    private String apiUrl;
    private String geaCurrentData;
    private String reqParameters;
    private String respCode;
    private String respCodeDesc;
    private int retryCount;
    private MapServerAckStatus serverAckStatus;
    private MapDeployStatus deployStatus;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    
    

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "deploy_queue_id", nullable = true)
    public long getDeployQueueId() {
        return deployQueueId;
    }

    public void setDeployQueueId(long deployQueueId) {
        this.deployQueueId = deployQueueId;
    }

    @Basic
    @Column(name = "gea_server_name", nullable = true, length = 45)
    public String getGeaServerName() {
        return geaServerName;
    }

    public void setGeaServerName(String targetServerName) {
        this.geaServerName = targetServerName;
    }

    @Basic
    @Column(name = "api_url", nullable = true, length = 250)
    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Basic
    @Column(name = "req_parameters", nullable = true, length = 1000)
    public String getReqParameters() {
        return reqParameters;
    }

    public void setReqParameters(String reqParameters) {
        this.reqParameters = reqParameters;
    }

    @Basic
    @Column(name = "resp_code", nullable = true, length = 45)
    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    @Basic
    @Column(name = "resp_code_desc", nullable = true, length = 250)
    public String getRespCodeDesc() {
        return respCodeDesc;
    }

    public void setRespCodeDesc(String respCodeDesc) {
        this.respCodeDesc = respCodeDesc;
    }

    @Basic
    @Column(name = "retry_count", nullable = true)
    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Basic
    @Column(name = "create_date", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "create_by", nullable = true, length = 45)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "update_date", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Basic
    @Column(name = "update_by", nullable = true, length = 45)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
	
    @Basic
    @Column(name = "gea_current_data", nullable = true, length = 1000)
	public String getGeaCurrentData() {
		return geaCurrentData;
	}

	public void setGeaCurrentData(String geaCurrentData) {
		this.geaCurrentData = geaCurrentData;
	}

	@Column(name = "server_ack_status")
	@Enumerated(EnumType.STRING)
	public MapServerAckStatus getServerAckStatus() {
		return serverAckStatus;
	}

	public void setServerAckStatus(MapServerAckStatus serverAckStatus) {
		this.serverAckStatus = serverAckStatus;
	}

	@Column(name = "deploy_status")
	@Enumerated(EnumType.STRING)
	public MapDeployStatus getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(MapDeployStatus deployStatus) {
		this.deployStatus = deployStatus;
	}

}
