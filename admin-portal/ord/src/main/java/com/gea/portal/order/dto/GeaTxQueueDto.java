package com.gea.portal.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gea.portal.order.util.GeaTXStatusEnum;
import com.gea.portal.order.util.OrderStatusEnum;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeaTxQueueDto {

	private static final Logger logger = LoggerFactory.getLogger(GeaTxQueueDto.class);

	@SerializedName(value = "geaRefId")
	private String transferId;

	@SerializedName(value = "pId")
	private String participantId;

	@SerializedName(value = "participantName")
	private String participantName;

	@SerializedName(value = "recordType")
	private String serviceType;

	@SerializedName(value = "createDatetime")
	private String submissionDateTime;

	@SerializedName(value = "updateDatetime")
	private String updateTime;

	@SerializedName(value = "status")
	private String status;

	@SerializedName(value = "geaStatus")
	private String geaStatus;

	@SerializedName(value = "csRemark")
	private String remark;
	
	private String moneyPoolId;

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSubmissionDateTime() {
		return submissionDateTime;
	}

	public void setSubmissionDateTime(String submissionDateTime) {
		this.submissionDateTime = submissionDateTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		try {
			this.status = OrderStatusEnum.valueOf(status).getDesc();
		} catch (IllegalArgumentException e){
			logger.error(status + "'s order status transform has exception");
		}
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGeaStatus() {
		return geaStatus;
	}

	public void setGeaStatus(String geaStatus) {
		try {
			if(StringUtils.isNotBlank(geaStatus)){
				this.geaStatus = GeaTXStatusEnum.valueOf(geaStatus).getDesc();
			}
		} catch (IllegalArgumentException e){
			logger.error(status + "'s gea status transform has exception");
		}
	}

	public String getMoneyPoolId() {
		return moneyPoolId;
	}

	public void setMoneyPoolId(String moneyPoolId) {
		this.moneyPoolId = moneyPoolId;
	}
}
