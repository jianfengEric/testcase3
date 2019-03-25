package com.gea.portal.ewp.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jimmy on 2018/8/29.
 */
@Entity
@Table(name="ewp_material")
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

    @Column(name = "ewp_form_id")
    private Long ewpFormId;

    @Column(name = "ewp_owener_id")
    private Long ewpOwnerId;

    @Column(name = "ewp_key_persion_id")
    private Long ewpKeyPersonId;

    @Column(name = "ewp_dispute_contact_id")
    private Long ewpDisputeContactId;

    @Column(name = "create_by")
    private String createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_by")
    private String updateBy;

    @Temporal(TemporalType.TIMESTAMP)
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
