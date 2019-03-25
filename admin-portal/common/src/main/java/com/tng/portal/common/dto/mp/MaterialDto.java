package com.tng.portal.common.dto.mp;

import java.io.Serializable;

/**
 * Created by Jimmy on 2018/9/3.
 */
public class MaterialDto implements Serializable {

    private String materialType;
    private String materialFilename;
    private String materialDesc;
    private String filePath;
    private String ewpPoolAdjustmentId;
    private String ewpPoolDepositCashOutId;

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getMaterialFilename() {
        return materialFilename;
    }

    public void setMaterialFilename(String materialFilename) {
        this.materialFilename = materialFilename;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEwpPoolAdjustmentId() {
        return ewpPoolAdjustmentId;
    }

    public void setEwpPoolAdjustmentId(String ewpPoolAdjustmentId) {
        this.ewpPoolAdjustmentId = ewpPoolAdjustmentId;
    }

    public String getEwpPoolDepositCashOutId() {
        return ewpPoolDepositCashOutId;
    }

    public void setEwpPoolDepositCashOutId(String ewpPoolDepositCashOutId) {
        this.ewpPoolDepositCashOutId = ewpPoolDepositCashOutId;
    }
}
