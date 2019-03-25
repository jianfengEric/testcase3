package com.tng.portal.common.dto.ewp;

import java.io.Serializable;

/**
 * Created by Jimmy on 2018/8/30.
 */
public class MaterialDto implements Serializable {

    private String materialType;
    private String materialFilename;
    private String materialDesc;
    private String filePath;
    private Long ewpFormId;
    private Long ewpOwnerId;
    private Long ewpKeyPersonId;
    private Long ewpDisputeContactId;

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

    public Long getEwpFormId() {
        return ewpFormId;
    }

    public void setEwpFormId(Long ewpFormId) {
        this.ewpFormId = ewpFormId;
    }

    public Long getEwpOwnerId() {
        return ewpOwnerId;
    }

    public void setEwpOwnerId(Long ewpOwnerId) {
        this.ewpOwnerId = ewpOwnerId;
    }

    public Long getEwpKeyPersonId() {
        return ewpKeyPersonId;
    }

    public void setEwpKeyPersonId(Long ewpKeyPersonId) {
        this.ewpKeyPersonId = ewpKeyPersonId;
    }

    public Long getEwpDisputeContactId() {
        return ewpDisputeContactId;
    }

    public void setEwpDisputeContactId(Long ewpDisputeContactId) {
        this.ewpDisputeContactId = ewpDisputeContactId;
    }

}
