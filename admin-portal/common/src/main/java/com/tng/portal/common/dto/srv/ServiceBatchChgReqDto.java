package com.tng.portal.common.dto.srv;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eric on 2018/11/27.
 */

public class ServiceBatchChgReqDto implements Serializable{
    private String instance;
    private String requestRemark;
    private Long requestApprovalId;
    private List<ServiceBatchDto> serviceBatchDtoList;
    private List<ServiceBatchDto> oldServiceBatchDtoList;

    public List<ServiceBatchDto> getOldServiceBatchDtoList() {
        return oldServiceBatchDtoList;
    }

    public void setOldServiceBatchDtoList(List<ServiceBatchDto> oldServiceBatchDtoList) {
        this.oldServiceBatchDtoList = oldServiceBatchDtoList;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public List<ServiceBatchDto> getServiceBatchDtoList() {
        return serviceBatchDtoList;
    }

    public void setServiceBatchDtoList(List<ServiceBatchDto> serviceBatchDtoList) {
        this.serviceBatchDtoList = serviceBatchDtoList;
    }

	public Long getRequestApprovalId() {
		return requestApprovalId;
	}

	public void setRequestApprovalId(Long requestApprovalId) {
		this.requestApprovalId = requestApprovalId;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}
