package com.gea.portal.mp.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/31.
 */
@Entity
@Table(name = "ewp_material")
public class EwpMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "material_type")
    private String materialType;

    @Column(name = "material_filename")
    private String materialFilename;

    @Column(name = "material_desc")
    private String materialDesc;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "ewp_adjust_id")
    private Long ewpPoolAdjustmentId;

    @Column(name = "ewp_deposit_cachout_id")
    private Long ewpPoolDepositCashOutId;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getEwpPoolAdjustmentId() {
        return ewpPoolAdjustmentId;
    }

    public void setEwpPoolAdjustmentId(Long ewpPoolAdjustmentId) {
        this.ewpPoolAdjustmentId = ewpPoolAdjustmentId;
    }

    public Long getEwpPoolDepositCashOutId() {
        return ewpPoolDepositCashOutId;
    }

    public void setEwpPoolDepositCashOutId(Long ewpPoolDepositCashOutId) {
        this.ewpPoolDepositCashOutId = ewpPoolDepositCashOutId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
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
}
