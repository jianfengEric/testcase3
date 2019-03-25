package com.gea.portal.apv.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Owen on 2018/9/13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalResponseDto implements Serializable{
    private String requestUserId;
    private String requestUserName;
    private String requestDate;
    private String participantRefId;
    private String participantName;
    private String approvalStatus;
    private String approvalUserId;
    private String approvalUserName;
    private String approvalDate;
    private String approvalType;
    private String instance;
    private String deploymentStatus;
    private String deploymentDate;
    private String realDeploymentDate;
    private String approvalRemark;
    private String deploymentType;

    public String getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(String requestUserId) {
        this.requestUserId = requestUserId;
    }

    public String getParticipantRefId() {
        return participantRefId;
    }

    public void setParticipantRefId(String participantRefId) {
        this.participantRefId = participantRefId;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(String deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getDeploymentDate() {
        return deploymentDate;
    }

    public void setDeploymentDate(String deploymentDate) {
        this.deploymentDate = deploymentDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(String approvalType) {
        this.approvalType = approvalType;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

	public String getApprovalRemark() {
		return approvalRemark;
	}

	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}

	public String getRequestUserName() {
		return requestUserName;
	}

	public void setRequestUserName(String requestUserName) {
		this.requestUserName = requestUserName;
	}

	public String getApprovalUserName() {
		return approvalUserName;
	}

	public void setApprovalUserName(String approvalUserName) {
		this.approvalUserName = approvalUserName;
	}

    public String getRealDeploymentDate() {
        return realDeploymentDate;
    }

    public void setRealDeploymentDate(String realDeploymentDate) {
        this.realDeploymentDate = realDeploymentDate;
    }

    public String getDeploymentType() {
		return deploymentType;
	}

	public void setDeploymentType(String deploymentType) {
		this.deploymentType = deploymentType;
	}
}
