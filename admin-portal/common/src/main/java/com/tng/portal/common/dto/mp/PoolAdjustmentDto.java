package com.tng.portal.common.dto.mp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jimmy on 2018/9/3.
 */
public class PoolAdjustmentDto implements Serializable {

    private String geaParticipantRefId;
    private String moneyPoolId;
    private Integer adjustType;
    private BigDecimal amount;
    private String requestRemark;
    private String status;
    private String currentEnvir;
    private String geaTxRefNo;
    private List<MaterialDto> materialDtos;

    public String getGeaParticipantRefId() {
        return geaParticipantRefId;
    }

    public void setGeaParticipantRefId(String geaParticipantRefId) {
        this.geaParticipantRefId = geaParticipantRefId;
    }

    public String getMoneyPoolId() {
        return moneyPoolId;
    }

    public void setMoneyPoolId(String moneyPoolId) {
        this.moneyPoolId = moneyPoolId;
    }

    public Integer getAdjustType() {
        return adjustType;
    }

    public void setAdjustType(Integer adjustType) {
        this.adjustType = adjustType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentEnvir() {
        return currentEnvir;
    }

    public void setCurrentEnvir(String currentEnvir) {
        this.currentEnvir = currentEnvir;
    }

    public List<MaterialDto> getMaterialDtos() {
        return materialDtos;
    }

    public void setMaterialDtos(List<MaterialDto> materialDtos) {
        this.materialDtos = materialDtos;
    }

	public String getGeaTxRefNo() {
		return geaTxRefNo;
	}

	public void setGeaTxRefNo(String geaTxRefNo) {
		this.geaTxRefNo = geaTxRefNo;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
}
